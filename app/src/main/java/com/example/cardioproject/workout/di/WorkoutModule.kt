package com.example.cardioproject.workout.di

import com.example.cardioproject.workout.data.repository.WorkoutSessionRepositoryImpl
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import com.example.cardioproject.workout.domain.usecase.GetWorkoutSetupDataUseCase
import com.example.cardioproject.workout.domain.usecase.StartWorkoutTimerUseCase
import com.example.cardioproject.workout.presentation.viewmodel.ActiveWorkoutViewModel
import com.example.cardioproject.workout.presentation.viewmodel.MainScreenViewModel
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutDetailsViewModel
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutHistoryViewModel
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val workoutModule = module {
    single<WorkoutSessionRepository> {
        WorkoutSessionRepositoryImpl(workoutDao = get(), tagDao = get()) //[cite: 27]
    }

    factory { GetWorkoutSetupDataUseCase(settingsRepository = get()) }
    factory { StartWorkoutTimerUseCase() }

    viewModel { WorkoutSettingsViewModel(getWorkoutSetupDataUseCase = get()) }
    viewModel {
        ActiveWorkoutViewModel(
            startTimerUseCase = get(),
            workoutRepository = get(),
            settingsRepository = get(),
            bleConnectionManager = get()
        )
    }

    viewModel { WorkoutHistoryViewModel(repository = get()) }
    viewModel { MainScreenViewModel(get(), get()) }

    viewModel { parameters ->
        WorkoutDetailsViewModel(
            workoutId = parameters.get(),
            repository = get()
        )
    }
}