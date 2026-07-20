package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<TrainingSettings> = repository.observeSettings()
}