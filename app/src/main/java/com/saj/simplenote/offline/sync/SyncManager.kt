package com.saj.simplenote.offline.sync

import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.home.data.model.CreateNoteRequest
import com.saj.simplenote.home.data.model.Note
import com.saj.simplenote.offline.local.NoteDao
import com.saj.simplenote.offline.local.PendingActionDao
import com.saj.simplenote.offline.local.PendingActionEntity
import com.saj.simplenote.offline.local.toDomain
import com.saj.simplenote.offline.local.toEntity
import com.saj.simplenote.offline.local.toJson
import com.saj.simplenote.offline.local.toNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log

/**
 * Simple sync manager: when network becomes available, processes pending actions (FIFO),
 * then fetches latest notes and merges using last-write-wins by updated_at timestamp.
 */
class SyncManager(
    private val apiService: ApiService,
    private val noteDao: NoteDao,
    private val pendingActionDao: PendingActionDao,
    networkStatusProvider: NetworkStatusProvider,
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            networkStatusProvider.networkStatus().collectLatest { connected ->
                if (connected) {
                    runSync()
                }
            }
        }
    }

    suspend fun runSync() = withContext(Dispatchers.IO) {
        // 1. Push pending actions
        val pending = pendingActionDao.getAll()
        val processedIds = mutableListOf<Long>()
        for (action in pending) {
            Log.d("SyncManager", "Processing pending action id=${action.id} type=${action.action} noteId=${action.noteId}")
            val success = processPendingAction(action)
            if (success) processedIds.add(action.id)
        }
        if (processedIds.isNotEmpty()) pendingActionDao.deleteByIds(processedIds)
        // 2. Pull remote latest and merge
        try {
            val response = apiService.getNotes()
            if (response.isSuccessful) {
                val body = response.body()
                val remoteNotes = body?.results ?: emptyList()
                mergeRemote(remoteNotes)
            }
        } catch (_: Exception) { }
    }

    private suspend fun processPendingAction(action: PendingActionEntity): Boolean {
        return try {
            when (action.action) {
                ACTION_CREATE -> {
                    val note = action.payloadJson?.toNote() ?: return true
                    val request = CreateNoteRequest(note.title, note.description)
                    val response = apiService.createNote(request)
                    if (response.isSuccessful) {
                        response.body()?.let { created ->
                            // Replace provisional note (same content) by inserting server note; if IDs differ we also clean up remap.
                            if (created.id != note.id) {
                                Log.d("SyncManager", "Replacing provisional note id=${note.id} with server id=${created.id}")
                                pendingActionDao.remapNoteId(oldId = note.id, newId = created.id)
                            }
                            noteDao.upsert(created.toEntity(dirty = false, provisionalId = null))
                        }
                        true
                    } else false
                }
                ACTION_UPDATE -> {
                    val note = action.payloadJson?.toNote() ?: return true
                    val request = CreateNoteRequest(note.title, note.description)
                    val response = apiService.updateNote(note.id, request)
                    if (response.isSuccessful) {
                        response.body()?.let { updated ->
                            noteDao.upsert(updated.toEntity(dirty = false))
                        }
                        true
                    } else {
                        // If server rejected update (maybe because ID was provisional yet?) keep dirty for retry
                        false
                    }
                }
                ACTION_DELETE -> {
                    val id = action.noteId ?: return true
                    val response = apiService.deleteNote(id)
                    if (response.isSuccessful) {
                        noteDao.hardDelete(id)
                        true
                    } else {
                        // Keep pending delete for retry
                        false
                    }
                }
                else -> true
            }
        } catch (e: Exception) { false }
    }

    private suspend fun mergeRemote(remote: List<Note>) {
        // naive last-write-wins using updated_at lexicographical compare (ISO format assumed)
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val localDirty = noteDao.getDirtyNotes().associateBy { it.id }
        val remoteEntities = remote.map { r ->
            val existingDirty = localDirty[r.id]
            if (existingDirty != null) {
                // If local dirty and remote older, keep local (skip overwrite)
                val remoteUpdated = r.updated_at
                val localUpdated = existingDirty.updated_at
                return@map if (remoteUpdated > localUpdated) {
                    r.toEntity(dirty = false)
                } else {
                    existingDirty // Keep dirty version; will be pushed later
                }
            } else {
                r.toEntity(dirty = false)
            }
        }
        noteDao.upsertAll(remoteEntities)
    }

    fun enqueueCreate(note: Note) {
        scope.launch { pendingActionDao.insert(PendingActionEntity(noteId = note.id, action = ACTION_CREATE, payloadJson = note.toJson())) }
    }

    fun enqueueUpdate(note: Note) {
        scope.launch { pendingActionDao.insert(PendingActionEntity(noteId = note.id, action = ACTION_UPDATE, payloadJson = note.toJson())) }
    }

    fun enqueueDelete(noteId: Int) {
        scope.launch { pendingActionDao.insert(PendingActionEntity(noteId = noteId, action = ACTION_DELETE, payloadJson = null)) }
    }

    companion object {
        const val ACTION_CREATE = "CREATE"
        const val ACTION_UPDATE = "UPDATE"
        const val ACTION_DELETE = "DELETE"
    }
}