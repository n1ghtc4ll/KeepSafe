package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import java.util.UUID

class AddWorkoutTagUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(name: String, color: String) {
        val newTag = Tag(
            id = UUID.randomUUID().toString(),
            name = name,
            color = color
        )
        repository.addTag(newTag)
    }
}

class EditWorkoutTagUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(tag: Tag) {
        require(tag.name.isNotBlank()) { "название тега не может быть пустым" }
        repository.updateTag(tag)
    }
}

class RemoveWorkoutTagUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(tagId: String) {
        repository.deleteTag(tagId)
    }
}