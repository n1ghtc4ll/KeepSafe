package com.example.cardioproject.settings.domain.repository

import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.UserProfile
import com.example.cardioproject.settings.domain.model.WorkoutTag
import kotlinx.coroutines.flow.Flow

/**
 * Контракт хранения настроек тренировки. Реализация — в data-слое (SettingsRepositoryImpl).
 *
 * Важно: этот репозиторий НЕ управляет Bluetooth-подключением — это зона ответственности
 * отдельного BLE-модуля. Сюда только приходит [updateDeviceInfo] с последним известным
 * состоянием устройства (имя/заряд/статус подключения).
 */
interface SettingsRepository {

    fun observeSettings(): Flow<TrainingSettings>

    suspend fun updateDeviceInfo(device: DeviceInfo)

    suspend fun updatePhaseSoundEnabled(enabled: Boolean)

    suspend fun updateSignalVolume(volume: Float)

    suspend fun addTag(tag: WorkoutTag)

    suspend fun updateTag(tag: WorkoutTag)

    suspend fun removeTag(tagId: String)

    suspend fun updateProfile(profile: UserProfile)

    suspend fun updateHeartRateZones(zones: List<HeartRateZone>)

    suspend fun updateCriticalPulseAlertEnabled(enabled: Boolean)

    suspend fun updateKeepScreenOnEnabled(enabled: Boolean)
}