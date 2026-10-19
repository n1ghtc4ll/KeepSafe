package com.example.cardioproject.settings.presentation.di

import com.example.cardioproject.settings.data.local.SettingsPreferencesDataSource
import com.example.cardioproject.settings.data.repository.SettingsRepositoryImpl
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import com.example.cardioproject.settings.domain.usecase.AutoCalculateHeartRateZonesUseCase
import com.example.cardioproject.settings.domain.usecase.TagInteractor
import com.example.cardioproject.settings.domain.usecase.UpdateHeartRateZoneUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateSignalVolumeUseCase
import com.example.cardioproject.settings.presentation.SettingsViewModel
import com.example.cardioproject.settings.presentation.viewmodel.DeviceConnectViewModel
import com.example.cardioproject.settings.presentation.viewmodel.ProfileBuilderViewModel
import org.koin.core.module.dsl.viewModel
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

    single<SettingsRepository> { SettingsRepositoryImpl(get(), get(), get()) }

    factory { UpdateSignalVolumeUseCase(get()) }
    factory { TagInteractor(get()) }
    factory { UpdateHeartRateZoneUseCase(get()) }
    factory { AutoCalculateHeartRateZonesUseCase(get()) }

    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            updateSignalVolume = get(),
            tagInteractor = get(),
            updateHeartRateZone = get(),
            autoCalculateHeartRateZones = get(),
        )
    }

    viewModel { parameters ->
        ProfileBuilderViewModel(
            profileId = parameters.getOrNull(),
            repository = get()
        )
    }
    viewModel { DeviceConnectViewModel(get(), get(), get()) }
}