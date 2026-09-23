package com.example.cardioproject.core.bluetooth.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BleScannerTest {
    private val bluetoothAdapter: BluetoothAdapter = mockk()
    private val leScanner: BluetoothLeScanner = mockk()

    private val callbackSlot = slot<ScanCallback>()

    private lateinit var bleScanner: BleScanner

    @Before
    fun setUp() {
        every { bluetoothAdapter.bluetoothLeScanner } returns leScanner

        every {
            leScanner.startScan(any(), any(), capture(callbackSlot))
        } returns Unit

        bleScanner = BleScanner(bluetoothAdapter)
    }

    @Test
    fun `scan starts with correct settings and emits ScanResult`() = runTest {
        val mockScanResult: ScanResult = mockk()

        bleScanner.scanBleDevices().test {
            // Убеждаемся, что startScan вызван
            verify(exactly = 1) { leScanner.startScan(any(), any(), any<ScanCallback>()) }

            // Имитируем приход пакета от периферии через пойманный callback
            callbackSlot.captured.onScanResult(0, mockScanResult)

            // Проверяем, что результат попал в наш Flow
            val emittedResult = awaitItem()
            assertEquals(mockScanResult, emittedResult)

            // Завершаем тест, отменяя сбор
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `scan throws exception when bluetoothAdapter is null`() = runTest {
        every { bluetoothAdapter.bluetoothLeScanner } returns null
        val disabledBleScanner = BleScanner(bluetoothAdapter)

        disabledBleScanner.scanBleDevices().test {
            val error = awaitError()
            assertTrue(error is CancellationException)
            assertEquals("Bluetooth выключен", error.message)
        }

        verify(exactly = 0) { leScanner.startScan(any(), any(), any<ScanCallback>()) }
    }
}