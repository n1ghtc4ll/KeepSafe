package com.example.cardioproject.settings.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardioproject.core.ui.components.AppThemeTopBar
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.core.ui.theme.spacing
import java.util.UUID
import com.example.cardioproject.settings.presentation.model.SettingsUiState
import com.example.cardioproject.settings.presentation.model.TimerProfileUi
import com.example.cardioproject.settings.presentation.model.WorkoutTag
import com.example.cardioproject.settings.presentation.ui.components.BirthDatePickerDialog
import com.example.cardioproject.settings.presentation.ui.components.BluetoothDeviceRow
import com.example.cardioproject.settings.presentation.ui.components.GenderPickerDialog
import com.example.cardioproject.settings.presentation.ui.components.HeartRateZoneRow
import com.example.cardioproject.settings.presentation.ui.components.LabeledInputRow
import com.example.cardioproject.settings.presentation.ui.components.OutlinedFullWidthButton
import com.example.cardioproject.settings.presentation.ui.components.SectionDivider
import com.example.cardioproject.settings.presentation.ui.components.SectionTitle
import com.example.cardioproject.settings.presentation.ui.components.TagInputDialog
import com.example.cardioproject.settings.presentation.ui.components.TagsFlowRow
import com.example.cardioproject.settings.presentation.ui.components.TextInputDialog
import com.example.cardioproject.settings.presentation.ui.components.TimerProfilesSection
import com.example.cardioproject.settings.presentation.ui.components.ToggleRow
import com.example.cardioproject.settings.presentation.ui.components.VolumeRow

// ---------- Screen ----------

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBackClick: () -> Unit = {},
    onAddProfileClick: () -> Unit = {},
    onEditProfileClick: (TimerProfileUi) -> Unit = {},
    onDeleteProfileClick: (TimerProfileUi) -> Unit = {},
    onDisconnectDevice: () -> Unit = {},
    onSearchDevices: () -> Unit = {},
    onPhaseSoundToggle: (Boolean) -> Unit = {},
    onVolumeChange: (Float) -> Unit = {},
    onAddTag: (String, String) -> Unit = { _, _ -> },
    onEditTag: (WorkoutTag, String, String) -> Unit = { _, _, _ -> },
    onDeleteTag: (WorkoutTag) -> Unit = {},
    onZoneChange: (Int, ClosedFloatingPointRange<Float>) -> Unit = { _, _ -> },
    onAutoCalculateZones: () -> Unit = {},
    onCriticalPulseAlertToggle: (Boolean) -> Unit = {},
    onKeepScreenOnToggle: (Boolean) -> Unit = {},
    onBirthDateChange: (String) -> Unit = {},
    onGenderChange: (String) -> Unit = {},
    onHeightChange: (String) -> Unit = {},
) {
    var showAddTagDialog by remember { mutableStateOf(false) }
    var tagBeingEdited by remember { mutableStateOf<WorkoutTag?>(null) }
    var showBirthDateDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }

    val defaultTagColorHex = String.format("#%08X", MaterialTheme.colorScheme.primary.toArgb())

    Scaffold(
        topBar = { AppThemeTopBar(title = "Настройки", onBackClick = onBackClick) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + MaterialTheme.spacing.small,
                bottom = MaterialTheme.spacing.extraLarge,
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            item {
                SectionTitle(
                    text = "Bluetooth-устройства",

                ) {
                    if (state.isDeviceConnected) {
                        BluetoothDeviceRow(
                            name = state.deviceName,
                            battery = state.deviceBattery,
                            onDisconnect = onDisconnectDevice
                        )
                    } else {
                        OutlinedFullWidthButton(
                            text = "Поиск устройств",
                            onClick = onSearchDevices
                        )
                    }
                }
            }

            item {
                SectionTitle("Табата и звуки") {
                    ToggleRow(
                        label = "Звуки смены фаз таймера",
                        checked = state.phaseSoundEnabled,
                        onCheckedChange = onPhaseSoundToggle
                    )
                    VolumeRow(
                        label = "Громкость сигналов",
                        volume = state.signalVolume,
                        onVolumeChange = onVolumeChange
                    )


                    TimerProfilesSection(
                        label = "Профили таймера",
                        profiles = state.timerProfiles,
                        onAddClick = onAddProfileClick,
                        onEditClick = onEditProfileClick,
                        onDeleteClick = onDeleteProfileClick
                    )
                }
            }

            item {
                SectionTitle("Теги тренировок") {
                    TagsFlowRow(
                        tags = state.tags,
                        onEditTag = { tag -> tagBeingEdited = tag }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { showAddTagDialog = true },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text("Добавить")
                        }
                    }
                }
            }

            item {
                SectionTitle(
                    text = "Личные данные"
                ) {
                    LabeledInputRow(
                        label = "Дата рождения",
                        value = state.birthDate,
                        icon = Icons.Default.DateRange,
                        onClick = { showBirthDateDialog = true }
                    )
                    LabeledInputRow(
                        label = "Пол",
                        value = state.gender,
                        onClick = { showGenderDialog = true }
                    )
                    LabeledInputRow(
                        label = "Рост",
                        value = state.height,
                        onClick = { showHeightDialog = true }
                    )
                }
            }

            item {
                SectionTitle("Пульс") {
                    OutlinedFullWidthButton(
                        text = "Авторасчет зон ЧСС",
                        onClick = onAutoCalculateZones
                    )

                    state.zones.forEachIndexed { index, zone ->
                        HeartRateZoneRow(
                            zone = zone,
                            onRangeChange = { newRange -> onZoneChange(index, newRange) }
                        )
                    }

                    ToggleRow(
                        label = "Оповещение о критическом пульсе",
                        checked = state.criticalPulseAlertEnabled,
                        onCheckedChange = onCriticalPulseAlertToggle
                    )
                }
            }

            item {
                SectionTitle("Дополнительно") {
                    ToggleRow(
                        label = "Оставить экран включенным",
                        checked = state.keepScreenOnEnabled,
                        onCheckedChange = onKeepScreenOnToggle
                    )
                }
            }
        }
    }

    if (showAddTagDialog) {
        TagInputDialog(
            title = "Новый тег",
            initialName = "",
            initialColorHex = defaultTagColorHex,
            onConfirm = { name, colorHex ->
                onAddTag(name, colorHex)
                showAddTagDialog = false
            },
            onDismiss = { showAddTagDialog = false }
        )
    }

    tagBeingEdited?.let { tag ->
        TagInputDialog(
            title = "Редактировать тег",
            initialName = tag.name,
            initialColorHex = tag.color,
            onConfirm = { newName, newColorHex ->
                onEditTag(tag, newName, newColorHex)
                tagBeingEdited = null
            },
            onDismiss = { tagBeingEdited = null },
            onDelete = if (tag.editable) {
                {
                    onDeleteTag(tag)
                    tagBeingEdited = null
                }
            } else null
        )
    }

    if (showBirthDateDialog) {
        BirthDatePickerDialog(
            initialValue = state.birthDate,
            onConfirm = { formattedDate ->
                onBirthDateChange(formattedDate)
                showBirthDateDialog = false
            },
            onDismiss = { showBirthDateDialog = false }
        )
    }

    if (showGenderDialog) {
        GenderPickerDialog(
            initialValue = state.gender,
            onConfirm = { value ->
                onGenderChange(value)
                showGenderDialog = false
            },
            onDismiss = { showGenderDialog = false }
        )
    }

    if (showHeightDialog) {
        TextInputDialog(
            title = "Рост, см",
            placeholder = "175",
            initialValue = state.height,
            keyboardType = KeyboardType.Number,
            onConfirm = { value ->
                onHeightChange(value)
                showHeightDialog = false
            },
            onDismiss = { showHeightDialog = false }
        )
    }
}

// ---------- Preview ----------

@Preview(showBackground = true, widthDp = 360, heightDp = 2000)
@Composable
private fun SettingsScreenPreview() {
    var state by remember { mutableStateOf(SettingsUiState()) }
    KeepSafeProjectTheme {
        SettingsScreen(
            state = state,
            onPhaseSoundToggle = { state = state.copy(phaseSoundEnabled = it) },
            onVolumeChange = { state = state.copy(signalVolume = it) },
            onAddTag = { name, color ->
                state = state.copy(tags = state.tags + WorkoutTag(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    color = "#FF0000"
                )
                )            },
            onEditTag = { tag, newName, color ->
                state = state.copy(
                    tags = state.tags.map { if (it.id == tag.id) it.copy(name = newName) else it }
                )
            },
            onDeleteTag = { tag ->
                state = state.copy(tags = state.tags.filterNot { it.id == tag.id })
            },
            onZoneChange = { index, range ->
                val updated = state.zones.toMutableList()
                updated[index] = updated[index].copy(range = range)
                state = state.copy(zones = updated)
            },
            onCriticalPulseAlertToggle = { state = state.copy(criticalPulseAlertEnabled = it) },
            onKeepScreenOnToggle = { state = state.copy(keepScreenOnEnabled = it) },
            onBirthDateChange = { state = state.copy(birthDate = it) },
            onGenderChange = { state = state.copy(gender = it) },
            onHeightChange = { state = state.copy(height = it) }
        )
    }
}