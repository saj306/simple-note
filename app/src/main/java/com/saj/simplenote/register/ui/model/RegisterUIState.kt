package com.saj.simplenote.register.ui.model

data class RegisterUIState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val retypePassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
