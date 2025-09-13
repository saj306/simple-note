package com.saj.simplenote.note.ui.model

data class NoteUIState(
    val noteId: Int? = null,
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false,
    val lastEdited: String = "",
    val isDirty: Boolean = false,
    val isAutoSaving: Boolean = false,
    val showDeleteConfirmationBottomSheet: Boolean = false,
)
