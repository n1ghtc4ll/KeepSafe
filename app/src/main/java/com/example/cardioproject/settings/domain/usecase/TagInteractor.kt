package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import java.util.UUID

class TagInteractor(
    private val settingsRepository: SettingsRepository
) {
    suspend fun addWorkoutTag(name: String, color: String) {
        val newTag = Tag(
            id = UUID.randomUUID().toString(),
            name = name,
            color = color
        )
        settingsRepository.addTag(newTag)
    }

    suspend fun updateWorkoutTag(tag: Tag) {
        require(tag.name.isNotBlank()) { "название тега не может быть пустым" }
        settingsRepository.updateTag(tag)
    }

    suspend fun deleteWorkoutTag(tagId: String) {
        settingsRepository.deleteTag(tagId)
    }
}