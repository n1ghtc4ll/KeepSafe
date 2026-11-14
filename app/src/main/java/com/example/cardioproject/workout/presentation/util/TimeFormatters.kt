package com.example.cardioproject.workout.presentation.util

import java.util.Locale

fun Int.formatAsMinutesSeconds(): String {
    val initialSeconds = coerceAtLeast(0)
    val minutes = initialSeconds / 60
    val seconds = initialSeconds % 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

fun Int.formatAsHoursMinutesSeconds(): String {
    val initialSeconds = coerceAtLeast(0)
    val hours = initialSeconds / 3600
    val minutes = (initialSeconds % 3600) / 60
    val seconds = initialSeconds % 60

    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}
