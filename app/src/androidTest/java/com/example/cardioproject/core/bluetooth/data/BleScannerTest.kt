package com.example.cardioproject.core.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assume.assumeNotNull
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
class BleScannerAndroidTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        GrantPermissionRule.grant(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bleScanner: BleScanner

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter

        // Проверяем железо: если Bluetooth нет или он выключен в шторке, тест пропускается
        assumeNotNull(adapter)
        assumeTrue("Bluetooth выключен на устройстве", adapter.isEnabled)

        bluetoothAdapter = adapter
        bleScanner = BleScanner(bluetoothAdapter)
    }

    @Test
    fun bleScanner_startsAndStopsCleanly_onRealHardware() = runTest {
        // Проверяем жизненный цикл: радиомодуль успешно запускает скан
        // и без ошибок освобождает ресурсы при отмене подписки
        bleScanner.scanBleDevices().test(timeout = 5.seconds) {
            // Сканирование активно в радиоэфире
            // Завершаем сбор, проверяя, что stopScan не уронит систему
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun bleScanner_capturesRealAdvertisementPacket() = runTest {
        // Тест для проверки захвата пакета в эфире.
        // Запусти симулятор пульсометра в nRF Connect на втором телефоне
        // или включи физический пульсометр рядом перед запуском.
        bleScanner.scanBleDevices().test(timeout = 10.seconds) {
            val result = awaitItem()

            // Проверяем, что реальный контроллер вернул валидный результат
            assert(result.device.address.isNotEmpty())
            assert(result.rssi < 0) // Уровень сигнала всегда отрицательный (dBm)

            cancelAndIgnoreRemainingEvents()
        }
    }
}