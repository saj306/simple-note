package com.saj.simplenote.domain.util

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.login.data.model.RefreshTokenRequest

class TokenManager(
    private val preferencesManager: PreferencesManager,
    private val apiService: ApiService
) {
    
    fun hasValidTokens(): Boolean {
        val accessToken = preferencesManager.getString(SharedPrefKeys.AccessToken.key, "")
        val refreshToken = preferencesManager.getString(SharedPrefKeys.RefreshToken.key, "")
        return accessToken.isNotEmpty() && refreshToken.isNotEmpty()
    }
    
    suspend fun validateAndRefreshTokens(): Boolean {
        val refreshToken = preferencesManager.getString(SharedPrefKeys.RefreshToken.key, "")
        
        if (refreshToken.isEmpty()) {
            clearTokens()
            return false
        }
        
        return try {
            val request = RefreshTokenRequest(refresh = refreshToken)
            val response = apiService.refreshToken(request)

            if (response.isSuccessful) {
                val newTokenResponse = response.body()
                if (newTokenResponse != null) {
                    preferencesManager.putString(SharedPrefKeys.AccessToken.key, newTokenResponse.access)
                    true
                } else {
                    // Treat empty body as transient issue – keep old token
                    true
                }
            } else {
                // Only clear tokens on explicit auth failures
                return when (response.code()) {
                    401, 403 -> { clearTokens(); false }
                    else -> true // keep existing tokens, allow offline usage / retry later
                }
            }
        } catch (e: Exception) {
            // Network / parsing error: keep existing tokens so offline still works
            true
        }
    }
    
    fun clearTokens() {
        preferencesManager.remove(SharedPrefKeys.AccessToken.key)
        preferencesManager.remove(SharedPrefKeys.RefreshToken.key)
        preferencesManager.remove(SharedPrefKeys.Username.key)
        preferencesManager.remove(SharedPrefKeys.Password.key)
    }
}
