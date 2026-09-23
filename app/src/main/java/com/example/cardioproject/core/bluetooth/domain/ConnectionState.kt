package com.example.cardioproject.core.bluetooth.domain

sealed interface ConnectionState {
    data object Disconnected : ConnectionState
    data object Connecting : ConnectionState
    data object Connected : ConnectionState
    data class Error(val status: Int, val message: String) : ConnectionState
}