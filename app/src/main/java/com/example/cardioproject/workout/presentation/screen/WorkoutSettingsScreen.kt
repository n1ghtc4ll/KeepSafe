package com.example.cardioproject.workout.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.model.WorkoutSettingsUiState
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

private val SettingsBackground = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF000000)
private val CardBorder = Color(0xFFCAC4D0)
private val AccentPurple = Color(0xFF6750A4)
private val InputBackgroundGray = Color(0xFFEEEEEE)
private val TabataBackground = Color(0xFFF3EDF7)
private val ElementBackgroundWhite = Color(0xFFFFFFFF)


@Composable
fun WorkoutSettingsScreen(
    viewModel: WorkoutSettingsViewModel = koinViewModel(),
    onNavigateToActiveWorkout: (WorkoutSettings) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    WorkoutSettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onStartClick = {
            val finalSettings = viewModel.getFinalSettings()
            onNavigateToActiveWorkout(finalSettings)
        },
        onTitleChange = viewModel::onTitleChanged,
        onTagSelect = viewModel::onTagSelected,
        onTabataToggle = viewModel::onTabataToggled,
        onProfileSelect = viewModel::onProfileSelected,
        onCustomParamChange = viewModel::onCustomParamChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSettingsScreenContent(
    uiState: WorkoutSettingsUiState = WorkoutSettingsUiState(),
    onBackClick: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onTagSelect: (Tag?) -> Unit = {},
    onTabataToggle: (Boolean) -> Unit = {},
    onProfileSelect: (TabataProfile?) -> Unit = {},
    onCustomParamChange: (String, Int) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SettingsBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Настройка тренировки",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LabelTextField(
                label = "Название",
                value = uiState.title,
                onValueChange = onTitleChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabelDropdownMenu(
                label = "Тег",
                items = uiState.availableTags,
                selectedItem = uiState.selectedTag,
                itemLabel = { it.name },
                onItemSelected = { onTagSelect(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Табата-таймер",
                    fontSize = 20.sp,
                    color = TextPrimary
                )
                Switch(
                    checked = uiState.isTabataEnabled,
                    onCheckedChange = onTabataToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AccentPurple
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isTabataEnabled) {
                val setup = uiState.tabataSetup
                val isReadOnly = setup is TabataSetup.Preset
                val currentProfile = (setup as? TabataSetup.Preset)?.profile

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TabataBackground, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Параметры таймера",
                        fontSize = 20.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CardBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Готовые профили",
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileDropdownMenu(
                        items = uiState.availableProfiles,
                        selectedProfile = currentProfile,
                        onItemSelected = onProfileSelect
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val warmUpValue = when (setup) {
                        is TabataSetup.Preset -> setup.profile.warmUpTimeSec
                        is TabataSetup.Custom -> setup.warmUpTimeSec
                        null -> 0
                    }
                    val repsValue = when (setup) {
                        is TabataSetup.Preset -> setup.profile.repsCount
                        is TabataSetup.Custom -> setup.repsCount
                        null -> 0
                    }
                    val workoutValue = when (setup) {
                        is TabataSetup.Preset -> setup.profile.workoutTimeSec
                        is TabataSetup.Custom -> setup.workoutTimeSec
                        null -> 0
                    }
                    val relaxValue = when (setup) {
                        is TabataSetup.Preset -> setup.profile.relaxTimeSec
                        is TabataSetup.Custom -> setup.relaxTimeSec
                        null -> 0
                    }
                    val coolDownValue = when (setup) {
                        is TabataSetup.Preset -> setup.profile.coolDownTimeSec
                        is TabataSetup.Custom -> setup.coolDownTimeSec
                        null -> 0
                    }
                    val setsValue = when (setup) {
                        is TabataSetup.Preset -> setup.profile.setsCount
                        is TabataSetup.Custom -> setup.setsCount
                        null -> 0
                    }

                    EditableTabataRow(
                        label = "Пауза перед\nтренировкой", value = warmUpValue, isReadOnly = isReadOnly, showIcon = true,
                        onValueChange = { onCustomParamChange("warmUp", it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    EditableTabataRow(
                        label = "Кол-во интервалов", value = repsValue, isReadOnly = isReadOnly, showIcon = false,
                        onValueChange = { onCustomParamChange("reps", it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    EditableTabataRow(
                        label = "Упражнение", value = workoutValue, isReadOnly = isReadOnly, showIcon = true,
                        onValueChange = { onCustomParamChange("workout", it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    EditableTabataRow(
                        label = "Отдых", value = relaxValue, isReadOnly = isReadOnly, showIcon = true,
                        onValueChange = { onCustomParamChange("relax", it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    EditableTabataRow(
                        label = "Перезарядка", value = coolDownValue, isReadOnly = isReadOnly, showIcon = true,
                        onValueChange = { onCustomParamChange("coolDown", it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    EditableTabataRow(
                        label = "Кол-во кругов", value = setsValue, isReadOnly = isReadOnly, showIcon = false,
                        onValueChange = { onCustomParamChange("sets", it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text(
                    text = "Начать",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LabelTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(text = label, fontSize = 20.sp, modifier = Modifier.weight(2f))

        Spacer(modifier = Modifier.width(16.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(6.dp))
                .weight(4f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> LabelDropdownMenu(
    label: String,
    items: List<T>,
    selectedItem: T?,
    itemLabel: (T) -> String,
    onItemSelected: (T?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(text = label, fontSize = 20.sp, modifier = Modifier.weight(2f))

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
                .background(Color(0xFFEEEEEE), RoundedCornerShape(6.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedItem?.let { itemLabel(it) } ?: "Выбрать",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(item)) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDropdownMenu(
    items: List<TabataProfile>,
    selectedProfile: TabataProfile?,
    onItemSelected: (TabataProfile?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(ElementBackgroundWhite, RoundedCornerShape(6.dp))
            .clickable { expanded = true }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedProfile?.name ?: "Свой таймер",
                fontSize = 16.sp,
                color = Color.Black
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = Color.Black
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            DropdownMenuItem(
                text = { Text("Свой таймер", color = TextPrimary) },
                onClick = {
                    onItemSelected(null)
                    expanded = false
                }
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name, color = TextPrimary) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun EditableTabataRow(
    label: String,
    value: Int,
    isReadOnly: Boolean,
    showIcon: Boolean,
    onValueChange: (Int) -> Unit
) {
    val isTimeField = showIcon

    var singleText by remember { mutableStateOf("") }
    var singleFocused by remember { mutableStateOf(false) }

    var mText by remember { mutableStateOf("00") }
    var sText by remember { mutableStateOf("00") }
    var mFocused by remember { mutableStateOf(false) }
    var sFocused by remember { mutableStateOf(false) }

    LaunchedEffect(value, isReadOnly) {
        if (isTimeField) {
            val m = value / 60
            val s = value % 60
            if (isReadOnly || (!mFocused && !sFocused)) {
                mText = String.format("%02d", m)
                sText = String.format("%02d", s)
            }
        } else {
            if (isReadOnly || !singleFocused) {
                singleText = if (value == 0 && !isReadOnly) "" else value.toString()
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            color = TextPrimary,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showIcon) {
                Icon(
                    imageVector = Icons.Filled.InvertColors,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .size(width = if (isTimeField) 100.dp else 64.dp, height = 30.dp)
                    .background(ElementBackgroundWhite, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isTimeField) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = mText,
                            onValueChange = { newM ->
                                if (!isReadOnly) {
                                    val digits = newM.filter { it.isDigit() }
                                    if (digits.length <= 3) {
                                        mText = digits
                                        val m = digits.toIntOrNull() ?: 0
                                        val s = sText.toIntOrNull() ?: 0
                                        onValueChange(m * 60 + s)
                                    }
                                }
                            },
                            readOnly = isReadOnly,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = if (isReadOnly) TextPrimary else AccentPurple,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(36.dp)
                                .onFocusChanged { focusState ->
                                    mFocused = focusState.isFocused
                                    if (!focusState.isFocused && !isReadOnly) {
                                        val m = mText.toIntOrNull() ?: 0
                                        mText = String.format("%02d", m)
                                    }
                                }
                        )

                        Text(":", fontSize = 16.sp, color = if (isReadOnly) TextPrimary else AccentPurple)

                        BasicTextField(
                            value = sText,
                            onValueChange = { newS ->
                                if (!isReadOnly) {
                                    val digits = newS.filter { it.isDigit() }
                                    if (digits.length <= 2) {
                                        var num = digits.toIntOrNull() ?: 0
                                        if (num > 59) {
                                            num = 59
                                            sText = "59"
                                        } else {
                                            sText = digits
                                        }
                                        val m = mText.toIntOrNull() ?: 0
                                        onValueChange(m * 60 + num)
                                    }
                                }
                            },
                            readOnly = isReadOnly,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = if (isReadOnly) TextPrimary else AccentPurple,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(28.dp)
                                .onFocusChanged { focusState ->
                                    sFocused = focusState.isFocused
                                    if (!focusState.isFocused && !isReadOnly) {
                                        val s = sText.toIntOrNull() ?: 0
                                        sText = String.format("%02d", s)
                                    }
                                }
                        )
                    }
                } else {
                    BasicTextField(
                        value = singleText,
                        onValueChange = { newText ->
                            if (!isReadOnly) {
                                val digits = newText.filter { it.isDigit() }
                                if (digits.length <= 3) {
                                    singleText = digits
                                    onValueChange(digits.toIntOrNull() ?: 0)
                                }
                            }
                        },
                        readOnly = isReadOnly,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = if (isReadOnly) TextPrimary else AccentPurple,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { singleFocused = it.isFocused }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
fun WorkoutSettingsScreenContentPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            WorkoutSettingsScreenContent()
        }
    }
}
