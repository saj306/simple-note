package com.saj.simplenote.login.ui.model

data class LoginUIState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
