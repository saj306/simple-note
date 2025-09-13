package com.saj.simplenote.home.ui.model

import com.saj.simplenote.home.data.model.Note

data class HomeUIState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val hasMorePages: Boolean = false,
    val isLoadingMore: Boolean = false,
    val totalCount: Int = 0,
)
