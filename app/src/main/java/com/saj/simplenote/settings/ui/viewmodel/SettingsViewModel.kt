package com.saj.simplenote.settings.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.domain.util.SharedPrefKeys
import com.saj.simplenote.domain.util.TokenManager
import com.saj.simplenote.settings.ui.model.SettingsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val tokenManager: TokenManager,
) : SimpleNoteViewModel() {

    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    val navBack: NavigationEvent = NavigationEvent()
    val navLogin: NavigationEvent = NavigationEvent()
    val navChangePassword: NavigationEvent = NavigationEvent()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val username = preferencesManager.getString(SharedPrefKeys.Username.key, "")
        // For now, we'll create a placeholder email based on username
        // In a real app, this would come from user profile API
        val email = if (username.isNotEmpty()) "$username@example.com" else ""
        
        _uiState.value = _uiState.value.copy(
            userName = username,
            userEmail = email
        )
    }

    fun onBackClick() {
        navBack.navigate()
    }

    fun onChangePasswordClick() {
        navChangePassword.navigate()
    }

    fun onLogOutClick() {
        _uiState.value = _uiState.value.copy(showLogoutDialog = true)
    }

    fun onLogoutDialogCancel() {
        _uiState.value = _uiState.value.copy(showLogoutDialog = false)
    }

    fun onLogoutDialogConfirm() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showLogoutDialog = false,
                isLoading = true
            )
            
            // Clear all stored user data using TokenManager
            tokenManager.clearTokens()
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            // Navigate to login screen
            navLogin.navigate()
        }
    }
}
