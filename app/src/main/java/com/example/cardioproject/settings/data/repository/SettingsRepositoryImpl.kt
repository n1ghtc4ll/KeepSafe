package com.example.cardioproject.settings.data.repository

import android.net.MacAddress
import com.example.cardioproject.core.common.data.dao.TabataProfileDao
import com.example.cardioproject.core.common.data.dao.TagDao
import com.example.cardioproject.core.common.data.mapper.toDomain
import com.example.cardioproject.core.common.data.mapper.toEntity
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.data.local.SettingsPreferencesDataSource
import com.example.cardioproject.settings.data.local.dao.SavedDevicesDao
import com.example.cardioproject.settings.data.mapper.toDomain
import com.example.cardioproject.settings.data.mapper.toDto
import com.example.cardioproject.settings.data.mapper.toEntity
import com.example.cardioproject.settings.data.mapper.toTrainingSettings
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.UserProfile
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataSource: SettingsPreferencesDataSource,
    private val tagDao: TagDao,
    private val tabataProfileDao: TabataProfileDao,
    private val savedDevicesDao: SavedDevicesDao
) : SettingsRepository {

    override fun observeSettings(): Flow<TrainingSettings> {
        return combine(
            dataSource.preferencesFlow,
            tagDao.getAllTags()
        ) { prefs, tagEntities ->

            val domainTags = tagEntities.map {
                it.toDomain()
            }

            prefs.toTrainingSettings(dataSource, domainTags)
        }
    }

    override fun getDevices(): Flow<List<DeviceInfo>> {
        return savedDevicesDao.getDevices().map {
            it.map {
                entity -> entity.toDomain()
            }
        }
    }

    override fun getSelectedDevice(): Flow<DeviceInfo?> {
        return savedDevicesDao.getSelectedDevice().map {
            it?.toDomain()
        }
    }

    override suspend fun insertDevice(device: DeviceInfo) {
        savedDevicesDao.insertDevice(device.toEntity())
    }

    override suspend fun deleteDevice(macAddress: String) {
        savedDevicesDao.deleteDevice(macAddress)
    }

    override suspend fun selectDevice(macAddress: String) {
        savedDevicesDao.selectDevice(macAddress)
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
        tabataProfileDao.deleteTabataProfileById(id)
    }
}