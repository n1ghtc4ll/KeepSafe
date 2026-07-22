package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import kotlinx.coroutines.flow.MutableStateFlow

class BleConnectionManager(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter?,
    private val gattCallback: GattCallbackHandler,
    private val connectionState: MutableStateFlow<ConnectionState>
) {
    private var bluetoothGatt: BluetoothGatt? = null

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(macAddress: String) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            connectionState.value = ConnectionState.ERROR
            return
        }

        val device = bluetoothAdapter.getRemoteDevice(macAddress)
        connectionState.value = ConnectionState.CONNECTING

        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnectAndClose() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        connectionState.value = ConnectionState.DISCONNECTED
    }
}