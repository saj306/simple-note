package com.saj.simplenote.onboarding.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.onboarding.ui.viewmodel.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {

    viewModel<OnboardingViewModel> {
        OnboardingViewModel(get())
    }

}