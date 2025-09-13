package com.saj.simplenote.offline.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PendingActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: PendingActionEntity)

    @Query("SELECT * FROM pending_actions ORDER BY timestamp ASC")
    suspend fun getAll(): List<PendingActionEntity>

    @Query("DELETE FROM pending_actions WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("DELETE FROM pending_actions")
    suspend fun clearAll()

    // When a provisional (offline-created) note receives a real server ID, remap all
    // subsequent pending actions (UPDATE/DELETE) to point at the new server ID so they succeed.
    @Query("UPDATE pending_actions SET noteId = :newId WHERE noteId = :oldId")
    suspend fun remapNoteId(oldId: Int, newId: Int)
}