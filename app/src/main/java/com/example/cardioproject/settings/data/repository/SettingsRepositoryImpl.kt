package com.example.cardioproject.settings.data.repository

import androidx.compose.ui.graphics.Color
import com.example.cardioproject.core.common.data.dao.TabataProfileDao
import com.example.cardioproject.core.common.data.dao.TagDao
import com.example.cardioproject.core.common.data.entity.TagEntity
import com.example.cardioproject.core.common.data.mapper.toDomain
import com.example.cardioproject.core.common.data.mapper.toEntity
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.data.local.SettingsPreferencesDataSource
import com.example.cardioproject.settings.data.mapper.toDto
import com.example.cardioproject.settings.data.mapper.toTrainingSettings
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.UserProfile
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataSource: SettingsPreferencesDataSource,
    private val tagDao: TagDao,
    private val tabataProfileDao: TabataProfileDao
) : SettingsRepository {

    override fun observeSettings(): Flow<TrainingSettings> {
        return combine(
            dataSource.preferencesFlow,
            tagDao.getAllTags()// Замени на свой метод из TagDao
        ) { prefs, tagEntities ->

            // Маппим Entity из БД в доменную модель настроек
            val domainTags = tagEntities.map {
                it.toDomain()
            }

            prefs.toTrainingSettings(dataSource, domainTags)
        }

        //dataSource.preferencesFlow.map { prefs -> prefs.toTrainingSettings(dataSource) }
    }

    override suspend fun updateDeviceInfo(device: DeviceInfo) {
        dataSource.updateDeviceInfo(device.name, device.macAddress, device.batteryLevel, device.isConnected)
    }

    override suspend fun updatePhaseSoundEnabled(enabled: Boolean) {
        dataSource.updatePhaseSoundEnabled(enabled)
    }

    override suspend fun updateSignalVolume(volume: Float) {
        dataSource.updateSignalVolume(volume)
    }

    override suspend fun updateProfile(profile: UserProfile) {
        dataSource.updateProfile(profile.birthDate, profile.gender, profile.heightCm)
    }

    override suspend fun updateHeartRateZones(zones: List<HeartRateZone>) {
        dataSource.updateZones(zones.map { it.toDto() })
    }

    override suspend fun updateCriticalPulseAlertEnabled(enabled: Boolean) {
        dataSource.updateCriticalPulseAlertEnabled(enabled)
    }

    override suspend fun updateKeepScreenOnEnabled(enabled: Boolean) {
        dataSource.updateKeepScreenOnEnabled(enabled)
    }

    override fun getTags(): Flow<List<Tag>> =
        tagDao.getAllTags().map { list -> list.map { it.toDomain() } }

    override suspend fun addTag(tag: Tag) {
        tagDao.insertTag(tag.toEntity())
    }

    override suspend fun updateTag(tag: Tag) {
        tagDao.updateTag(tag.toEntity())
    }

    override suspend fun deleteTag(id: String) {
        tagDao.deleteTagById(id)
    }

    override fun getTabataProfiles(): Flow<List<TabataProfile>> =
        tabataProfileDao.getAllTabataProfiles().map { list -> list.map { it.toDomain() } }

    override suspend fun getTabataProfileById(id: String): TabataProfile? {
        return tabataProfileDao.getTabataProfileById(id)?.toDomain()
    }

    override suspend fun saveTabataProfile(profile: TabataProfile) {
        tabataProfileDao.insertTabataProfile(profile.toEntity())
    }

    override suspend fun deleteTabataProfile(id: String) {
        // Убедись, что добавил метод deleteTabataProfileById(id: String) в свой TabataProfileDao!
        tabataProfileDao.deleteTabataProfileById(id)
    }
}