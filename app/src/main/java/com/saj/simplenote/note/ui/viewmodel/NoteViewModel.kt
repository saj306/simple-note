package com.saj.simplenote.note.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.NotesUpdateManager
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.domain.util.SharedPrefKeys
import com.saj.simplenote.note.data.repository.NoteRepository
import com.saj.simplenote.note.ui.model.NoteState
import com.saj.simplenote.note.ui.model.NoteUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NoteViewModel(
    private val preferencesManager: PreferencesManager,
    private val noteRepository: NoteRepository,
    private val notesUpdateManager: NotesUpdateManager,
) : SimpleNoteViewModel() {

    private val _uiState = MutableStateFlow(NoteUIState())
    val uiState: StateFlow<NoteUIState> = _uiState.asStateFlow()

    val navBack: NavigationEvent = NavigationEvent()

    private var noteState: NoteState = NoteState.CREATE
    private var originalTitle: String = ""
    private var originalContent: String = ""
    private var autoSaveJob: Job? = null

    companion object {
        private const val AUTO_SAVE_DELAY_MS = 1000L // Auto-save after 1 second of inactivity
    }

    fun initializeNote(noteId: Int? = null) {
        if (noteId != null) {
            // Edit mode
            noteState = NoteState.EDIT
            _uiState.value = _uiState.value.copy(
                noteId = noteId,
                isEditMode = true,
                isLoading = true
            )
            loadNote(noteId)
        } else {
            // Create mode
            noteState = NoteState.CREATE
            _uiState.value = _uiState.value.copy(
                noteId = null,
                isEditMode = false,
                lastEdited = getCurrentTimestamp()
            )
        }
    }

    private fun loadNote(noteId: Int) {
        viewModelScope.launch {
            noteRepository.getNoteById(noteId)
                .onSuccess { note ->
                    originalTitle = note.title
                    originalContent = note.description
                    _uiState.value = _uiState.value.copy(
                        title = note.title,
                        content = note.description,
                        lastEdited = formatDate(note.updated_at),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load note"
                    )
                }
        }
    }

    fun onTitleChanged(newTitle: String) {
        _uiState.value = _uiState.value.copy(
            title = newTitle,
            isDirty = isDirty(newTitle, _uiState.value.content)
        )
        scheduleAutoSave()
    }

    fun onContentChanged(newContent: String) {
        _uiState.value = _uiState.value.copy(
            content = newContent,
            isDirty = isDirty(_uiState.value.title, newContent)
        )
        scheduleAutoSave()
    }

    private fun scheduleAutoSave() {
        // Cancel previous auto-save job
        autoSaveJob?.cancel()
        
        // Only auto-save if there's content and we're not already saving
        val currentState = _uiState.value
        if ((currentState.title.trim().isNotEmpty() || currentState.content.trim().isNotEmpty()) 
            && !currentState.isSaving && currentState.isDirty) {
            
            autoSaveJob = viewModelScope.launch {
                delay(AUTO_SAVE_DELAY_MS)
                autoSave()
            }
        }
    }

    private suspend fun autoSave() {
        val currentState = _uiState.value
        if (!currentState.isDirty || currentState.isSaving) return

        // Don't auto-save if title is empty but content exists (wait for user to add title)
        if (currentState.title.trim().isEmpty() && currentState.content.trim().isNotEmpty()) {
            return
        }

        // Don't auto-save if both title and content are empty
        if (currentState.title.trim().isEmpty() && currentState.content.trim().isEmpty()) {
            return
        }

        // Set auto-saving state
        _uiState.value = currentState.copy(isAutoSaving = true)
        saveNote()
    }

    private fun isDirty(title: String, content: String): Boolean {
        return when (noteState) {
            NoteState.CREATE -> title.isNotEmpty() || content.isNotEmpty()
            NoteState.EDIT -> title != originalTitle || content != originalContent
            NoteState.VIEW -> false
        }
    }

    fun saveNote() {
        val currentState = _uiState.value
        if (currentState.title.trim().isEmpty()) {
            return // Don't save if title is empty
        }

        _uiState.value = currentState.copy(isSaving = true, errorMessage = null)

        viewModelScope.launch {
            val result = if (noteState == NoteState.CREATE) {
                noteRepository.createNote(currentState.title.trim(), currentState.content.trim())
            } else {
                noteRepository.updateNote(
                    currentState.noteId!!, 
                    currentState.title.trim(), 
                    currentState.content.trim()
                )
            }

            result
                .onSuccess { note ->
                    originalTitle = note.title
                    originalContent = note.description
                    _uiState.value = _uiState.value.copy(
                        noteId = note.id,
                        title = note.title,
                        content = note.description,
                        lastEdited = formatDate(note.updated_at),
                        isSaving = false,
                        isAutoSaving = false,
                        isDirty = false,
                        errorMessage = null
                    )
                    
                    // Notify other screens about the note update
                    viewModelScope.launch {
                        if (noteState == NoteState.CREATE) {
                            notesUpdateManager.notifyNoteCreated(note)
                            noteState = NoteState.EDIT
                        } else {
                            notesUpdateManager.notifyNoteUpdated(note)
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        isAutoSaving = false,
                        errorMessage = exception.message ?: "Failed to save note"
                    )
                }
        }
    }

    fun deleteNote() {
        val currentState = _uiState.value
        if (currentState.noteId == null) return

        _uiState.value = currentState.copy(isDeleting = true, errorMessage = null, showDeleteConfirmationBottomSheet = false)

        viewModelScope.launch {
            noteRepository.deleteNote(currentState.noteId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isDeleting = false)
                    
                    // Notify other screens about the note deletion
                    notesUpdateManager.notifyNoteDeleted(currentState.noteId)
                    
                    navBack.navigate()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        errorMessage = exception.message ?: "Failed to delete note"
                    )
                }
        }
    }

    fun showDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(showDeleteConfirmationBottomSheet = true)
    }

    fun hideDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(showDeleteConfirmationBottomSheet = false)
    }

    fun onBackPressed() {
        // Cancel any pending auto-save
        autoSaveJob?.cancel()
        
        // Perform final save if there are unsaved changes
        val currentState = _uiState.value
        if (currentState.isDirty && currentState.title.trim().isNotEmpty() && !currentState.isSaving) {
            viewModelScope.launch {
                saveNote()
                // Navigate back after save completes
                navBack.navigate()
            }
        } else {
            navBack.navigate()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tehran")
        return "Last edited on ${sdf.format(Date())}"
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Assume input is in UTC

            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("Asia/Tehran") // Convert to Tehran time

            val date = inputFormat.parse(dateString)
            "Last edited on ${outputFormat.format(date)}"
        } catch (e: Exception) {
            dateString // Fallback to the original string if parsing fails
        }
    }
}
