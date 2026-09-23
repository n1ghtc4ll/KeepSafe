package com.example.cardioproject.core.bluetooth.domain

data class HeartRateData(
    val heartRateBpm: Int,
    val isSensorContactDetected: Boolean? = null,
    val rrIntervalsMs: List<Int> = emptyList()
)