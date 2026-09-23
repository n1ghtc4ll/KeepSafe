package com.example.cardioproject.core.bluetooth.data

import com.example.cardioproject.core.bluetooth.domain.HeartRateData

object HeartRateParser {
    fun parse(data: ByteArray): HeartRateData? {
        if (data.isEmpty()) return null

        var offset = 0

        val flags = data[offset].toInt()
        offset += 1

        val isFormatUint16 = (flags and 0x01) != 0

        val heartRateBpm: Int = if (isFormatUint16) {
            if (data.size < offset + 2) return null

            val lowByte = data[offset].toInt() and 0xFF
            val highByte = data[offset + 1].toInt() and 0xFF
            val value = lowByte or (highByte shl 8)

            offset += 2
            value
        } else {
            if (data.size < offset + 1) return null
            val value = data[offset].toInt() and 0xFF

            offset += 1
            value
        }

        val isSensorContactSupported = (flags and 0x04) != 0
        val isSensorContactDetected = if (isSensorContactSupported) {
            (flags and 0x02) != 0
        } else {
            null
        }

        val hasEnergyExpended = (flags and 0x08) != 0
        if (hasEnergyExpended) offset += 2

        val hasRrIntervals = (flags and 0x10) != 0
        val rrIntervalsList = mutableListOf<Int>()

        if (hasRrIntervals) {
            while (offset + 1 < data.size) {
                val low = data[offset].toInt() and 0xFF
                val high = data[offset + 1].toInt() and 0xFF
                val rawRrValue = low or (high shl 8)

                val rrMs = (rawRrValue * 1000) / 1024
                rrIntervalsList.add(rrMs)

                offset += 2
            }
        }

        return HeartRateData(
            heartRateBpm = heartRateBpm,
            isSensorContactDetected = isSensorContactDetected,
            rrIntervalsMs = rrIntervalsList
        )
    }
}