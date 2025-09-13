package com.saj.simplenote.note.ui.model

data class NoteUIModel(
    val title: String,
    val titlePlaceholder: String,
    val contentPlaceholder: String,
    val deleteButton: String,
    val backButton: String,
    val lastEditedText: String,
    val savingText: String,
    val deleteConfirmationMessage: String,
    val deleteConfirmationConfirm: String,
)
