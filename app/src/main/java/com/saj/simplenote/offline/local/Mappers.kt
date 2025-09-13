package com.saj.simplenote.offline.local

import com.saj.simplenote.home.data.model.Note
import com.google.gson.Gson

fun Note.toEntity(dirty: Boolean = false, deleted: Boolean = false, provisionalId: Int? = null): NoteEntity = NoteEntity(
    id = id,
    title = title,
    description = description,
    created_at = created_at,
    updated_at = updated_at,
    creator_name = creator_name,
    creator_username = creator_username,
    dirty = dirty,
    deleted = deleted,
    provisionalId = provisionalId
)

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    description = description,
    created_at = created_at,
    updated_at = updated_at,
    creator_name = creator_name,
    creator_username = creator_username
)

// Serialize / deserialize Note for pending actions
fun Note.toJson(): String = Gson().toJson(this)
fun String.toNote(): Note = Gson().fromJson(this, Note::class.java)
