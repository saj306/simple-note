package com.saj.simplenote.home.data.repository

import com.google.gson.Gson
import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.home.data.model.CreateNoteRequest
import com.saj.simplenote.home.data.model.Note
import com.saj.simplenote.home.data.model.NotesResponse
import com.saj.simplenote.login.data.model.ApiError
import com.saj.simplenote.offline.local.NoteDao
import com.saj.simplenote.offline.local.toDomain
import com.saj.simplenote.offline.local.toEntity
import com.saj.simplenote.offline.sync.SyncManager
import kotlinx.coroutines.flow.first

class HomeRepository(
    private val apiService: ApiService,
    private val noteDao: NoteDao,
    private val syncManager: SyncManager,
) {
    
    suspend fun getNotes(page: Int? = null, pageSize: Int? = null): Result<NotesResponse> {
        val currentPage = page ?: 1
        val size = pageSize ?: 10

        return try {
            // Always hit network for requested page so pagination works.
            val response = apiService.getNotes(currentPage, size)
            if (response.isSuccessful) {
                response.body()?.let { notesResponse ->
                    // Cache/refresh locally – upsert keeps existing dirty flags if implemented that way.
                    noteDao.upsertAll(notesResponse.results.map { it.toEntity() })
                    Result.success(notesResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception(parseError(response.code(), response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            // Offline fallback: emulate pagination from local cache if available
            val localNotes = noteDao.observeNotes().first().map { it.toDomain() }
            if (localNotes.isEmpty()) {
                Result.failure(Exception("Network error. Please check your connection and try again."))
            } else {
                val fromIndex = (currentPage - 1) * size
                val toIndex = (fromIndex + size).coerceAtMost(localNotes.size)
                val pageSlice = if (fromIndex in 0 until localNotes.size) {
                    localNotes.subList(fromIndex, toIndex)
                } else emptyList()
                val hasNext = toIndex < localNotes.size
                Result.success(
                    NotesResponse(
                        count = localNotes.size,
                        next = if (hasNext) "offline://next" else null,
                        previous = if (currentPage > 1) "offline://prev" else null,
                        results = pageSlice
                    )
                )
            }
        }
    }
    
    suspend fun searchNotes(
        query: String,
        page: Int? = null,
        pageSize: Int? = null
    ): Result<NotesResponse> {
        return try {
            // Since the API likely uses AND logic when both title and description are provided,
            // we'll search in both title and description with separate calls and combine results
            val queryString = query.takeIf { it.isNotEmpty() } ?: return Result.failure(Exception("Empty search query"))
            
            // Search in title
            val titleResponse = apiService.filterNotes(
                title = queryString,
                description = null,
                page = page,
                pageSize = pageSize
            )
            
            // Search in description  
            val descriptionResponse = apiService.filterNotes(
                title = null,
                description = queryString,
                page = page,
                pageSize = pageSize
            )
            
            when {
                titleResponse.isSuccessful && descriptionResponse.isSuccessful -> {
                    val titleNotes = titleResponse.body()?.results ?: emptyList()
                    val descriptionNotes = descriptionResponse.body()?.results ?: emptyList()
                    
                    // Combine results and remove duplicates based on note ID
                    val combinedNotes = (titleNotes + descriptionNotes).distinctBy { it.id }
                    
                    // Use the title response as base and update with combined results
                    val baseResponse = titleResponse.body()!!
                    val combinedResponse = baseResponse.copy(
                        results = combinedNotes,
                        count = combinedNotes.size
                    )
                    
                    Result.success(combinedResponse)
                }
                titleResponse.isSuccessful -> {
                    titleResponse.body()?.let { notesResponse ->
                        Result.success(notesResponse)
                    } ?: Result.failure(Exception("Empty response body"))
                }
                descriptionResponse.isSuccessful -> {
                    descriptionResponse.body()?.let { notesResponse ->
                        Result.success(notesResponse)
                    } ?: Result.failure(Exception("Empty response body"))
                }
                else -> {
                    // Both failed, return the title response error
                    handleErrorResponse(titleResponse)
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Please check your connection and try again."))
        }
    }

    private fun handleErrorResponse(response: retrofit2.Response<NotesResponse>): Result<NotesResponse> {
        val errorMessage = when (response.code()) {
            401 -> "Authentication failed. Please login again."
            403 -> "Access denied."
            404 -> "No notes found."
            500 -> "Server error. Please try again later."
            else -> {
                try {
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                        apiError.errors.firstOrNull()?.detail ?: "Unknown error occurred"
                    } else {
                        "Unknown error occurred"
                    }
                } catch (e: Exception) {
                    "Unknown error occurred"
                }
            }
        }
        return Result.failure(Exception(errorMessage))
    }
    
    suspend fun createNote(title: String, description: String): Result<Note> {
        return try {
            val request = CreateNoteRequest(title = title, description = description)
            val response = apiService.createNote(request)
            if (response.isSuccessful) {
                response.body()?.let { note ->
                    noteDao.upsert(note.toEntity())
                    Result.success(note)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception(parseError(response.code(), response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            // Offline create: create provisional note using current timestamp as updated_at
            val provisional = Note(
                id = (System.currentTimeMillis() / 1000).toInt(),
                title = title,
                description = description,
                created_at = "", // could store local timestamp
                updated_at = "9999-12-31T23:59:59", // ensure sorts top until synced
                creator_name = "",
                creator_username = ""
            )
            noteDao.upsert(provisional.toEntity(dirty = true))
            syncManager.enqueueCreate(provisional)
            Result.success(provisional)
        }
    }
    
    suspend fun updateNote(noteId: Int, title: String, description: String): Result<Note> {
        val existing = noteDao.getById(noteId)
        suspend fun performOfflineFallback(reason: String?): Result<Note> {
            return if (existing != null) {
                val updated = existing.copy(title = title, description = description, dirty = true)
                noteDao.upsert(updated)
                syncManager.enqueueUpdate(updated.toDomain())
                Result.success(updated.toDomain())
            } else Result.failure(Exception(reason ?: "Note not found locally"))
        }
        return try {
            val request = CreateNoteRequest(title = title, description = description)
            val response = apiService.updateNote(noteId, request)
            if (response.isSuccessful) {
                response.body()?.let { note ->
                    noteDao.upsert(note.toEntity())
                    Result.success(note)
                } ?: performOfflineFallback("Empty body")
            } else {
                // Treat server error (like 404 due to provisional ID) as offline fallback scenario
                performOfflineFallback(parseError(response.code(), response.errorBody()?.string()))
            }
        } catch (_: Exception) {
            performOfflineFallback(null)
        }
    }
    
    suspend fun deleteNote(noteId: Int): Result<Unit> {
        suspend fun fallback(): Result<Unit> {
            noteDao.markDeleted(noteId)
            syncManager.enqueueDelete(noteId)
            return Result.success(Unit)
        }
        return try {
            val response = apiService.deleteNote(noteId)
            if (response.isSuccessful) {
                noteDao.hardDelete(noteId)
                Result.success(Unit)
            } else {
                // fallback for provisional ID/other errors
                fallback()
            }
        } catch (_: Exception) { fallback() }
    }

    private fun parseError(code: Int, errorBody: String?): String {
        return when (code) {
            400 -> "Invalid note data. Please check your input."
            401 -> "Authentication failed. Please login again."
            403 -> "Access denied."
            404 -> "Note not found."
            500 -> "Server error. Please try again later."
            else -> {
                try {
                    if (!errorBody.isNullOrEmpty()) {
                        val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                        apiError.errors.firstOrNull()?.detail ?: "Unknown error occurred"
                    } else {
                        "Unknown error occurred"
                    }
                } catch (e: Exception) {
                    "Unknown error occurred"
                }
            }
        }
    }
}
