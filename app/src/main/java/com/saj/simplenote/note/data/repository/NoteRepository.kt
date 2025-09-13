package com.saj.simplenote.note.data.repository

import com.google.gson.Gson
import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.home.data.model.CreateNoteRequest
import com.saj.simplenote.home.data.model.Note
import com.saj.simplenote.login.data.model.ApiError
import com.saj.simplenote.offline.local.NoteDao
import com.saj.simplenote.offline.local.toDomain
import com.saj.simplenote.offline.local.toEntity
import com.saj.simplenote.offline.sync.SyncManager
import com.saj.simplenote.domain.util.DateUtil

class NoteRepository(
    private val apiService: ApiService,
    private val noteDao: NoteDao,
    private val syncManager: SyncManager,
) {
    
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
                val errorBody = response.errorBody()?.string()
                val apiError = try { Gson().fromJson(errorBody, ApiError::class.java) } catch (e: Exception) { null }
                val errorMessage = apiError?.errors?.firstOrNull()?.detail ?: "Create note failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val nowIso = DateUtil.currentIsoUtc()
            val provisionalId = (System.currentTimeMillis() / 1000).toInt()
            val provisional = Note(
                id = provisionalId,
                title = title,
                description = description,
                created_at = nowIso,
                updated_at = nowIso,
                creator_name = "",
                creator_username = ""
            )
            noteDao.upsert(provisional.toEntity(dirty = true, provisionalId = provisionalId))
            syncManager.enqueueCreate(provisional)
            Result.success(provisional)
        }
    }
    
    suspend fun getNoteById(noteId: Int): Result<Note> {
        // try local first
        val local = noteDao.getById(noteId)
        if (local != null) return Result.success(local.toDomain())
        return try {
            val response = apiService.getNoteById(noteId)
            if (response.isSuccessful) {
                response.body()?.let { note ->
                    noteDao.upsert(note.toEntity())
                    Result.success(note)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = try { Gson().fromJson(errorBody, ApiError::class.java) } catch (e: Exception) { null }
                val errorMessage = apiError?.errors?.firstOrNull()?.detail ?: "Get note failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateNote(noteId: Int, title: String, description: String): Result<Note> {
        val existing = noteDao.getById(noteId)
        suspend fun fallback(reason: String?): Result<Note> {
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
                } ?: fallback("Empty response body")
            } else {
                fallback(null)
            }
        } catch (_: Exception) { fallback(null) }
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
            } else fallback()
        } catch (_: Exception) { fallback() }
    }
}
