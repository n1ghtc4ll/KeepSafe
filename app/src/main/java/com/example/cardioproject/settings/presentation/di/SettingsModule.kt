package com.example.cardioproject.settings.presentation.di

import com.example.cardioproject.settings.data.local.SettingsPreferencesDataSource
import com.example.cardioproject.settings.data.repository.SettingsRepositoryImpl
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import com.example.cardioproject.settings.domain.usecase.AddWorkoutTagUseCase
import com.example.cardioproject.settings.domain.usecase.AutoCalculateHeartRateZonesUseCase
import com.example.cardioproject.settings.domain.usecase.EditWorkoutTagUseCase
import com.example.cardioproject.settings.domain.usecase.ObserveSettingsUseCase
import com.example.cardioproject.settings.domain.usecase.RemoveWorkoutTagUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateCriticalPulseAlertUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateHeartRateZoneUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateKeepScreenOnUseCase
import com.example.cardioproject.settings.domain.usecase.UpdatePhaseSoundUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateSignalVolumeUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateUserProfileUseCase
import com.example.cardioproject.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin-модуль для feature settings.
 * Подключить в общий список модулей приложения (там, где стартует Koin, например
 * в Application.onCreate -> startKoin { modules(...) }):
 *
 *   startKoin {
 *       modules(anthroDiaryModule, settingsModule, /* остальные */)
 *   }
 */
val settingsModule = module {

    single { SettingsPreferencesDataSource(context = get()) }

    single<SettingsRepository> { SettingsRepositoryImpl(dataSource = get()) }

    factory { ObserveSettingsUseCase(get()) }
    factory { UpdatePhaseSoundUseCase(get()) }
    factory { UpdateSignalVolumeUseCase(get()) }
    factory { AddWorkoutTagUseCase(get()) }
    factory { EditWorkoutTagUseCase(get()) }
    factory { RemoveWorkoutTagUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }
    factory { UpdateHeartRateZoneUseCase(get()) }
    factory { AutoCalculateHeartRateZonesUseCase(get()) }
    factory { UpdateCriticalPulseAlertUseCase(get()) }
    factory { UpdateKeepScreenOnUseCase(get()) }

    viewModel {
        SettingsViewModel(
            observeSettings = get(),
            updatePhaseSound = get(),
            updateSignalVolume = get(),
            addWorkoutTag = get(),
            editWorkoutTag = get(),
            removeWorkoutTag = get(),
            updateUserProfile = get(),
            updateHeartRateZone = get(),
            autoCalculateHeartRateZones = get(),
            updateCriticalPulseAlert = get(),
            updateKeepScreenOn = get(),
        )
    }
}