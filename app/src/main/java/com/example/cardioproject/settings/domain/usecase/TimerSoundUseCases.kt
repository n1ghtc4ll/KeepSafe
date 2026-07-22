package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.repository.SettingsRepository

class UpdatePhaseSoundUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.updatePhaseSoundEnabled(enabled)
    }
}

class UpdateSignalVolumeUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(volume: Float) {
        require(volume in 0f..1f) { "volume должен быть в диапазоне 0f..1f" }
        repository.updateSignalVolume(volume)
    }
}