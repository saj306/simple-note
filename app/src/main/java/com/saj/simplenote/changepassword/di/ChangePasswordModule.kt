package com.saj.simplenote.changepassword.di

import com.saj.simplenote.changepassword.data.repository.ChangePasswordRepository
import com.saj.simplenote.changepassword.ui.viewmodel.ChangePasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val changePasswordModule = module {

    single { ChangePasswordRepository(get()) }

    viewModel<ChangePasswordViewModel> {
        ChangePasswordViewModel(get())
    }

}
