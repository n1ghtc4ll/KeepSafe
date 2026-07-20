package com.example.cardioproject

import android.app.Application
import com.example.cardioproject.core.di.mainModule
import com.example.cardioproject.workout.di.workoutModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                mainModule,
                workoutModule
            )
        }
    }
}