package com.saj.simplenote.domain.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Centralized date helpers to ensure consistent formatting & parsing.
 * Remote API examples observed:
 *  - yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'
 *  - yyyy-MM-dd'T'HH:mm:ss'Z'
 * Offline provisional previously used a bare "yyyy-MM-dd'T'HH:mm:ss" which failed parsing.
 */
object DateUtil {

    // Display pattern required: e.g. "Sep 13, 2025" (single day digit when < 10)
    private const val DISPLAY_PATTERN = "MMM d, yyyy"

    private val displayFormatter = SimpleDateFormat(DISPLAY_PATTERN, Locale.US)

    // Ordered list of accepted ISO-ish input patterns
    private val inputPatterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss" // fallback (assumed UTC)
    )

    /**
     * Format an API timestamp (or provisional local timestamp) into the display form.
     * If parsing fails, the original string is returned to avoid blank UI.
     */
    fun formatForDisplay(raw: String): String {
        if (raw.isBlank()) return raw
        for (pattern in inputPatterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val date = sdf.parse(raw)
                if (date != null) return displayFormatter.format(date)
            } catch (_: Exception) { /* try next */ }
        }
        return raw
    }

    /**
     * Generates a current UTC timestamp in the canonical pattern we send when offline.
     * We choose the most common remote format without microseconds for simplicity.
     */
    fun currentIsoUtc(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return sdf.format(Date())
    }
}
