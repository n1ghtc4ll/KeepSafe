package com.example.cardioproject.workout.di

import com.example.cardioproject.workout.data.repository.WorkoutSessionRepositoryImpl
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import com.example.cardioproject.workout.domain.usecase.GetWorkoutSetupDataUseCase
import com.example.cardioproject.workout.domain.usecase.StartWorkoutTimerUseCase
import org.koin.dsl.module

val workoutModule = module {
    single<WorkoutSessionRepository> {
        WorkoutSessionRepositoryImpl(get(), get())
    }

    factory { GetWorkoutSetupDataUseCase(get()) }
    factory { StartWorkoutTimerUseCase() }
}