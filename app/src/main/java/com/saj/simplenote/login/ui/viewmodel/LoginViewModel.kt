package com.saj.simplenote.login.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.domain.util.SharedPrefKeys
import com.saj.simplenote.login.data.repository.LoginRepository
import com.saj.simplenote.login.ui.model.LoginUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val preferencesManager: PreferencesManager,
    private val loginRepository: LoginRepository,
) : SimpleNoteViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    val navMain: NavigationEvent = NavigationEvent()
    val navRegister: NavigationEvent = NavigationEvent()

    fun onUsernameChanged(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            errorMessage = null
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        
        // Basic validation
        if (currentState.username.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please enter your username")
            return
        }
        
        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please enter your password")
            return
        }

        // Start loading
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val result = loginRepository.login(
                    username = currentState.username,
                    password = currentState.password
                )
                
                result.fold(
                    onSuccess = { loginResponse ->
                        // Store tokens
                        preferencesManager.putString(SharedPrefKeys.AccessToken.key, loginResponse.access)
                        preferencesManager.putString(SharedPrefKeys.RefreshToken.key, loginResponse.refresh)
                        preferencesManager.putString(SharedPrefKeys.Username.key, currentState.username)
                        
                        _uiState.value = currentState.copy(isLoading = false)
                        navMain.navigate()
                    },
                    onFailure = { exception ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Login failed. Please try again."
                        )
                    }
                )
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "An unexpected error occurred. Please try again."
                )
            }
        }
    }

    fun onRegisterClick() {
        navRegister.navigate()
    }
}
