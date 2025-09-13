package com.saj.simplenote.offline.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_actions")
data class PendingActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Int?, // null for create until server assigns? We'll use temporary negative IDs for local creates
    val action: String, // CREATE, UPDATE, DELETE
    val payloadJson: String?, // serialized note for create/update
    val timestamp: Long = System.currentTimeMillis()
)