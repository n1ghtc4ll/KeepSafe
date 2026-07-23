package com.example.cardioproject.core.bluetooth

import com.example.cardioproject.core.bluetooth.data.FakeHeartRateDevice
import com.example.cardioproject.core.bluetooth.domain.HeartRateDevice
import org.koin.dsl.module

val bluetoothModule = module {
    single<HeartRateDevice> { FakeHeartRateDevice() }

    // singleOf(::HeartRateDeviceImpl) bind HeartRateDevice::class
}