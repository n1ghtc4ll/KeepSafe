package com.example.cardioproject.workout.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.core.ui.theme.spacing
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.workout.domain.model.WorkoutPhase
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.model.ActiveWorkoutUiState
import com.example.cardioproject.workout.presentation.util.formatAsMinutesSeconds
import com.example.cardioproject.workout.presentation.util.getColorForPhase
import com.example.cardioproject.workout.presentation.util.totalReps
import com.example.cardioproject.workout.presentation.util.totalSets
import com.example.cardioproject.workout.presentation.viewmodel.ActiveWorkoutViewModel
import com.example.cardioproject.workout.ui.components.HeartRateCard
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun WorkoutScreen(
    settings: WorkoutSettings,
    viewModel: ActiveWorkoutViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val dynamicPhaseColor = settings.tabataSetup.getColorForPhase(uiState.currentPhase)

    LaunchedEffect(settings) {
        viewModel.startWorkout(settings)
    }

    LaunchedEffect(uiState.currentPhase) {
        if (uiState.currentPhase == WorkoutPhase.FINISHED) {
            onFinish()
        }
    }

    WorkoutScreenContent(
        uiState = uiState,
        trainingName = settings.title,
        isTabataEnabled = settings.isTabataEnabled,
        tabataThemeColor = dynamicPhaseColor,
        setRepCount = Pair(settings.tabataSetup.totalSets, settings.tabataSetup.totalReps),
        onBackClick = {
            viewModel.stopWorkout()
        },
        onStopClick = {
            viewModel.stopWorkout()
        },
        onPauseClick = {

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreenContent(
    uiState: ActiveWorkoutUiState,
    trainingName: String,
    isTabataEnabled: Boolean,
    tabataThemeColor: Color,
    setRepCount: Pair<Int, Int>,
    onBackClick: () -> Unit = {},
    onStopClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trainingName,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        Text(
                            text = uiState.totalElapsedFormatted,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Назад"
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            HeartRateCard(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                heartRate = uiState.currentHeartRate,
                zones = uiState.hrZones
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onStopClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Стоп",
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Button(
                    onClick = onPauseClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Пауза",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            if (isTabataEnabled) {
                TabataTimer(
                    modifier = Modifier.weight(1f),
                    formattedTimeRemaining = uiState.tabataRemainingFormatted,
                    phase = uiState.currentPhase,
                    setInfo = Pair(uiState.currentSet, setRepCount.first),
                    repInfo = Pair(uiState.currentRep, setRepCount.second),
                    themeColor = tabataThemeColor
                )
            }

            HeartRateChartWithZones(
                heartRateHistory = uiState.heartRateHistory,
                zones = uiState.hrZones,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun TabataTimer(
    modifier: Modifier = Modifier,
    formattedTimeRemaining: String,
    phase: WorkoutPhase,
    setInfo: Pair<Int, Int>,
    repInfo: Pair<Int, Int>,
    themeColor: Color = Color(0xFF6BBA62)
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color.White,
            themeColor
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(gradientBrush, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formattedTimeRemaining,
            fontSize = 110.sp,
            maxLines = 1
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Сет: $setInfo",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Цикл: $repInfo",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = phase.phaseName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Right,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HeartRateChartWithZones(
    heartRateHistory: List<Int>,
    zones: List<HeartRateZone>,
    modifier: Modifier = Modifier
) {
    val minBpm = zones.minOfOrNull { it.lowerBpm }?.toFloat() ?: 40f
    val maxBpm = zones.maxOfOrNull { it.upperBpm }?.toFloat() ?: 200f
    val totalRange = (maxBpm - minBpm).coerceAtLeast(1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
    ) {
        // Основная область: Шкала Y слева + сам графический Canvas справа
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Шкала Y слева (фиксированной ширины, текст выровнен по правому краю)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${maxBpm.toInt()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${((maxBpm + minBpm) / 2).toInt()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${minBpm.toInt()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Сам график внутри Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // СЛОЙ 1: Зоны и сетка
                Canvas(modifier = Modifier.matchParentSize()) {
                    val heightPerBpm = size.height / totalRange

                    zones.forEach { zone ->
                        val topY = size.height - ((zone.upperBpm.toFloat() - minBpm) * heightPerBpm)
                        val bottomY = size.height - ((zone.lowerBpm.toFloat() - minBpm) * heightPerBpm)

                        val zoneColor = when (zone.label) {
                            "Зона 1" -> Color(0xFF64B5F6).copy(alpha = 0.25f)
                            "Зона 2" -> Color(0xFF81C784).copy(alpha = 0.25f)
                            "Зона 3" -> Color(0xFFFFB74D).copy(alpha = 0.25f)
                            else -> Color(0xFFE57373).copy(alpha = 0.25f)
                        }

                        drawRect(
                            color = zoneColor,
                            topLeft = Offset(0f, topY.coerceIn(0f, size.height)),
                            size = Size(size.width, (bottomY - topY).coerceAtLeast(2f))
                        )
                    }

                    // Горизонтальные линии сетки
                    val steps = 4
                    for (i in 0..steps) {
                        val y = size.height - (i * (size.height / steps))
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.7f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }

                // СЛОЙ 2: Линия пульса
                Canvas(modifier = Modifier.matchParentSize()) {
                    if (heartRateHistory.size < 2) return@Canvas

                    val widthStep = size.width / (heartRateHistory.size - 1).coerceAtLeast(1)
                    val heightPerBpm = size.height / totalRange

                    val path = Path().apply {
                        heartRateHistory.forEachIndexed { index, bpm ->
                            val x = index * widthStep
                            val y = size.height - ((bpm.toFloat() - minBpm) * heightPerBpm)
                            if (index == 0) moveTo(x, y) else lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color(0xFF7C6BFF),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Шкала X снизу (смещена вправо на ширину левой шкалы, чтобы ровно совпадать с началом графика)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp), // Отступ равен ширине колонки шкалы Y
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val totalSeconds = heartRateHistory.size
            Text(
                text = "0с",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
            if (totalSeconds > 5) {
                Text(
                    text = "${totalSeconds / 2}с",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${totalSeconds}с",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 37)
@Composable
fun WorkoutScreenContentPreview() {
    KeepSafeProjectTheme {
        WorkoutScreenContent(
            uiState = ActiveWorkoutUiState(),
            trainingName = "Название тренировки",
            isTabataEnabled = true,
            tabataThemeColor = Color(0xFF6BBA62),
            setRepCount = Pair(1, 1)
        )
    }
}
