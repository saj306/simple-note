package com.saj.simplenote
import android.app.Application
import com.saj.simplenote.changepassword.di.changePasswordModule
import com.saj.simplenote.domain.di.domainModule
import com.saj.simplenote.domain.di.networkModule
import com.saj.simplenote.home.di.homeModule
import com.saj.simplenote.login.di.loginModule
import com.saj.simplenote.note.di.noteModule
import com.saj.simplenote.offline.di.offlineModule
import com.saj.simplenote.onboarding.di.onboardingModule
import com.saj.simplenote.register.di.registerModule
import com.saj.simplenote.settings.di.settingsModule
import com.saj.simplenote.splash.di.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin


class SimpleNoteApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@SimpleNoteApplication)
            modules(
                domainModule,
                networkModule,
                splashModule,
                offlineModule,
                onboardingModule,
                loginModule,
                registerModule,
                homeModule,
                noteModule,
                settingsModule,
                changePasswordModule
            )
        }
    }
}