package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.le.ScanResult
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.scan
import kotlin.time.Duration.Companion.milliseconds

class BleScanRepository(
    private val bleScanner: BleScanner
) {
    @OptIn(FlowPreview::class)
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun getDiscoveredDevices(): Flow<List<ScanResult>> {
        return bleScanner.scanBleDevices()
            .scan(emptyMap<String, ScanResult>()) { devicesMap, newResult ->
                devicesMap + (newResult.device.address to newResult)
            }
            .sample(500L.milliseconds)
            .map { devicesMap ->
                devicesMap.values.sortedByDescending { it.rssi }
            }
    }
}