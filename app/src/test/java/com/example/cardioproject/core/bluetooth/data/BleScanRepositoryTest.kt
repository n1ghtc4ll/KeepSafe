package com.example.cardioproject.core.bluetooth.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class BleScanRepositoryTest {
    private val bleScanner: BleScanner = mockk()

    private val scanFlow = MutableSharedFlow<ScanResult>()

    private lateinit var scanRepository: BleScanRepository

    @Before
    fun setUp() {
        every {  bleScanner.scanBleDevices() } returns scanFlow
        scanRepository = BleScanRepository(bleScanner)
    }

    fun createMockScanResult(address: String, rssi: Int): ScanResult {
        val mockDevice: BluetoothDevice = mockk {
            every { this@mockk.address } returns address
        }

        return mockk {
            every { device } returns mockDevice
            every { this@mockk.rssi } returns rssi
        }
    }

    @Test
    fun `getDiscoveredDevices deduplicates by MAC, sorts by RSSI descending and run updates in a sample time`() = runTest {
        scanRepository.getDiscoveredDevices().test {
            scanFlow.emit(createMockScanResult(address = "AA:AA:AA:AA:AA:AA", rssi = -80))
            scanFlow.emit(createMockScanResult(address = "BB:BB:BB:BB:BB:BB", rssi = -60))
            scanFlow.emit(createMockScanResult(address = "AA:AA:AA:AA:AA:AA", rssi = -40))
            expectNoEvents()
            advanceTimeBy(500L.milliseconds)

            val firstResult = awaitItem()
            assertEquals(2, firstResult.size)

            assertEquals("AA:AA:AA:AA:AA:AA", firstResult[0].device.address)
            assertEquals(-40, firstResult[0].rssi)
            assertEquals("BB:BB:BB:BB:BB:BB", firstResult[1].device.address)
            assertEquals(-60, firstResult[1].rssi)


            scanFlow.emit(createMockScanResult(address = "BB:BB:BB:BB:BB:BB", rssi = -35))
            scanFlow.emit(createMockScanResult(address = "CC:CC:CC:CC:CC:CC", rssi = -50))
            advanceTimeBy(500L.milliseconds)

            val secondResult = awaitItem()
            assertEquals(3, secondResult.size)

            assertEquals("BB:BB:BB:BB:BB:BB", secondResult[0].device.address)
            assertEquals(-35, secondResult[0].rssi)
            assertEquals("AA:AA:AA:AA:AA:AA", secondResult[1].device.address)
            assertEquals(-40, secondResult[1].rssi)
            assertEquals("CC:CC:CC:CC:CC:CC", secondResult[2].device.address)
            assertEquals(-50, secondResult[2].rssi)

            cancelAndIgnoreRemainingEvents()
        }
    }
}