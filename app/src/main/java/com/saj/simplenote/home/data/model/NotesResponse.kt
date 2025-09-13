package com.saj.simplenote.home.data.model

data class NotesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Note>
)
