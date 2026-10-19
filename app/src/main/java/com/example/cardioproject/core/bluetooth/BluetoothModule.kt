package com.example.cardioproject.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.cardioproject.core.bluetooth.data.BleConnectionManager
import com.example.cardioproject.core.bluetooth.data.BleScanRepository
import com.example.cardioproject.core.bluetooth.data.BleScanner
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val bluetoothModule = module {
    single<BluetoothManager> {
        androidContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    single<BluetoothAdapter> {
        get<BluetoothManager>().adapter
    }

    single { BleScanner(get()) }
    single { BleScanRepository(get()) }

    single { BleConnectionManager(context = androidContext(), bluetoothAdapter = get()) }
}