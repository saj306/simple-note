package com.saj.simplenote.offline.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val creator_name: String,
    val creator_username: String,
    // Track if this note has local unsynced changes
    val dirty: Boolean = false,
    // Track if note was locally deleted (tombstone) to sync delete later
    val deleted: Boolean = false,
    // Original provisional local ID (epoch seconds) when created offline; null for server-originated notes
    val provisionalId: Int? = null
)