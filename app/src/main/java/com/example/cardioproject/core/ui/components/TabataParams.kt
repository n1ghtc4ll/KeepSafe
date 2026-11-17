package com.example.cardioproject.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.core.ui.theme.spacing
import com.example.cardioproject.settings.presentation.model.PhaseType
import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.presentation.util.breakColorHex
import com.example.cardioproject.workout.presentation.util.breakTimeSec
import com.example.cardioproject.workout.presentation.util.coolDownColorHex
import com.example.cardioproject.workout.presentation.util.coolDownTimeSec
import com.example.cardioproject.workout.presentation.util.relaxColorHex
import com.example.cardioproject.workout.presentation.util.relaxTimeSec
import com.example.cardioproject.workout.presentation.util.totalReps
import com.example.cardioproject.workout.presentation.util.totalSets
import com.example.cardioproject.workout.presentation.util.warmUpColorHex
import com.example.cardioproject.workout.presentation.util.warmUpTimeSec
import com.example.cardioproject.workout.presentation.util.workoutColorHex
import com.example.cardioproject.workout.presentation.util.workoutTimeSec

@Composable
fun TabataParams(
    tabataSetup: TabataSetup?,
    availableProfiles: List<TabataProfile>? = null,
    onCustomParamChange: (String, Int) -> Unit,
    onCustomParamColorChange: (PhaseType, Long) -> Unit,
    onProfileSelect: (TabataProfile?) -> Unit = {  }
) {
    val currentProfile = (tabataSetup as? TabataSetup.Preset)?.profile

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(16.dp)
            )
            .padding(MaterialTheme.spacing.medium)
    ) {
        Text(
            text = "Параметры таймера",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider(
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.small,
                bottom = MaterialTheme.spacing.medium
            ),
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )

        if (availableProfiles != null) {
            Text(
                text = "Готовые профили",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            ProfileDropdownMenu(
                items = availableProfiles,
                selectedProfile = currentProfile,
                onItemSelected = onProfileSelect
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        TabataArgs(
            tabataSetup = tabataSetup,
            onCustomParamChange = onCustomParamChange,
            onCustomParamColorChange = onCustomParamColorChange
        )
    }
}

@Composable
fun TabataArgs(
    tabataSetup: TabataSetup?,
    onCustomParamChange: (String, Int) -> Unit,
    onCustomParamColorChange: (PhaseType, Long) -> Unit
) {
    val isReadOnly = tabataSetup is TabataSetup.Preset

    val warmUpValue = tabataSetup?.warmUpTimeSec ?: 0
    val repsValue = tabataSetup?.totalReps ?: 1
    val workoutValue = tabataSetup?.workoutTimeSec ?: 0
    val relaxValue = tabataSetup?.relaxTimeSec ?: 0
    val breakValue = tabataSetup?.breakTimeSec ?: 0
    val coolDownValue = tabataSetup?.coolDownTimeSec ?: 0
    val setsValue = tabataSetup?.totalSets ?: 1

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumSmall)
    ) {
        EditableTabataRow(
            label = "Пауза перед\nтренировкой",
            value = warmUpValue,
            isReadOnly = isReadOnly,
            isTimeField = true,
            colorHex = tabataSetup?.warmUpColorHex,
            onValueChange = { onCustomParamChange("warmUp", it) },
            onColorChange = { onCustomParamColorChange(PhaseType.WARM_UP, it) }
        )

        EditableTabataRow(
            label = "Кол-во интервалов",
            value = repsValue,
            isReadOnly = isReadOnly,
            isTimeField = false,
            colorHex = null,
            onValueChange = { onCustomParamChange("reps", it) },
            onColorChange = {  }
        )

        EditableTabataRow(
            label = "Упражнение",
            value = workoutValue,
            isReadOnly = isReadOnly,
            isTimeField = true,
            colorHex = tabataSetup?.workoutColorHex,
            onValueChange = { onCustomParamChange("workout", it) },
            onColorChange = { onCustomParamColorChange(PhaseType.WORKOUT, it) }
        )

        EditableTabataRow(
            label = "Отдых",
            value = relaxValue,
            isReadOnly = isReadOnly,
            isTimeField = true,
            colorHex = tabataSetup?.relaxColorHex,
            onValueChange = { onCustomParamChange("relax", it) },
            onColorChange = { onCustomParamColorChange(PhaseType.RELAX, it) }
        )

        EditableTabataRow(
            label = "Перезарядка",
            value = breakValue,
            isReadOnly = isReadOnly,
            isTimeField = true,
            colorHex = tabataSetup?.breakColorHex,
            onValueChange = { onCustomParamChange("break", it) },
            onColorChange = { onCustomParamColorChange(PhaseType.BREAK, it) }
        )

        EditableTabataRow(
            label = "Заминка",
            value = coolDownValue,
            isReadOnly = isReadOnly,
            isTimeField = true,
            colorHex = tabataSetup?.coolDownColorHex,
            onValueChange = { onCustomParamChange("coolDown", it) },
            onColorChange = { onCustomParamColorChange(PhaseType.COOL_DOWN, it) }
        )

        EditableTabataRow(
            label = "Кол-во кругов",
            value = setsValue,
            isReadOnly = isReadOnly,
            isTimeField = false,
            colorHex = null,
            onValueChange = { onCustomParamChange("sets", it) },
            onColorChange = {  }
        )
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
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(6.dp)
            )
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
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
        ) {
            DropdownMenuItem(
                text = { Text("Свой таймер", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onItemSelected(null)
                    expanded = false
                }
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name, color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabataParamsPreview() {
    KeepSafeProjectTheme {
        TabataParams(
            tabataSetup = TabataSetup.Custom(),
            availableProfiles = listOf(),
            onCustomParamChange = { _, _ -> },
            onCustomParamColorChange = { _, _ -> },
            onProfileSelect = {  }
        )
    }
}