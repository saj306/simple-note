package com.saj.simplenote.settings.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.util.TokenManager
import com.saj.simplenote.settings.ui.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

}
