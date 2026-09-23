package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BleScanner(
    private val bluetoothAdapter: BluetoothAdapter
) {
    private val filter = ScanFilter.Builder().setServiceUuid(
        ParcelUuid(BleConstants.HEART_RATE_SERVICE_UUID)
    ).build()
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun scanBleDevices(): Flow<ScanResult> = callbackFlow {
        val scanner = bluetoothAdapter.bluetoothLeScanner
        if (scanner == null) {
            cancel(CancellationException("Bluetooth выключен"))
            return@callbackFlow
        }

        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                trySend(result)
            }

            override fun onScanFailed(errorCode: Int) {
                cancel(CancellationException("Ошибка сканирования: $errorCode"))
            }
        }

        scanner.startScan(listOf(filter), scanSettings, callback)

        awaitClose {
            scanner.stopScan(callback)
        }
    }
}
