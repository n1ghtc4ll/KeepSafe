package com.example.cardioproject.settings.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// ---------- Colors matching the mock (light theme) ----------

private val BackgroundColor = Color(0xFFFFFFFF)
private val SurfaceColor = Color(0xFFF5F5F7)
private val AccentPurple = Color(0xFF7C6BFF)
private val TextPrimary = Color(0xFF1A1A1E)
private val TextSecondary = Color(0xFF737379)
private val DividerColor = Color(0xFFE0E0E4)
private val DestructiveRed = Color(0xFFD32F2F)

private val KeepSafeLightColorScheme = lightColorScheme(
    background = BackgroundColor,
    surface = SurfaceColor,
    primary = AccentPurple,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

// ---------- Data models ----------

data class HeartRateZone(
    val label: String,
    var range: ClosedFloatingPointRange<Float>
)

data class TimerProfileUi(
    val id: String,
    val name: String
)

data class WorkoutTag(
    val id: String,
    val name: String,
    val color: String, // Добавили цвет, так как он есть в TagEntity
    val editable: Boolean = true
)

data class SettingsUiState(
    val deviceName: String = "Coospo HW9",
    val deviceBattery: Int = 85,
    val isDeviceConnected: Boolean = true,
    val phaseSoundEnabled: Boolean = true,
    val signalVolume: Float = 0.6f,
    val tags: List<WorkoutTag> = listOf(
        WorkoutTag(UUID.randomUUID().toString(), "Общая", "#6750A4", false),
        WorkoutTag(UUID.randomUUID().toString(), "Силовая", "#6750A4", false),
        WorkoutTag(UUID.randomUUID().toString(), "Легкая", "#6750A4", false),
        WorkoutTag(UUID.randomUUID().toString(), "Вело", "#6750A4", false)
    ),
    val timerProfiles: List<TimerProfileUi> = listOf(
        TimerProfileUi("1", "Профиль 1"),
        TimerProfileUi("2", "Профиль 2")
    ),
    val birthDate: String = "",
    val gender: String = "",
    val height: String = "",
    val zones: List<HeartRateZone> = listOf(
        HeartRateZone("Зона 1", 60f..79f),
        HeartRateZone("Зона 2", 60f..79f),
        HeartRateZone("Зона 3", 60f..79f),
        HeartRateZone("Зона 4", 60f..79f),
    ),
    val criticalPulseAlertEnabled: Boolean = true,
    val keepScreenOnEnabled: Boolean = true,
)

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
    onAddTag: (String) -> Unit = {},
    onEditTag: (WorkoutTag, String) -> Unit = { _, _ -> },
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

    Scaffold(
        topBar = { SettingsTopBar(onBackClick) },
        containerColor = BackgroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = 32.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                SectionTitle("Bluetooth-устройства")
                BluetoothDeviceRow(
                    name = state.deviceName,
                    battery = state.deviceBattery,
                    isConnected = state.isDeviceConnected,
                    onDisconnect = onDisconnectDevice
                )
                Spacer(Modifier.height(8.dp))
                OutlinedFullWidthButton(text = "Поиск устройств", onClick = onSearchDevices)
                SectionDivider()
            }

            item {
                SectionTitle("Табата и звуки")
                ToggleRow(
                    label = "Звуки смены фаз таймера",
                    checked = state.phaseSoundEnabled,
                    onCheckedChange = onPhaseSoundToggle
                )
                VolumeRow(
                    volume = state.signalVolume,
                    onVolumeChange = onVolumeChange
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Профили таймера",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TimerProfilesSection(
                    profiles = state.timerProfiles,
                    onAddClick = onAddProfileClick,
                    onEditClick = onEditProfileClick,
                    onDeleteClick = onDeleteProfileClick
                )

                SectionDivider()
            }

            item {
                SectionTitle("Теги тренировок")
                TagsFlowRow(
                    tags = state.tags,
                    onEditTag = { tag -> tagBeingEdited = tag }
                )
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = { showAddTagDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = BorderStroke(1.dp, DividerColor)
                    ) {
                        Text("Добавить")
                    }
                }
                SectionDivider()
            }

            item {
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
                SectionDivider()
            }

            item {
                SectionTitle("Пульс")
                OutlinedFullWidthButton(text = "Авторасчет зон ЧСС", onClick = onAutoCalculateZones)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Ручная настройка зон ЧСС",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            itemsIndexed(items = state.zones) { index, zone ->
                HeartRateZoneRow(
                    zone = zone,
                    onRangeChange = { newRange -> onZoneChange(index, newRange) }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                ToggleRow(
                    label = "Оповещение о критическом пульсе",
                    checked = state.criticalPulseAlertEnabled,
                    onCheckedChange = onCriticalPulseAlertToggle
                )
                SectionDivider()
            }

            item {
                SectionTitle("Дополнительно")
                ToggleRow(
                    label = "Оставить экран включенным",
                    checked = state.keepScreenOnEnabled,
                    onCheckedChange = onKeepScreenOnToggle
                )
            }
        }
    }

    if (showAddTagDialog) {
        TagInputDialog(
            title = "Новый тег",
            initialValue = "",
            onConfirm = { name ->
                onAddTag(name)
                showAddTagDialog = false
            },
            onDismiss = { showAddTagDialog = false }
        )
    }

    tagBeingEdited?.let { tag ->
        TagInputDialog(
            title = "Редактировать тег",
            initialValue = tag.name,
            onConfirm = { newName ->
                onEditTag(tag, newName)
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

    /*if (showBirthDateDialog) {
        TextInputDialog(
            title = "Дата рождения",
            placeholder = "гггг-мм-дд",
            initialValue = state.birthDate,
            onConfirm = { value ->
                onBirthDateChange(value)
                showBirthDateDialog = false
            },
            onDismiss = { showBirthDateDialog = false }
        )
    }*/
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

// ---------- Components ----------

@Composable
private fun SettingsTopBar(onBackClick: () -> Unit) {
    Surface(color = BackgroundColor) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Настройки",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 12.dp, bottom = 10.dp)
    )
}

@Composable
private fun SectionDivider() {
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun BluetoothDeviceRow(
    name: String,
    battery: Int,
    isConnected: Boolean,
    onDisconnect: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = name, color = TextPrimary, fontSize = 15.sp)
            Text(text = "Батарея: $battery%", color = TextSecondary, fontSize = 13.sp)
        }
        if (isConnected) {
            OutlinedButton(
                onClick = onDisconnect,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                border = BorderStroke(1.dp, DividerColor)
            ) {
                Text("Отключить")
            }
        }
    }
}

@Composable
private fun OutlinedFullWidthButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
        border = BorderStroke(1.dp, DividerColor)
    ) {
        Text(text)
    }
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextPrimary, fontSize = 15.sp, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AccentPurple,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = DividerColor
            )
        )
    }
}

@Composable
private fun VolumeRow(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Громкость сигналов", color = TextPrimary, fontSize = 15.sp)
        Spacer(Modifier.height(6.dp))
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            colors = SliderDefaults.colors(
                thumbColor = AccentPurple,
                activeTrackColor = AccentPurple,
                inactiveTrackColor = DividerColor
            )
        )
    }
}

@Composable
private fun TimerProfilesSection(
    profiles: List<TimerProfileUi>,
    onAddClick: () -> Unit,
    onEditClick: (TimerProfileUi) -> Unit,
    onDeleteClick: (TimerProfileUi) -> Unit
) {
    Column {
        if (profiles.isNotEmpty()) {
            Surface(
                color = SurfaceColor,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DividerColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    profiles.forEachIndexed { index, profile ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = profile.name, color = TextPrimary, fontSize = 15.sp)
                            Row {
                                IconButton(
                                    onClick = { onEditClick(profile) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Редактировать",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(Modifier.width(16.dp))
                                IconButton(
                                    onClick = { onDeleteClick(profile) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Удалить",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        if (index < profiles.lastIndex) {
                            androidx.compose.material3.Divider(color = DividerColor, thickness = 1.dp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(
                onClick = onAddClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                border = BorderStroke(1.dp, DividerColor)
            ) {
                Text("Добавить")
            }
        }
    }
}

@Composable
private fun TagsFlowRow(
    tags: List<WorkoutTag>,
    onEditTag: (WorkoutTag) -> Unit
) {
    SimpleFlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalGap = 8.dp,
        verticalGap = 8.dp
    ) {
        tags.forEach { tag ->
            TagChip(tag = tag, onEditClick = { onEditTag(tag) })
        }
    }
}

/**
 * Minimal wrap-row layout that places children left-to-right, wrapping to a new line
 * when the current row would exceed the available width. Implemented directly with
 * [Layout] instead of foundation's FlowRow to avoid API-version mismatches between
 * the compiled app and the IDE preview renderer.
 */
@Composable
private fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 8.dp,
    verticalGap: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val horizontalGapPx = horizontalGap.roundToPx()
        val verticalGapPx = verticalGap.roundToPx()
        val maxWidth = constraints.maxWidth
        val itemConstraints = Constraints(maxWidth = maxWidth)
        val placeables = measurables.map { it.measure(itemConstraints) }

        data class FlowLine(val items: MutableList<androidx.compose.ui.layout.Placeable> = mutableListOf(), var width: Int = 0, var height: Int = 0)

        val rows = mutableListOf(FlowLine())
        placeables.forEach { placeable ->
            var row = rows.last()
            val extraWidth = if (row.items.isEmpty()) placeable.width else horizontalGapPx + placeable.width
            if (row.items.isNotEmpty() && row.width + extraWidth > maxWidth) {
                row = FlowLine()
                rows.add(row)
            }
            row.items.add(placeable)
            row.width += if (row.items.size == 1) placeable.width else horizontalGapPx + placeable.width
            row.height = maxOf(row.height, placeable.height)
        }

        val totalHeight = rows.sumOf { it.height } + verticalGapPx * (rows.size - 1).coerceAtLeast(0)

        layout(maxWidth, totalHeight) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                row.items.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + horizontalGapPx
                }
                y += row.height + verticalGapPx
            }
        }
    }
}

@Composable
private fun TagChip(tag: WorkoutTag, onEditClick: () -> Unit) {
    Surface(
        color = SurfaceColor,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, DividerColor)
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tag.name, color = TextPrimary, fontSize = 14.sp)
            if (tag.editable) {
                Spacer(Modifier.width(6.dp))
                IconButton(onClick = onEditClick, modifier = Modifier.size(18.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать тег",
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledInputRow(
    label: String,
    value: String,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { base -> if (onClick != null) base.clickable(onClick = onClick) else base }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextPrimary, fontSize = 15.sp)
        Surface(
            color = SurfaceColor,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, DividerColor)
        ) {
            Row(
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeartRateZoneRow(
    zone: HeartRateZone,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = zone.label, color = TextPrimary, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${zone.range.start.toInt()}-${zone.range.endInclusive.toInt()} уд/мин",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        RangeSlider(
            value = zone.range,
            onValueChange = onRangeChange,
            valueRange = 40f..200f,
            colors = SliderDefaults.colors(
                thumbColor = AccentPurple,
                activeTrackColor = AccentPurple,
                inactiveTrackColor = DividerColor
            )
        )
    }
}

// ---------- Dialogs ----------

@Composable
private fun TagInputDialog(
    title: String,
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var text by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                label = { Text("Название тега") }
            )
        },
        confirmButton = {
            TextButton(onClick = { if (text.isNotBlank()) onConfirm(text.trim()) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Row {
                if (onDelete != null) {
                    TextButton(onClick = onDelete) {
                        Text("Удалить", color = DestructiveRed)
                    }
                }
                TextButton(onClick = onDismiss) { Text("Отмена") }
            }
        }
    )
}

@Composable
private fun TextInputDialog(
    title: String,
    initialValue: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                placeholder = { Text(placeholder) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text.trim()) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDatePickerDialog(
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val formattedDate = formatter.format(Date(millis))
                        onConfirm(formattedDate)
                    } ?: onDismiss()
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )
    }
}

@Composable
private fun GenderPickerDialog(
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableStateOf(initialValue) }
    val options = listOf("Мужской", "Женский")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Пол") },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = option }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selected == option, onClick = { selected = option })
                        Spacer(Modifier.width(8.dp))
                        Text(option, color = TextPrimary)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

// ---------- Preview ----------

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 900)
@Composable
private fun SettingsScreenPreview() {
    var state by remember { mutableStateOf(SettingsUiState()) }
    MaterialTheme(colorScheme = KeepSafeLightColorScheme) {
        SettingsScreen(
            state = state,
            onPhaseSoundToggle = { state = state.copy(phaseSoundEnabled = it) },
            onVolumeChange = { state = state.copy(signalVolume = it) },
            onAddTag = { name ->
                state = state.copy(tags = state.tags + WorkoutTag(id = UUID.randomUUID().toString(), name = name, color = "#FF0000"))            },
            onEditTag = { tag, newName ->
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