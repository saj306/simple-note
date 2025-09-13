package com.saj.simplenote.register.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.register.data.repository.RegisterRepository
import com.saj.simplenote.register.ui.model.RegisterUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val preferencesManager: PreferencesManager,
    private val registerRepository: RegisterRepository,
) : SimpleNoteViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    val navLogin: NavigationEvent = NavigationEvent()
    val navBack: NavigationEvent = NavigationEvent()

    fun onFirstNameChanged(firstName: String) {
        _uiState.value = _uiState.value.copy(
            firstName = firstName,
            errorMessage = null
        )
    }

    fun onLastNameChanged(lastName: String) {
        _uiState.value = _uiState.value.copy(
            lastName = lastName,
            errorMessage = null
        )
    }

    fun onUsernameChanged(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            errorMessage = null
        )
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            errorMessage = null
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun onRetypePasswordChanged(retypePassword: String) {
        _uiState.value = _uiState.value.copy(
            retypePassword = retypePassword,
            errorMessage = null
        )
    }

    fun onRegisterClick() {
        val currentState = _uiState.value
        
        // Basic validation
        if (currentState.firstName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "First name is required")
            return
        }
        
        if (currentState.lastName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Last name is required")
            return
        }
        
        if (currentState.username.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Username is required")
            return
        }
        
        if (currentState.email.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email is required")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid email address")
            return
        }
        
        if (currentState.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password is required")
            return
        }
        
        if (currentState.password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }
        
        if (currentState.retypePassword.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please retype your password")
            return
        }
        
        if (currentState.password != currentState.retypePassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            registerRepository.register(
                username = currentState.username,
                password = currentState.password,
                email = currentState.email,
                firstName = currentState.firstName,
                lastName = currentState.lastName
            ).fold(
                onSuccess = { registerResponse ->
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    // Navigate back to login after successful registration
                    navLogin.navigate()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Registration failed"
                    )
                }
            )
        }
    }

    fun onLoginClick() {
        navLogin.navigate()
    }
}
