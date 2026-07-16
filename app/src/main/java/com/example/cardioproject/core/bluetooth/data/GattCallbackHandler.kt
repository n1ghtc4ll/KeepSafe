package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import androidx.annotation.RequiresPermission
import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GattCallbackHandler(
    private val scope: CoroutineScope,
    private val operationQueue: BleOperationQueue,
    val connectionState: MutableStateFlow<ConnectionState>,
    val heartRate: MutableStateFlow<Int>
): BluetoothGattCallback() {

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS)
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    connectionState.value = ConnectionState.CONNECTED
                    scope.launch {
                        operationQueue.enqueueOperation { gatt.discoverServices() }
                    }
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectionState.value = ConnectionState.DISCONNECTED
                    gatt.close()
                }
            }
        else {
            connectionState.value = ConnectionState.ERROR
            gatt.close()
        }

        operationQueue.signalOperationCompleted()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        operationQueue.signalOperationCompleted()

        if (status == BluetoothGatt.GATT_SUCCESS) {
            val service = gatt.getService(BleConstants.HEART_RATE_SERVICE_UUID)
            val characteristic = service?.getCharacteristic(BleConstants.HEART_RATE_CHARACTERISTIC_UUID)

            if (characteristic != null) {
                scope.launch {
                    operationQueue.enqueueOperation {
                        gatt.setCharacteristicNotification(characteristic, true)
                        operationQueue.signalOperationCompleted()
                    }

                    operationQueue.enqueueOperation {
                        val descriptor = characteristic.getDescriptor(
                            BleConstants.CLIENT_CHARACTERISTIC_CONFIG_UUID
                        )
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)
                    }
                }
            }
        }
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        operationQueue.signalOperationCompleted()
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        if (characteristic.uuid == BleConstants.HEART_RATE_CHARACTERISTIC_UUID) {
            val hrValue = HeartRateParser.parseHeartRate(characteristic.value)
            heartRate.value = hrValue
        }
    }
}