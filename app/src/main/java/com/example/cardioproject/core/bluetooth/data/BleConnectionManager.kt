package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class BleConnectionManager(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter
) {
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState = _connectionState.asStateFlow()

    private var bluetoothGatt: BluetoothGatt? = null

    private val _heartRateBpm = MutableSharedFlow<Int>(extraBufferCapacity = 64)
    val heartRateBpm: SharedFlow<Int> = _heartRateBpm.asSharedFlow()

    private val _batteryLevel = MutableStateFlow<Int?>(null)
    val batteryLevel = _batteryLevel.asStateFlow()

    private val gattCallback = object : BluetoothGattCallback() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.d("BLE_DEBUG", "onConnectionStateChange: status=$status, newState=$newState")

            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e("BLE_DEBUG", "GATT error: status=$status")
                closeConnection()
                _connectionState.value = ConnectionState.Error(status, "error occurred with status: $status")
                return
            }

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("BLE_DEBUG", "ConnectionState = Connected")
                    _connectionState.value = ConnectionState.Connected
                    gatt?.discoverServices()
                }

                BluetoothProfile.STATE_CONNECTING -> {
                    Log.d("BLE_DEBUG", "ConnectionState = Connecting")
                    _connectionState.value = ConnectionState.Connecting
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("BLE_DEBUG", "ConnectionState = Disconnected")
                    _connectionState.value = ConnectionState.Disconnected
                    closeConnection()
                }

                BluetoothProfile.STATE_DISCONNECTING -> {
                    Log.d("BLE_DEBUG", "ConnectionState = Disconnecting")
                }
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                return
            }

            val hrService = gatt?.getService(BleConstants.HEART_RATE_SERVICE_UUID) ?: return

            val hrCharacteristic = hrService.getCharacteristic(
                BleConstants.HEART_RATE_CHARACTERISTIC_UUID
            ) ?: return

            enableHeartRateNotifications(gatt, hrCharacteristic)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            if (characteristic.uuid == BleConstants.HEART_RATE_CHARACTERISTIC_UUID) {
                val measurement = HeartRateParser.parse(value) ?: return

                _heartRateBpm.tryEmit(measurement.heartRateBpm)
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE_DEBUG", "Подписка на пульс оформлена. Запрашиваем батарею.")
                val batteryService = gatt?.getService(BleConstants.BATTERY_SERVICE_UUID)
                val batteryChar = batteryService?.getCharacteristic(BleConstants.BATTERY_CHARACTERISTIC_UUID)

                if (batteryChar != null) {
                    try {
                        gatt.readCharacteristic(batteryChar)
                    } catch (e: SecurityException) {
                        Log.e("BLE_DEBUG", "Нет прав для чтения батареи", e)
                    }
                } else {
                    Log.d("BLE_DEBUG", "Датчик не поддерживает передачу уровня заряда")
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            handleBatteryRead(characteristic, value, status)
        }

        private fun handleBatteryRead(characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS && characteristic.uuid == BleConstants.BATTERY_CHARACTERISTIC_UUID) {
                if (value.isNotEmpty()) {
                    val battery = value[0].toInt() and 0xFF
                    Log.d("BLE_DEBUG", "Заряд батареи успешно прочитан: $battery%")
                    _batteryLevel.value = battery
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun enableHeartRateNotifications(
        gatt: BluetoothGatt,
        characteristics: BluetoothGattCharacteristic
    ) {
        gatt.setCharacteristicNotification(characteristics, true)

        val descriptor = characteristics.getDescriptor(
            BleConstants.CLIENT_CHARACTERISTIC_CONFIG_UUID
        ) ?: return

        val payload = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            gatt.writeDescriptor(descriptor, payload)
        else {
            @Suppress("DEPRECATION")
            descriptor.value = payload
            @Suppress("DEPRECATION")
            gatt.writeDescriptor(descriptor)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(macAddress: String) {
        if (!bluetoothAdapter.isEnabled) {
            _connectionState.value = ConnectionState.Error(-1, "Bluetooth выключен")
            return
        }

        val device: BluetoothDevice = try {
            bluetoothAdapter.getRemoteDevice(macAddress)
        } catch (e: IllegalArgumentException) {
            _connectionState.value = ConnectionState.Error(-1, "Невалидный MAC-адрес: $macAddress")
            return
        }

        bluetoothGatt?.close()

        _connectionState.value = ConnectionState.Connecting

        @Suppress("DEPRECATION")
        bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        Log.d("BLE_DEBUG", "Пользователь нажал Отключить. Инициируем разрыв.")
        bluetoothGatt?.disconnect()
        closeConnection()
        _connectionState.value = ConnectionState.Disconnected
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun closeConnection() {
        Log.d("BLE_DEBUG", "Очистка ресурсов (close). GATT-клиент уничтожается.")
        runCatching { bluetoothGatt?.close() }
        bluetoothGatt = null
    }
}