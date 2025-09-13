package com.saj.simplenote.domain.util

import kotlin.random.Random

object NoteColorUtil {
    
    // Predefined set of light, pleasant colors for note backgrounds
    private val lightColors = listOf(
        "#FFE1F5FE", // Light Blue
        "#FFECF0F1", // Light Gray
        "#FFF1F8E9", // Light Green
        "#FFFFF8E1", // Light Yellow
        "#FFFCE4EC", // Light Pink
        "#FFF3E5F5", // Light Purple
        "#FFE8F5E8", // Light Mint
        "#FFFEF7E0", // Light Peach
        "#FFE3F2FD", // Light Sky Blue
        "#FFF9FBE7", // Light Lime
        "#FFEDE7F6", // Light Lavender
        "#FFFFF3E0", // Light Orange
        "#FFE0F2F1", // Light Teal
        "#FFFDF5E6", // Light Cream
        "#FFF8E1FF"  // Light Magenta
    )

    fun getRandomLightColor(): String {
        return lightColors[Random.nextInt(lightColors.size)]
    }

    fun getDefaultNoteColor(): String {
        return lightColors.first()
    }

    fun getColorForNoteId(noteId: Int): androidx.compose.ui.graphics.Color {
        val colorIndex = kotlin.math.abs(noteId) % lightColors.size
        val colorString = lightColors[colorIndex]
        return try {
            androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(colorString))
        } catch (e: Exception) {
            androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(getDefaultNoteColor()))
        }
    }
    
}
