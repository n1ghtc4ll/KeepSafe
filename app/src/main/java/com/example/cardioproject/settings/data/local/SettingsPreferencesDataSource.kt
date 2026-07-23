package com.example.cardioproject.settings.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cardioproject.settings.data.local.dto.HeartRateZoneDto
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.trainingSettingsDataStore by preferencesDataStore(name = "training_settings")

/**
 * Единственная точка доступа к DataStore для модуля настроек тренировки.
 * Preferences DataStore хранит только примитивы, поэтому списки (теги, зоны ЧСС)
 * сериализуются в JSON и хранятся под отдельными строковыми ключами.
 */
class SettingsPreferencesDataSource(
    private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    private object Keys {
        val DEVICE_NAME = stringPreferencesKey("device_name")
        val DEVICE_MAC = stringPreferencesKey("device_mac")
        val DEVICE_BATTERY = intPreferencesKey("device_battery")
        val DEVICE_CONNECTED = booleanPreferencesKey("device_connected")

        val PHASE_SOUND_ENABLED = booleanPreferencesKey("phase_sound_enabled")
        val SIGNAL_VOLUME = floatPreferencesKey("signal_volume")

        //val TAGS_JSON = stringPreferencesKey("tags_json")

        val BIRTH_DATE = stringPreferencesKey("birth_date")
        val GENDER = stringPreferencesKey("gender")
        val HEIGHT_CM = intPreferencesKey("height_cm")

        val ZONES_JSON = stringPreferencesKey("zones_json")

        val CRITICAL_PULSE_ALERT_ENABLED = booleanPreferencesKey("critical_pulse_alert_enabled")
        val KEEP_SCREEN_ON_ENABLED = booleanPreferencesKey("keep_screen_on_enabled")
    }

    val preferencesFlow: Flow<Preferences> = context.trainingSettingsDataStore.data

    suspend fun updateDeviceInfo(name: String, macAddress: String, batteryLevel: Int, isConnected: Boolean) {
        context.trainingSettingsDataStore.edit { prefs ->
            prefs[Keys.DEVICE_NAME] = name
            prefs[Keys.DEVICE_MAC] = macAddress
            prefs[Keys.DEVICE_BATTERY] = batteryLevel
            prefs[Keys.DEVICE_CONNECTED] = isConnected
        }
    }

    suspend fun updatePhaseSoundEnabled(enabled: Boolean) {
        context.trainingSettingsDataStore.edit { prefs -> prefs[Keys.PHASE_SOUND_ENABLED] = enabled }
    }

    suspend fun updateSignalVolume(volume: Float) {
        context.trainingSettingsDataStore.edit { prefs -> prefs[Keys.SIGNAL_VOLUME] = volume }
    }

/*    suspend fun updateTags(tags: List<WorkoutTagDto>) {
        context.trainingSettingsDataStore.edit { prefs -> prefs[Keys.TAGS_JSON] = json.encodeToString(tags) }
    }*/

    suspend fun updateProfile(birthDate: String, gender: String, heightCm: Int?) {
        context.trainingSettingsDataStore.edit { prefs ->
            prefs[Keys.BIRTH_DATE] = birthDate
            prefs[Keys.GENDER] = gender
            if (heightCm != null) prefs[Keys.HEIGHT_CM] = heightCm else prefs.remove(Keys.HEIGHT_CM)
        }
    }

    suspend fun updateZones(zones: List<HeartRateZoneDto>) {
        context.trainingSettingsDataStore.edit { prefs -> prefs[Keys.ZONES_JSON] = json.encodeToString(zones) }
    }

    suspend fun updateCriticalPulseAlertEnabled(enabled: Boolean) {
        context.trainingSettingsDataStore.edit { prefs -> prefs[Keys.CRITICAL_PULSE_ALERT_ENABLED] = enabled }
    }

    suspend fun updateKeepScreenOnEnabled(enabled: Boolean) {
        context.trainingSettingsDataStore.edit { prefs -> prefs[Keys.KEEP_SCREEN_ON_ENABLED] = enabled }
    }

    fun deviceName(prefs: Preferences) = prefs[Keys.DEVICE_NAME] ?: ""
    fun deviceMac(prefs: Preferences): String = prefs[Keys.DEVICE_MAC] ?: ""
    fun deviceBattery(prefs: Preferences) = prefs[Keys.DEVICE_BATTERY] ?: 0
    fun deviceConnected(prefs: Preferences) = prefs[Keys.DEVICE_CONNECTED] ?: false

    fun phaseSoundEnabled(prefs: Preferences) = prefs[Keys.PHASE_SOUND_ENABLED] ?: true
    fun signalVolume(prefs: Preferences) = prefs[Keys.SIGNAL_VOLUME] ?: 0.6f

/*    fun tags(prefs: Preferences): List<WorkoutTagDto> =
        prefs[Keys.TAGS_JSON]
            ?.let { raw -> runCatching { json.decodeFromString<List<WorkoutTagDto>>(raw) }.getOrNull() }
            ?: DEFAULT_TAGS*/

    fun birthDate(prefs: Preferences) = prefs[Keys.BIRTH_DATE] ?: ""
    fun gender(prefs: Preferences) = prefs[Keys.GENDER] ?: ""
    fun heightCm(prefs: Preferences) = prefs[Keys.HEIGHT_CM]

    fun zones(prefs: Preferences): List<HeartRateZoneDto> =
        prefs[Keys.ZONES_JSON]
            ?.let { raw -> runCatching { json.decodeFromString<List<HeartRateZoneDto>>(raw) }.getOrNull() }
            ?: DEFAULT_ZONES

    fun criticalPulseAlertEnabled(prefs: Preferences) = prefs[Keys.CRITICAL_PULSE_ALERT_ENABLED] ?: true
    fun keepScreenOnEnabled(prefs: Preferences) = prefs[Keys.KEEP_SCREEN_ON_ENABLED] ?: true

    private companion object {
/*        val DEFAULT_TAGS = listOf(
            WorkoutTagDto(id = "default_general", name = "Общая"),
            WorkoutTagDto(id = "default_strength", name = "Силовая"),
            WorkoutTagDto(id = "default_light", name = "Легкая"),
            WorkoutTagDto(id = "default_bike", name = "Вело")
        )*/

        val DEFAULT_ZONES = listOf(
            HeartRateZoneDto(index = 0, label = "Зона 1", lowerBpm = 100, upperBpm = 119),
            HeartRateZoneDto(index = 1, label = "Зона 2", lowerBpm = 120, upperBpm = 139),
            HeartRateZoneDto(index = 2, label = "Зона 3", lowerBpm = 140, upperBpm = 159),
            HeartRateZoneDto(index = 3, label = "Зона 4", lowerBpm = 160, upperBpm = 200)
        )
    }
}