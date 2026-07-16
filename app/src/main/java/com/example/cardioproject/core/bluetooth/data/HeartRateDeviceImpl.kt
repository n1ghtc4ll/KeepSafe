package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import com.example.cardioproject.core.bluetooth.domain.HeartRateDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeartRateDeviceImpl(context: Context): HeartRateDevice {
    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    private val _heartRate = MutableStateFlow(0)

    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    override val heartRate: StateFlow<Int> = _heartRate.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val operationQueue = BleOperationQueue()

    private val gattCallback = GattCallbackHandler(
        scope = scope,
        operationQueue = operationQueue,
        connectionState = _connectionState,
        heartRate = _heartRate
    )

    private val connectionManager = BleConnectionManager(
        context = context,
        bluetoothAdapter = bluetoothAdapter,
        gattCallback = gattCallback,
        connectionState = _connectionState
    )

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun connect(address: String) {
        connectionManager.connect(address)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun disconnect() {
        connectionManager.disconnectAndClose()
        operationQueue.clear()
        _heartRate.value = 0
    }
}