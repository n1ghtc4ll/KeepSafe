package com.example.cardioproject.core.common.domain.repository

import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getTags() : Flow<List<Tag>>
    fun getTabataProfiles(): Flow<List<TabataProfile>>

    suspend fun addTag(tag: Tag)
    suspend fun saveTabataProfile(profile: TabataProfile)
}