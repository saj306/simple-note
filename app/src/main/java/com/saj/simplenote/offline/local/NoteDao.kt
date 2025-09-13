package com.saj.simplenote.offline.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE deleted = 0 ORDER BY updated_at DESC")
    fun observeNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(notes: List<NoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: NoteEntity)

    @Query("UPDATE notes SET deleted = 1, dirty = 1 WHERE id = :id")
    suspend fun markDeleted(id: Int)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun hardDelete(id: Int)

    @Query("SELECT * FROM notes WHERE dirty = 1")
    suspend fun getDirtyNotes(): List<NoteEntity>
}