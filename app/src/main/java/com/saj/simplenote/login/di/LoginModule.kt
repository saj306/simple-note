package com.saj.simplenote.login.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.login.data.repository.LoginRepository
import com.saj.simplenote.login.ui.viewmodel.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    
    single { LoginRepository(get()) }

    viewModel<LoginViewModel> {
        LoginViewModel(get(), get())
    }

}
