package com.saj.simplenote.onboarding.ui.viewmodel

import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.domain.util.SharedPrefKeys

class OnboardingViewModel(
    private val preferencesManager: PreferencesManager,
) : SimpleNoteViewModel() {

    val navLogin: NavigationEvent = NavigationEvent()

    fun onGetStartedClick() {
        // Mark onboarding as completed
        preferencesManager.putBoolean(SharedPrefKeys.OnboardingCompleted.key, true)
        navLogin.navigate()
    }

}