package com.example.cardioproject.settings.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.cardioproject.settings.presentation.viewmodel.DeviceConnectViewModel
import com.example.cardioproject.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Точка входа для навигации: NavHost должен вызывать именно SettingsRoute(...),
 * а не SettingsScreen(...) напрямую — Route подключает ViewModel и state,
 * SettingsScreen остаётся чистым stateless-компонентом (удобно для превью/тестов).
 *
 * ViewModel собирается через Koin (см. presentation/di/SettingsModule.kt) —
 * убедись, что settingsModule подключён в общий startKoin { modules(...) }.
 *
 * Пример подключения в NavHost:
 *   composable("settings") { SettingsRoute(onBackClick = { navController.popBackStack() }) }
 */
@Composable
fun SettingsRoute(
    onBackClick: () -> Unit = {},
    onNavigateToProfileBuilder: (String?) -> Unit = {}
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val deviceViewModel: DeviceConnectViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showScanSheet by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            showScanSheet = true
        }
    }

    fun onSearchDevicesClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasScan = ContextCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED

            val hasConnect = ContextCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED

            if (hasScan && hasConnect) {
                showScanSheet = true
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
            }
        } else {
            val hasLocation = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasLocation) {
                showScanSheet = true
            } else {
                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    SettingsScreen(
        state = state,
        onBackClick = onBackClick,
        onSearchDevices = ::onSearchDevicesClick/*{

            *//*try {
                // Открываем системные настройки Bluetooth телефона
                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                context.startActivity(intent)
            } catch (e: Exception) {
                // Защита, если на кастомной прошивке путь к настройкам отличается
            }*//*
        }*/,
        onAddProfileClick = { onNavigateToProfileBuilder(null) },
        onEditProfileClick = { profile -> onNavigateToProfileBuilder(profile.id) },
        onDeleteProfileClick = viewModel::onDeleteProfile,
        onPhaseSoundToggle = viewModel::onPhaseSoundToggle,
        onVolumeChange = viewModel::onVolumeChange,
        onAddTag = viewModel::onAddTag,
        onEditTag = viewModel::onEditTag,
        onDeleteTag = viewModel::onDeleteTag,
        onZoneChange = viewModel::onZoneChange,
        onAutoCalculateZones = viewModel::onAutoCalculateZones,
        onCriticalPulseAlertToggle = viewModel::onCriticalPulseAlertToggle,
        onKeepScreenOnToggle = viewModel::onKeepScreenOnToggle,
        onBirthDateChange = viewModel::onBirthDateChange,
        onGenderChange = viewModel::onGenderChange,
        onHeightChange = viewModel::onHeightChange,
    )

    if (showScanSheet) {
        DeviceScanBottomSheet(
            viewModel = deviceViewModel,
            onDismiss = { showScanSheet = false }
        )
    }
}