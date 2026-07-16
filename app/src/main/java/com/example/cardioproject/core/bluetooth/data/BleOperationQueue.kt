package com.example.cardioproject.core.bluetooth.data

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

class BleOperationQueue {
    private val mutex = Mutex()

    private var pendingContinuation: CancellableContinuation<Unit>? = null

    suspend fun enqueueOperation(operation: () -> Unit) {
        mutex.withLock {
            suspendCancellableCoroutine { continuation ->
                pendingContinuation = continuation
                operation.invoke()
            }
        }
    }

    fun signalOperationCompleted() {
        pendingContinuation?.resume(Unit)
        pendingContinuation = null
    }

    fun clear() {
        pendingContinuation?.cancel()
        pendingContinuation = null
    }
}