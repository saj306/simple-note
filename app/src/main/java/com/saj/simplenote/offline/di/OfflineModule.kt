package com.saj.simplenote.offline.di

import androidx.room.Room
import com.saj.simplenote.offline.local.AppDatabase
import com.saj.simplenote.offline.sync.NetworkStatusProvider
import com.saj.simplenote.offline.sync.SyncManager
import org.koin.dsl.module

val offlineModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "simplenote.db").build()
    }
    single { get<AppDatabase>().noteDao() }
    single { get<AppDatabase>().pendingActionDao() }
    single { NetworkStatusProvider(get()) }
    single { SyncManager(get(), get(), get(), get()) }
}
