package com.example.cardioproject.settings.presentation.screen

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cardioproject.settings.domain.model.DeviceInfo
import com.example.cardioproject.settings.presentation.viewmodel.DeviceConnectViewModel
import org.koin.compose.viewmodel.koinViewModel

@RequiresPermission(allOf = [
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT
])
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScanBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    viewModel: DeviceConnectViewModel = koinViewModel(),
) {
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val devices by viewModel.discoveredDevices.collectAsStateWithLifecycle()
    DisposableEffect(Unit) {
        viewModel.startScan()
        onDispose {
            viewModel.stopScan()
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        DeviceScanBottomSheetContent(
            modifier = modifier,
            onDeviceClick = viewModel::selectAndConnectDevice,
            devices = devices,
            isScanning = isScanning,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun DeviceScanBottomSheetContent(
    modifier: Modifier = Modifier,
    onDeviceClick: (DeviceInfo) -> Unit = { },
    onRetryClick: () -> Unit = { },
    onDismiss: () -> Unit = { },
    devices: List<DeviceInfo>,
    isScanning: Boolean
) {
    Column(modifier = modifier
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Выбор Bluetooth-устройства",
                style = MaterialTheme.typography.titleLarge,
            )

            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                IconButton(onClick = onRetryClick) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Повторить поиск",
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (devices.isEmpty()) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text =
                        if (isScanning)
                            "Ищем датчики рядом...\nУбедитесь, что пульсометр включен и надет"
                        else
                            "Устройства не найдены",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                if (isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 2.5.dp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                items(devices) { device ->
                    DeviceItem(
                        device = device,
                        onClick = {
                            onDeviceClick(device)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    device: DeviceInfo,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = { Text(text = device.name) },
        supportingContent = { Text(text = device.macAddress) }
    )
}

@Preview(showBackground = true)
@Composable
fun DeviceScanBottomSheetPreview() {
    DeviceScanBottomSheetContent(
        devices = listOf(
            DeviceInfo(
                name = "device0",
                macAddress = "AA:AA:AA:AA:AA",
                batteryLevel = 0,
                isConnected = false),
            DeviceInfo(
                name = "device1",
                macAddress = "BB:BB:BB:BB:BB",
                batteryLevel = 0,
                isConnected = false)
        ),
        isScanning = true
    )
}