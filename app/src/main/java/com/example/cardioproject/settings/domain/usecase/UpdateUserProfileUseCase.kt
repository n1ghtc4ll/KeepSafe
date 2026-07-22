package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.model.UserProfile
import com.example.cardioproject.settings.domain.repository.SettingsRepository

class UpdateUserProfileUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.updateProfile(profile)
    }
}