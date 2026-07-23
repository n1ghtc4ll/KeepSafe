package com.example.cardioproject.settings.data.mapper

import androidx.datastore.preferences.core.Preferences
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.data.local.SettingsPreferencesDataSource
import com.example.cardioproject.settings.data.local.dto.HeartRateZoneDto
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.UserProfile

/*fun WorkoutTagDto.toDomain() = WorkoutTag(id = id, name = name, isEditable = isEditable)
fun WorkoutTag.toDto() = WorkoutTagDto(id = id, name = name, isEditable = isEditable)*/

fun HeartRateZoneDto.toDomain() = HeartRateZone(index = index, label = label, lowerBpm = lowerBpm, upperBpm = upperBpm)
fun HeartRateZone.toDto() = HeartRateZoneDto(index = index, label = label, lowerBpm = lowerBpm, upperBpm = upperBpm)

fun Preferences.toTrainingSettings(
    source: SettingsPreferencesDataSource,
    tagsList: List<Tag>
): TrainingSettings = TrainingSettings(
    device = DeviceInfo(
        name = source.deviceName(this),
        batteryLevel = source.deviceBattery(this),
        isConnected = source.deviceConnected(this)
    ),
    phaseSoundEnabled = source.phaseSoundEnabled(this),
    signalVolume = source.signalVolume(this),
    tags = tagsList,
    profile = UserProfile(
        birthDate = source.birthDate(this),
        gender = source.gender(this),
        heightCm = source.heightCm(this)
    ),
    heartRateZones = source.zones(this).map { it.toDomain() },
    criticalPulseAlertEnabled = source.criticalPulseAlertEnabled(this),
    keepScreenOnEnabled = source.keepScreenOnEnabled(this)
)