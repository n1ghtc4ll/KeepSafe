package com.example.cardioproject.settings.domain.repository

import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Контракт хранения настроек тренировки. Реализация — в data-слое (SettingsRepositoryImpl).
 *
 * Важно: этот репозиторий НЕ управляет Bluetooth-подключением — это зона ответственности
 * отдельного BLE-модуля. Сюда только приходит [selectDevice] с последним известным
 * состоянием устройства (имя/заряд/статус подключения).
 */
interface SettingsRepository {
    fun observeSettings(): Flow<TrainingSettings>

    fun getDevices(): Flow<List<DeviceInfo>>
    fun getSelectedDevice(): Flow<DeviceInfo?>
    suspend fun insertDevice(device: DeviceInfo)
    suspend fun selectDevice(macAddress: String)
    suspend fun deleteDevice(macAddress: String)

    suspend fun updatePhaseSoundEnabled(enabled: Boolean)
    suspend fun updateSignalVolume(volume: Float)
    suspend fun updateProfile(profile: UserProfile)
    suspend fun updateHeartRateZones(zones: List<HeartRateZone>)
    suspend fun updateCriticalPulseAlertEnabled(enabled: Boolean)
    suspend fun updateKeepScreenOnEnabled(enabled: Boolean)

    fun getTags(): Flow<List<Tag>>
    suspend fun addTag(tag: Tag)
    suspend fun updateTag(tag: Tag)
    suspend fun deleteTag(id: String)

    fun getTabataProfiles(): Flow<List<TabataProfile>>
    suspend fun getTabataProfileById(id: String): TabataProfile?
    suspend fun saveTabataProfile(profile: TabataProfile)
    suspend fun deleteTabataProfile(id: String)
}