package com.saj.simplenote.home.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.home.data.repository.HomeRepository
import com.saj.simplenote.home.ui.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    
    single { HomeRepository(get(), get(), get()) }

    viewModel<HomeViewModel> {
        HomeViewModel(get(), get(), get(), get())
    }

}
