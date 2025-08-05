package com.example.instagramclone

import android.app.Application
import com.example.instagramclone.di.appModule
import com.example.instagramclone.di.networkModule
import com.example.instagramclone.di.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext)
            modules(
                listOf(appModule, networkModule, viewmodelModule)
            )
        }
    }
}