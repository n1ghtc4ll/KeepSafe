package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.repository.SettingsRepository

class UpdateCriticalPulseAlertUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.updateCriticalPulseAlertEnabled(enabled)
    }
}

class UpdateKeepScreenOnUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.updateKeepScreenOnEnabled(enabled)
    }
}