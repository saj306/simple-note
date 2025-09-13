package com.saj.simplenote.domain.model

import com.saj.simplenote.home.data.model.Note
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class NoteUpdateEvent {
    data class NoteCreated(val note: Note) : NoteUpdateEvent()
    data class NoteUpdated(val note: Note) : NoteUpdateEvent()
    data class NoteDeleted(val noteId: Int) : NoteUpdateEvent()
}

class NotesUpdateManager {
    private val _noteUpdates = MutableSharedFlow<NoteUpdateEvent>()
    val noteUpdates = _noteUpdates.asSharedFlow()
    
    suspend fun notifyNoteCreated(note: Note) {
        _noteUpdates.emit(NoteUpdateEvent.NoteCreated(note))
    }
    
    suspend fun notifyNoteUpdated(note: Note) {
        _noteUpdates.emit(NoteUpdateEvent.NoteUpdated(note))
    }
    
    suspend fun notifyNoteDeleted(noteId: Int) {
        _noteUpdates.emit(NoteUpdateEvent.NoteDeleted(noteId))
    }
}
