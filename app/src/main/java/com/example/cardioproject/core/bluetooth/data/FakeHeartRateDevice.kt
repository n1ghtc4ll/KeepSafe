package com.example.cardioproject.core.bluetooth.data

import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import com.example.cardioproject.core.bluetooth.domain.HeartRateDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FakeHeartRateDevice : HeartRateDevice {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var simulationJob: Job? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _heartRate = MutableStateFlow(0)
    override val heartRate: StateFlow<Int> = _heartRate.asStateFlow()

    override fun connect(address: String) {
        if (simulationJob?.isActive == true) return

        simulationJob = scope.launch {
            _connectionState.value = ConnectionState.CONNECTING
            delay(1000)
            _connectionState.value = ConnectionState.CONNECTED
            var currentBpm = 80
            _heartRate.value = currentBpm

            while (isActive) {
                delay(1000)
                currentBpm += listOf(-2, -1, 0, 1, 2, 3).random()

                if (currentBpm > 180) currentBpm = 180
                if (currentBpm < 60) currentBpm = 60

                _heartRate.value = currentBpm
            }
        }
    }

    override fun disconnect() {
        simulationJob?.cancel()
        simulationJob = null

        _connectionState.value = ConnectionState.DISCONNECTED
        _heartRate.value = 0
    }
}