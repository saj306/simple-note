package com.saj.simplenote.splash.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.splash.ui.viewmodel.SplashScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {

    viewModel<SplashScreenViewModel> {
        SplashScreenViewModel(get(), get(), get())
    }

}