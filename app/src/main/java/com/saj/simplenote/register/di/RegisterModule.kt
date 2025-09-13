package com.saj.simplenote.register.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.register.data.repository.RegisterRepository
import com.saj.simplenote.register.ui.viewmodel.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val registerModule = module {
    
    single { RegisterRepository(get()) }

    viewModel<RegisterViewModel> {
        RegisterViewModel(get(), get())
    }

}
