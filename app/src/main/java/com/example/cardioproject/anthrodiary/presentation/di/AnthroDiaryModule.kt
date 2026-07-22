package com.example.cardioproject.anthrodiary.presentation.di

import com.example.cardioproject.anthrodiary.data.local.AnthroDiaryDatabase
import com.example.cardioproject.anthrodiary.data.repository.AnthroDiaryRepositoryImpl
import com.example.cardioproject.anthrodiary.domain.repository.AnthroDiaryRepository
import com.example.cardioproject.anthrodiary.domain.usecase.DeleteMeasurementUseCase
import com.example.cardioproject.anthrodiary.domain.usecase.GetMeasurementByIdUseCase
import com.example.cardioproject.anthrodiary.domain.usecase.ObserveLatestMeasurementUseCase
import com.example.cardioproject.anthrodiary.domain.usecase.ObserveMeasurementsUseCase
import com.example.cardioproject.anthrodiary.domain.usecase.SaveMeasurementUseCase
import com.example.cardioproject.anthrodiary.presentation.AnthroDiaryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Подключить в общий startKoin { modules(...) } рядом с settingsModule.
 *
 * ObserveLatestMeasurementUseCase не используется этим ViewModel напрямую, но
 * экспортирован — им может воспользоваться settings-модуль, если решите брать
 * рост/вес из дневника, а не хранить дублирующееся значение в settings.UserProfile.
 */
val anthroDiaryModule = module {

    single { AnthroDiaryDatabase.getInstance(androidContext()) }
    single { get<AnthroDiaryDatabase>().anthroMeasurementDao() }

    single<AnthroDiaryRepository> { AnthroDiaryRepositoryImpl(dao = get()) }

    factory { ObserveMeasurementsUseCase(get()) }
    factory { ObserveLatestMeasurementUseCase(get()) }
    factory { SaveMeasurementUseCase(get()) }
    factory { GetMeasurementByIdUseCase(get()) }
    factory { DeleteMeasurementUseCase(get()) }

    viewModel {
        AnthroDiaryViewModel(
            observeMeasurements = get(),
            saveMeasurement = get()
        )
    }
}