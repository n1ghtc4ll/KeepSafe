package com.example.cardioproject.core.bluetooth.domain

import kotlinx.coroutines.flow.StateFlow

interface HeartRateDevice {
    val connectionState: StateFlow<ConnectionState>
    val heartRate: StateFlow<Int>

    fun connect(address: String)
    fun disconnect()
}