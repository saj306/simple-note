package com.saj.simplenote.domain.di

import com.saj.simplenote.domain.model.NotesUpdateManager
import com.saj.simplenote.domain.model.PreferencesManager
import org.koin.dsl.module

val domainModule = module {

    single { PreferencesManager(get()) }
    single { NotesUpdateManager() }

}
