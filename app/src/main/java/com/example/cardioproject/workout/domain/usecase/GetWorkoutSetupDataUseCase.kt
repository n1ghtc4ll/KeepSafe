package com.example.cardioproject.workout.domain.usecase

import com.example.cardioproject.core.common.domain.repository.SettingsRepository
import com.example.cardioproject.workout.domain.model.WorkoutSetupData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetWorkoutSetupDataUseCase(
    private val commonRepository: SettingsRepository
) {
    operator fun invoke(): Flow<WorkoutSetupData> {
        return combine(
            commonRepository.getTags(),
            commonRepository.getTabataProfiles()
        ) {
            tags, profiles ->
            WorkoutSetupData(tags, profiles)
        }
    }
}