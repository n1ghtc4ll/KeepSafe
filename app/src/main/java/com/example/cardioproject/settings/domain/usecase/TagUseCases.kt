package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.model.WorkoutTag
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import java.util.UUID

class AddWorkoutTagUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(name: String) {
        val trimmed = name.trim()
        require(trimmed.isNotEmpty()) { "название тега не может быть пустым" }
        repository.addTag(WorkoutTag(id = UUID.randomUUID().toString(), name = trimmed))
    }
}

class EditWorkoutTagUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(tag: WorkoutTag) {
        require(tag.name.isNotBlank()) { "название тега не может быть пустым" }
        repository.updateTag(tag)
    }
}

class RemoveWorkoutTagUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(tagId: String) {
        repository.removeTag(tagId)
    }
}