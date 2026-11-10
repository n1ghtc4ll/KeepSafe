package com.example.cardioproject.workout.presentation.screen


import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.workout.presentation.model.MainScreenUiState
import com.example.cardioproject.workout.presentation.viewmodel.MainScreenViewModel
import com.example.cardioproject.workout.ui.components.HeartRateCard
import org.koin.androidx.compose.koinViewModel


@Composable
fun HeartRateMainScreen(
    onSettingsClick: () -> Unit = {},
    onAddMeasurementsClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {}
) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HeartRateMainScreenContent(
        state = state,
        onSettingsClick = onSettingsClick,
        onAddMeasurementsClick = onAddMeasurementsClick,
        onStartWorkoutClick = onStartWorkoutClick
    )
}

@Composable
fun HeartRateMainScreenContent(
    state: MainScreenUiState = MainScreenUiState(),
    onSettingsClick: () -> Unit = {},
    onAddMeasurementsClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BatteryIndicator(percent = state.deviceBattery)

            Text(
                text = if (state.isDeviceConnected)
                    "Подключено\n• ${state.deviceName ?: "Неизвестное устройство"}"
                else
                    "Нет подключения",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Настройки",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        HeartRateCard(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp),
            contentPadding = PaddingValues(start = 30.dp, top = 20.dp, end = 30.dp, bottom = 30.dp),
            heartRate = state.currentBpm,
            zones = state.hrZones
        )

        Button(
            onClick = onAddMeasurementsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Добавить новые замеры",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onStartWorkoutClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Начать тренировку",
                    modifier = Modifier.width(150.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BatteryIndicator(percent: Int?, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(22.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface,
                    RoundedCornerShape(3.dp)
                )
        )
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .width(3.dp)
                .height(8.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface,
                    RoundedCornerShape(1.dp)
                )
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = if (percent != null) "$percent%" else "-%",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
fun HeartRateMainScreenPreview() {
    KeepSafeProjectTheme {
        HeartRateMainScreenContent()
    }
}