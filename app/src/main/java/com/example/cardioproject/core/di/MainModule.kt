package com.example.cardioproject.core.di

import androidx.room.Room
import com.example.cardioproject.Main
import com.example.cardioproject.anthrodiary.data.repository.AnthroDiaryRepositoryImpl
import com.example.cardioproject.anthrodiary.domain.repository.AnthroDiaryRepository
import com.example.cardioproject.core.common.data.db.AppDatabase
import com.example.cardioproject.core.common.data.repository.SettingsRepositoryImpl
import com.example.cardioproject.core.common.domain.repository.SettingsRepository
import com.example.cardioproject.core.navigation.Route
import com.example.cardioproject.core.navigation.TopLevelBackStack
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Main) }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "cardio_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<AppDatabase>().tagDao() }
    single { get<AppDatabase>().workoutSessionDao() }
    single { get<AppDatabase>().tabataProfileDao() }
    single { get<AppDatabase>().anthroMeasurementDao() }

    single<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }
    single<AnthroDiaryRepository> { AnthroDiaryRepositoryImpl(dao = get()) }
}