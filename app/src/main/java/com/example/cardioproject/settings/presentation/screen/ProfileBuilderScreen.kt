package com.example.cardioproject.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardioproject.core.ui.theme.TabataPhaseColorsPalette
import com.example.cardioproject.core.ui.theme.tabataContainer
import com.example.cardioproject.core.ui.components.ColorPickerDialog
import com.example.cardioproject.settings.presentation.model.PhaseType
import com.example.cardioproject.settings.presentation.model.ProfileBuilderUiState
import com.example.cardioproject.settings.presentation.ui.components.BuilderLabelTextField
import com.example.cardioproject.settings.presentation.ui.components.EditablePhaseRow

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
        color = MaterialTheme.colorScheme.background
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
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = if (state.id == null) "Создание профиля" else "Редактирование",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
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
                    .background(MaterialTheme.colorScheme.tabataContainer, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Параметры таймера",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Сохранить",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
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
            colorsPalette = TabataPhaseColorsPalette,
            onColorSelected = { selectedColorHex ->
                onPhaseColorChange(type, selectedColorHex)
                phaseToEditColor = null
            },
            onDismiss = { phaseToEditColor = null }
        )
    }
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