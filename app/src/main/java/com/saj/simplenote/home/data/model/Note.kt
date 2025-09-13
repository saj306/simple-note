package com.saj.simplenote.home.data.model


data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val creator_name: String,
    val creator_username: String,
)
