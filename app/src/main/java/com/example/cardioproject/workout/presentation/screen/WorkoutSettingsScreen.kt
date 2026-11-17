package com.example.cardioproject.workout.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.core.ui.components.LabelDropdownMenu
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.core.ui.theme.spacing
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.model.WorkoutSettingsUiState
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.example.cardioproject.core.ui.components.LabelTextField
import com.example.cardioproject.core.ui.components.TabataParams
import com.example.cardioproject.settings.presentation.model.PhaseType


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
        onCustomParamChange = viewModel::onCustomParamChanged,
        onCustomPhaseColorChange = viewModel::onCustomPhaseColorChanged,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSettingsScreenContent(
    uiState: WorkoutSettingsUiState = WorkoutSettingsUiState(),
    onBackClick: () -> Unit = {  },
    onStartClick: () -> Unit = {  },
    onTitleChange: (String) -> Unit = {  },
    onTagSelect: (Tag?) -> Unit = {  },
    onTabataToggle: (Boolean) -> Unit = {  },
    onProfileSelect: (TabataProfile?) -> Unit = {  },
    onCustomParamChange: (String, Int) -> Unit = { _, _ -> },
    onCustomPhaseColorChange: (PhaseType, Long) -> Unit = { _, _ -> },
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Text(
                    text = "Настройка тренировки",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                LabelTextField(
                    label = "Название",
                    value = uiState.title,
                    onValueChange = onTitleChange
                )

                LabelDropdownMenu(
                    label = "Тег",
                    items = uiState.availableTags,
                    selectedItem = uiState.selectedTag,
                    itemLabel = { it.name },
                    onItemSelected = { onTagSelect(it) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Табата-таймер",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Switch(
                        checked = uiState.isTabataEnabled,
                        onCheckedChange = onTabataToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                if (uiState.isTabataEnabled) {
                    val setup = uiState.tabataSetup

                    TabataParams(
                        tabataSetup = setup,
                        availableProfiles = uiState.availableProfiles,
                        onCustomParamChange = onCustomParamChange,
                        onCustomParamColorChange = onCustomPhaseColorChange,
                        onProfileSelect = onProfileSelect
                    )
                }
            }

            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(vertical = MaterialTheme.spacing.small)
            ) {
                Text(
                    text = "Начать",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
fun WorkoutSettingsScreenContentPreview() {
    KeepSafeProjectTheme {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            WorkoutSettingsScreenContent()
        }
    }
}
