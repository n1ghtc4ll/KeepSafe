package com.example.cardioproject.settings.domain.model

import com.example.cardioproject.core.common.domain.model.Tag
import kotlin.String

/**
 * Последнее известное состояние подключённого устройства.
 * Логика сканирования/подключения (BLE) живёт в отдельном модуле —
 * этот модуль только хранит и отдаёт последний известный снимок состояния.
 */
data class DeviceInfo(
    val name: String,
    val macAddress: String = "",
    val batteryLevel: Int,
    val isConnected: Boolean
) {
    companion object {
        val Unknown = DeviceInfo(name = "", macAddress = "", batteryLevel = 0, isConnected = false)
    }
}

/**
 * Одна зона ЧСС (например "Зона 1": 60-79 уд/мин).
 */
data class HeartRateZone(
    val index: Int,
    val label: String,
    val lowerBpm: Int,
    val upperBpm: Int
) {
    init {
        require(lowerBpm in MIN_BPM..MAX_BPM) { "lowerBpm должен быть в диапазоне $MIN_BPM..$MAX_BPM" }
        require(upperBpm in MIN_BPM..MAX_BPM) { "upperBpm должен быть в диапазоне $MIN_BPM..$MAX_BPM" }
        require(lowerBpm <= upperBpm) { "lowerBpm не может быть больше upperBpm" }
    }

    companion object {
        const val MIN_BPM = 40
        const val MAX_BPM = 200
    }
}

data class UserProfile(
    val birthDate: String = "",
    val gender: String = "",
    val heightCm: Int? = null
)

/**
 * Полный агрегат настроек тренировки — то, что в презентационном слое
 * маппится в SettingsUiState для отображения на экране.
 */
data class TrainingSettings(
    val device: DeviceInfo = DeviceInfo.Unknown,
    val phaseSoundEnabled: Boolean = true,
    val signalVolume: Float = 0.6f,
    val tags: List<Tag> = emptyList(),
    val profile: UserProfile = UserProfile(),
    val heartRateZones: List<HeartRateZone> = emptyList(),
    val criticalPulseAlertEnabled: Boolean = true,
    val keepScreenOnEnabled: Boolean = true
)