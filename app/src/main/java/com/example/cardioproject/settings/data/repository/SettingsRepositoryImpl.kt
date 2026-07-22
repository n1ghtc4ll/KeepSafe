package com.example.cardioproject.settings.data.repository

import com.example.cardioproject.settings.data.local.SettingsPreferencesDataSource
import com.example.cardioproject.settings.data.mapper.toDto
import com.example.cardioproject.settings.data.mapper.toTrainingSettings
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.UserProfile
import com.example.cardioproject.settings.domain.model.WorkoutTag
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataSource: SettingsPreferencesDataSource
) : SettingsRepository {

    override fun observeSettings(): Flow<TrainingSettings> =
        dataSource.preferencesFlow.map { prefs -> prefs.toTrainingSettings(dataSource) }

    override suspend fun updateDeviceInfo(device: DeviceInfo) {
        dataSource.updateDeviceInfo(device.name, device.batteryLevel, device.isConnected)
    }

    override suspend fun updatePhaseSoundEnabled(enabled: Boolean) {
        dataSource.updatePhaseSoundEnabled(enabled)
    }

    override suspend fun updateSignalVolume(volume: Float) {
        dataSource.updateSignalVolume(volume)
    }

    override suspend fun addTag(tag: WorkoutTag) {
        val current = dataSource.tags(dataSource.preferencesFlow.first())
        dataSource.updateTags(current + tag.toDto())
    }

    override suspend fun updateTag(tag: WorkoutTag) {
        val current = dataSource.tags(dataSource.preferencesFlow.first())
        val updated = current.map { if (it.id == tag.id) tag.toDto() else it }
        dataSource.updateTags(updated)
    }

    override suspend fun removeTag(tagId: String) {
        val current = dataSource.tags(dataSource.preferencesFlow.first())
        dataSource.updateTags(current.filterNot { it.id == tagId })
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
}