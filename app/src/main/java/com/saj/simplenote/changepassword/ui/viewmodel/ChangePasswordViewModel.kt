package com.saj.simplenote.changepassword.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.saj.simplenote.changepassword.data.model.ChangePasswordRequest
import com.saj.simplenote.changepassword.data.repository.ChangePasswordRepository
import com.saj.simplenote.changepassword.ui.model.ChangePasswordUIState
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordRepository: ChangePasswordRepository
) : SimpleNoteViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUIState())
    val uiState: StateFlow<ChangePasswordUIState> = _uiState.asStateFlow()

    val navBack: NavigationEvent = NavigationEvent()

    fun onCurrentPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            currentPassword = password,
            errorMessage = null
        )
    }

    fun onNewPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            newPassword = password,
            errorMessage = null
        )
    }

    fun onRetypeNewPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            retypeNewPassword = password,
            errorMessage = null
        )
    }

    fun onSubmitClick() {
        val currentState = _uiState.value
        
        // Validate inputs
        if (currentState.currentPassword.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Current password is required")
            return
        }
        
        if (currentState.newPassword.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "New password is required")
            return
        }
        
        if (currentState.retypeNewPassword.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please confirm your new password")
            return
        }
        
        if (currentState.newPassword != currentState.retypeNewPassword) {
            _uiState.value = currentState.copy(errorMessage = "New passwords do not match")
            return
        }
        
        if (currentState.newPassword.length < 8) {
            _uiState.value = currentState.copy(errorMessage = "New password must be at least 8 characters long")
            return
        }
        
        if (currentState.currentPassword == currentState.newPassword) {
            _uiState.value = currentState.copy(errorMessage = "New password must be different from current password")
            return
        }

        // Submit password change
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            val request = ChangePasswordRequest(
                oldPassword = currentState.currentPassword,
                newPassword = currentState.newPassword
            )
            
            changePasswordRepository.changePassword(request).fold(
                onSuccess = { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Password changed successfully",
                        currentPassword = "",
                        newPassword = "",
                        retypeNewPassword = ""
                    )
                    
                    // Navigate back after a short delay
                    kotlinx.coroutines.delay(1500)
                    navBack.navigate()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Password change failed"
                    )
                }
            )
        }
    }

    fun onBackClick() {
        navBack.navigate()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false, successMessage = null)
    }
}
