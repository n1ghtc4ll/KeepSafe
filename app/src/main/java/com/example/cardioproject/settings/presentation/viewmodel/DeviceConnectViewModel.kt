package com.example.cardioproject.settings.presentation.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.bluetooth.data.BleConnectionManager
import com.example.cardioproject.core.bluetooth.data.BleScanRepository
import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class DeviceConnectViewModel(
    private val settingsRepository: SettingsRepository,
    private val bleScanRepository: BleScanRepository,
    private val bleConnectionManager: BleConnectionManager
) : ViewModel() {
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<DeviceInfo>>(emptyList())
    val discoveredDevices = _discoveredDevices.asStateFlow()

    private var scanJob: Job? = null

    init {
        observeConnection()
    }

    private fun observeConnection() {
        bleConnectionManager.connectionState
            .onEach { state ->
                when (state) {
                    is ConnectionState.Connected -> {
                        updateConnectionFlag(true)
                    }
                    is ConnectionState.Disconnected, is ConnectionState.Error -> {
                        updateConnectionFlag(false)
                    }
                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateConnectionFlag(isConnected: Boolean) {
        viewModelScope.launch {
            val currentDevice = settingsRepository.observeSettings().first().device
            if (currentDevice.macAddress.isNotBlank() && currentDevice.isConnected != isConnected) {
                settingsRepository.updateDeviceInfo(currentDevice.copy(isConnected = isConnected))
            }
        }
    }

    @RequiresPermission(allOf = [
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    ])
    fun startScan() {
        scanJob?.cancel()
        _isScanning.value = true

        scanJob = bleScanRepository
            .getDiscoveredDevices()
            .onEach { results ->
                _discoveredDevices.value = results.map { result ->
                    DeviceInfo(
                        name = result.device.name,
                        macAddress = result.device.address,
                        batteryLevel = 0,
                        isConnected = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        _isScanning.value = false
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun selectAndConnectDevice(device: DeviceInfo) {
        //stopScan()
        viewModelScope.launch {
            scanJob?.cancelAndJoin()
            scanJob = null
            _isScanning.value = false

            settingsRepository.updateDeviceInfo(device)

            delay(150.milliseconds)
            bleConnectionManager.connect(device.macAddress)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnectDevice() {
        bleConnectionManager.disconnect()
    }
}