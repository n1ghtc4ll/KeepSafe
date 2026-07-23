package com.example.cardioproject.settings.presentation.screen

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.settings.presentation.HeartRateZone

// ---------- Цвета из твоей стилистики ----------
private val SettingsBackground = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF000000)
private val CardBorder = Color(0xFFCAC4D0)
private val AccentPurple = Color(0xFF6750A4)
private val InputBackgroundGray = Color(0xFFEEEEEE)
private val TabataBackground = Color(0xFFF3EDF7)
private val ElementBackgroundWhite = Color(0xFFFFFFFF)

// Стандартная Material-палитра для выбора цвета фазы
private val MaterialColorsPalette = listOf(
    0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7,
    0xFF3F51B5, 0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4,
    0xFF009688, 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39,
    0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722,
    0xFF795548, 0xFF9E9E9E, 0xFF607D8B
)

// ---------- Стэйт экрана ----------
data class ProfileBuilderUiState(
    val id: String? = null,
    val name: String = "",
    val setsCount: Int = 1,
    val repsCount: Int = 8,

    val warmUpTimeSec: Int = 30,
    val warmUpColorHex: Long = 0xFFFF9800, // Orange

    val workoutTimeSec: Int = 45,
    val workoutColorHex: Long = 0xFF4CAF50, // Green

    val relaxTimeSec: Int = 15,
    val relaxColorHex: Long = 0xFF2196F3, // Blue

    val breakTimeSec: Int = 60,
    val breakColorHex: Long = 0xFF9C27B0, // Purple

    val coolDownTimeSec: Int = 120,
    val coolDownColorHex: Long = 0xFF00BCD4  // Cyan
)

enum class PhaseType { WARM_UP, WORKOUT, RELAX, BREAK, COOL_DOWN }

// ---------- Экран ----------
@Composable
fun ProfileBuilderScreen(
    state: ProfileBuilderUiState = ProfileBuilderUiState(),
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onParamChange: (String, Int) -> Unit = { _, _ -> },
    onPhaseColorChange: (PhaseType, Long) -> Unit = { _, _ -> }
) {
    var phaseToEditColor by remember { mutableStateOf<PhaseType?>(null) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SettingsBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
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
                    text = if (state.id == null) "Создание профиля" else "Редактирование",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Название
            BuilderLabelTextField(
                label = "Название",
                value = state.name,
                onValueChange = onNameChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Карточка настроек
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

                EditablePhaseRow(
                    label = "Пауза перед\nтренировкой",
                    value = state.warmUpTimeSec,
                    isTimeField = true,
                    colorHex = state.warmUpColorHex,
                    onColorClick = { phaseToEditColor = PhaseType.WARM_UP },
                    onValueChange = { onParamChange("warmUp", it) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditablePhaseRow(
                    label = "Кол-во интервалов",
                    value = state.repsCount,
                    isTimeField = false,
                    colorHex = null,
                    onColorClick = null,
                    onValueChange = { onParamChange("reps", it) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditablePhaseRow(
                    label = "Упражнение",
                    value = state.workoutTimeSec,
                    isTimeField = true,
                    colorHex = state.workoutColorHex,
                    onColorClick = { phaseToEditColor = PhaseType.WORKOUT },
                    onValueChange = { onParamChange("workout", it) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditablePhaseRow(
                    label = "Отдых",
                    value = state.relaxTimeSec,
                    isTimeField = true,
                    colorHex = state.relaxColorHex,
                    onColorClick = { phaseToEditColor = PhaseType.RELAX },
                    onValueChange = { onParamChange("relax", it) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditablePhaseRow(
                    label = "Перезарядка",
                    value = state.breakTimeSec,
                    isTimeField = true,
                    colorHex = state.breakColorHex,
                    onColorClick = { phaseToEditColor = PhaseType.BREAK },
                    onValueChange = { onParamChange("break", it) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditablePhaseRow(
                    label = "Заминка",
                    value = state.coolDownTimeSec,
                    isTimeField = true,
                    colorHex = state.coolDownColorHex,
                    onColorClick = { phaseToEditColor = PhaseType.COOL_DOWN },
                    onValueChange = { onParamChange("coolDown", it) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditablePhaseRow(
                    label = "Кол-во кругов",
                    value = state.setsCount,
                    isTimeField = false,
                    colorHex = null,
                    onColorClick = null,
                    onValueChange = { onParamChange("sets", it) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text(
                    text = "Сохранить",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Диалог выбора цвета
    phaseToEditColor?.let { type ->
        val currentColor = when (type) {
            PhaseType.WARM_UP -> state.warmUpColorHex
            PhaseType.WORKOUT -> state.workoutColorHex
            PhaseType.RELAX -> state.relaxColorHex
            PhaseType.BREAK -> state.breakColorHex
            PhaseType.COOL_DOWN -> state.coolDownColorHex
        }

        ColorPickerDialog(
            initialColor = currentColor,
            onColorSelected = { selectedColorHex ->
                onPhaseColorChange(type, selectedColorHex)
                phaseToEditColor = null
            },
            onDismiss = { phaseToEditColor = null }
        )
    }
}

// ---------- Компоненты ----------

@Composable
private fun BuilderLabelTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 20.sp, modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.width(16.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .background(color = InputBackgroundGray, shape = RoundedCornerShape(6.dp))
                .weight(4f)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            textStyle = TextStyle(fontSize = 16.sp, color = TextPrimary),
            singleLine = true
        )
    }
}

@Composable
private fun EditablePhaseRow(
    label: String,
    value: Int,
    isTimeField: Boolean,
    colorHex: Long?,
    onColorClick: (() -> Unit)?,
    onValueChange: (Int) -> Unit
) {
    var singleText by remember { mutableStateOf("") }
    var singleFocused by remember { mutableStateOf(false) }

    var mText by remember { mutableStateOf("00") }
    var sText by remember { mutableStateOf("00") }
    var mFocused by remember { mutableStateOf(false) }
    var sFocused by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        if (isTimeField) {
            val m = value / 60
            val s = value % 60
            if (!mFocused && !sFocused) {
                mText = String.format("%02d", m)
                sText = String.format("%02d", s)
            }
        } else {
            if (!singleFocused) {
                singleText = if (value == 0) "" else value.toString()
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
            if (colorHex != null && onColorClick != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .clickable(onClick = onColorClick)
                )
                Spacer(modifier = Modifier.width(12.dp))
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
                                val digits = newM.filter { it.isDigit() }
                                if (digits.length <= 3) {
                                    mText = digits
                                    val m = digits.toIntOrNull() ?: 0
                                    val s = sText.toIntOrNull() ?: 0
                                    onValueChange(m * 60 + s)
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = AccentPurple,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(36.dp)
                                .onFocusChanged { focusState ->
                                    mFocused = focusState.isFocused
                                    if (!focusState.isFocused) {
                                        val m = mText.toIntOrNull() ?: 0
                                        mText = String.format("%02d", m)
                                    }
                                }
                        )

                        Text(":", fontSize = 16.sp, color = AccentPurple)

                        BasicTextField(
                            value = sText,
                            onValueChange = { newS ->
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
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = AccentPurple,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(28.dp)
                                .onFocusChanged { focusState ->
                                    sFocused = focusState.isFocused
                                    if (!focusState.isFocused) {
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
                            val digits = newText.filter { it.isDigit() }
                            if (digits.length <= 3) {
                                singleText = digits
                                onValueChange(digits.toIntOrNull() ?: 0)
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = AccentPurple,
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

@Composable
private fun ColorPickerDialog(
    initialColor: Long,
    onColorSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(initialColor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Выберите цвет фазы", color = TextPrimary) },
        containerColor = SettingsBackground,
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(MaterialColorsPalette) { colorValue ->
                    val isSelected = selectedColor == colorValue
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(colorValue))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) TextPrimary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = colorValue }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onColorSelected(selectedColor) }) {
                Text("Выбрать", color = AccentPurple)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = TextPrimary)
            }
        }
    )
}

// ---------- Preview ----------
@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
fun ProfileBuilderScreenPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ProfileBuilderScreen()
        }
    }
}