package com.example.cardioproject.workout.presentation.util

import androidx.compose.ui.graphics.Color
import com.example.cardioproject.core.ui.theme.UnknownZoneColor
import com.example.cardioproject.core.ui.theme.Zone1Blue
import com.example.cardioproject.core.ui.theme.Zone2Green
import com.example.cardioproject.core.ui.theme.Zone3Yellow
import com.example.cardioproject.core.ui.theme.Zone4Red
import com.example.cardioproject.settings.domain.model.HeartRateZone

fun getZoneColorForBpm(bpm: Int?, zones: List<HeartRateZone>): Color {
    if (bpm == null || bpm == 0 || zones.isEmpty()) return UnknownZoneColor

    val activeZone = zones.find { bpm.toFloat() in it.lowerBpm.toFloat()..it.upperBpm.toFloat() }

    return when (activeZone?.index) {
        0 -> Zone1Blue
        1 -> Zone2Green
        2 -> Zone3Yellow
        3 -> Zone4Red
        else -> UnknownZoneColor
    }
}