package com.saj.simplenote.changepassword.ui.model

data class ChangePasswordUIState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val retypeNewPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)
