package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.repository.SettingsRepository

/**
 * Вызывается BLE-модулем при подключении/отключении устройства или изменении заряда батареи.
 * Сам этот модуль подключением не занимается — только сохраняет последний известный снимок.
 */
class UpdateDeviceInfoUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(device: DeviceInfo) {
        repository.updateDeviceInfo(device)
    }
}