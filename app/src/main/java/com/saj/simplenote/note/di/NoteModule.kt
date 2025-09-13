package com.saj.simplenote.note.di

import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.note.data.repository.NoteRepository
import com.saj.simplenote.note.ui.viewmodel.NoteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noteModule = module {
    
    single { NoteRepository(get(), get(), get()) }

    viewModel<NoteViewModel> {
        NoteViewModel(get(), get(), get())
    }

}
