package com.example.cardioproject.core.common.data.repository

import com.example.cardioproject.core.common.data.dao.TabataProfileDao
import com.example.cardioproject.core.common.data.dao.TagDao
import com.example.cardioproject.core.common.data.mapper.toDomain
import com.example.cardioproject.core.common.data.mapper.toEntity
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.core.common.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val tabataProfileDao: TabataProfileDao,
    private val tagDao: TagDao
): SettingsRepository {
    override fun getTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { tagEntityList ->
            tagEntityList.map {
                tagEntity -> tagEntity.toDomain()
            }
        }
    }

    override fun getTabataProfiles(): Flow<List<TabataProfile>> {
        return tabataProfileDao.getAllTabataProfiles().map { tabataProfileEntityList ->
            tabataProfileEntityList.map {
                tabataProfileEntity -> tabataProfileEntity.toDomain()
            }
        }
    }

    override suspend fun addTag(tag: Tag) {
        tagDao.insertTag(tag.toEntity())
    }

    override suspend fun saveTabataProfile(profile: TabataProfile) {
        tabataProfileDao.insertTabataProfile(profile.toEntity())
    }
}