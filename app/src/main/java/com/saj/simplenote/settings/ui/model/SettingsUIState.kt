package com.saj.simplenote.settings.ui.model

data class SettingsUIState(
    val userName: String = "",
    val userEmail: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showLogoutDialog: Boolean = false
)
