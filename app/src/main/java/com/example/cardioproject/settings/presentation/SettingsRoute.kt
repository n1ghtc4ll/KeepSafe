package com.example.cardioproject.settings.presentation

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    SettingsScreen(
        state = state,
        onBackClick = onBackClick,
        onSearchDevices = {
            try {
                // Открываем системные настройки Bluetooth телефона
                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                context.startActivity(intent)
            } catch (e: Exception) {
                // Защита, если на кастомной прошивке путь к настройкам отличается
            }
        },
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
}