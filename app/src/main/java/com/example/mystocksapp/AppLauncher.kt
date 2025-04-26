package com.example.mystocksapp

import android.app.Application
import androidx.activity.ComponentActivity

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AppLauncher : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppLauncher)
            modules(
                repositoryModule,
                viewModelModule,
                networkModule,
                objectBoxModule,
                imageModule
            )
        }
    }
}
