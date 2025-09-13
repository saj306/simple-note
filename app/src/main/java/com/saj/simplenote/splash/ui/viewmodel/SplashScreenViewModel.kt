package com.saj.simplenote.splash.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.domain.util.SharedPrefKeys
import com.saj.simplenote.domain.util.TokenManager
import com.saj.simplenote.offline.sync.NetworkStatusProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val preferencesManager: PreferencesManager,
    private val tokenManager: TokenManager,
    private val networkStatusProvider: NetworkStatusProvider,
) : SimpleNoteViewModel() {

    val navLogin: NavigationEvent = NavigationEvent()
    val navOnboarding: NavigationEvent = NavigationEvent()
    val navHome: NavigationEvent = NavigationEvent()

    fun checkOnboardingStatus() {
        val isOnboardingCompleted =
            preferencesManager.getBoolean(SharedPrefKeys.OnboardingCompleted.key, false)
        
        if (isOnboardingCompleted) {
            // Check if user has valid tokens
            if (tokenManager.hasValidTokens()) {
                // Attempt offline-first shortcut
                viewModelScope.launch {
                    val isOnline = try { networkStatusProvider.networkStatus().first() } catch (_: Exception) { true }
                    if (!isOnline) {
                        // Offline: skip refresh, proceed
                        navHome.navigate()
                    } else {
                        if (tokenManager.validateAndRefreshTokens()) {
                            navHome.navigate()
                        } else {
                            navLogin.navigate()
                        }
                    }
                }
            } else {
                // No valid tokens, go to login
                navLogin.navigate()
            }
        } else {
            // User needs to complete onboarding
            navOnboarding.navigate()
        }
    }

}