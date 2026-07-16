package com.example.cardioproject.core.bluetooth.data

import java.util.UUID

object BleConstants {
    private const val BASE_UUID_PREFIX = "0000"
    private const val BASE_UUID_SUFFIX = "-0000-1000-8000-00805f9b34fb"

    val HEART_RATE_SERVICE_UUID: UUID = UUID.fromString("${BASE_UUID_PREFIX}180D$BASE_UUID_SUFFIX")

    val HEART_RATE_CHARACTERISTIC_UUID: UUID = UUID.fromString("${BASE_UUID_PREFIX}2A37$BASE_UUID_SUFFIX")

    val CLIENT_CHARACTERISTIC_CONFIG_UUID: UUID = UUID.fromString("${BASE_UUID_PREFIX}2902$BASE_UUID_SUFFIX")
}