package com.saj.simplenote.changepassword.ui.model

data class ChangePasswordUIModel(
    val title: String,
    val currentPasswordLabel: String,
    val newPasswordLabel: String,
    val retypeNewPasswordLabel: String,
    val currentPasswordPlaceholder: String,
    val newPasswordPlaceholder: String,
    val retypeNewPasswordPlaceholder: String,
    val submitButtonText: String,
    val backButtonText: String
)
