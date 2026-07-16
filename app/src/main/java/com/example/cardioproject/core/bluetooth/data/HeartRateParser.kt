package com.example.cardioproject.core.bluetooth.data

object HeartRateParser {
    fun parseHeartRate(bytes: ByteArray): Int {
        if (bytes.isEmpty()) return 0

        val formatFlag = bytes[0].toInt() and 0x01
        val isFormat16Bit = formatFlag != 0

        return if (isFormat16Bit) {
            val byte1 = bytes[1].toInt() and 0xFF
            val byte2 = bytes[2].toInt() and 0xFF
            (byte2 shl 8) or byte1
        } else {
            bytes[1].toInt() and 0xFF
        }
    }
}