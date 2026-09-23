package com.example.cardioproject.core.bluetooth.data

import android.bluetooth.BluetoothGatt

sealed interface GattConnectionState {
    data object Connecting : GattConnectionState
    data class Connected(val gatt: BluetoothGatt) : GattConnectionState
    data class Disconnected(val status: Int) : GattConnectionState
}