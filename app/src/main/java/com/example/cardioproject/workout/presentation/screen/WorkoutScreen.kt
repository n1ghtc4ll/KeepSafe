package com.example.cardioproject.workout.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.domain.model.WorkoutPhase
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.model.ActiveWorkoutUiState
import com.example.cardioproject.workout.presentation.util.getColorForPhase
import com.example.cardioproject.workout.presentation.util.totalReps
import com.example.cardioproject.workout.presentation.util.totalSets
import com.example.cardioproject.workout.presentation.viewmodel.ActiveWorkoutViewModel
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

    val formattedTime = uiState.timeRemainingSec.let { seconds ->
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        String.format("%02d:%02d", minutes, remainingSeconds)
    }

    WorkoutScreenContent(
        uiState = uiState,
        time = formattedTime,
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
    time: String,
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
                    Text(
                        text = trainingName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
            )
        },

        containerColor = Color(0xFFF3F3F3)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            HeartRateCard(
                heartRate = uiState.currentHeartRate,
                modifier = Modifier.weight(1f)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB73225)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Стоп", fontSize = 18.sp, color = Color.White)
                }

                Button(
                    onClick = onPauseClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B52A3)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Пауза", fontSize = 18.sp, color = Color.White)
                }
            }

            if (isTabataEnabled) {
                TabataTimer(
                    modifier = Modifier.weight(1f),
                    timeRemaining = uiState.timeRemainingSec,
                    phase = uiState.currentPhase,
                    setInfo = Pair(uiState.currentSet, setRepCount.first),
                    repInfo = Pair(uiState.currentRep, setRepCount.second),
                    themeColor = tabataThemeColor
                )
            }

            HeartRateChartWithZones(
                heartRateHistory = uiState.heartRateHistory,
                zones = uiState.hrZones,
                modifier = Modifier.padding(vertical = 8.dp).weight(1f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = time,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun HeartRateCard(heartRate: Int, modifier: Modifier = Modifier) {

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFF6BBA62)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(gradientBrush, RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = heartRate.toString(),
            fontSize = 120.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            lineHeight = 110.sp
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 4.dp),
            thickness = 1.dp,
            color = Color(0x40000000)
        )

        Text(
            text = "уд/мин",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ZonesGraphCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "График с зонами",
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
fun TabataTimer(
    modifier: Modifier = Modifier,
    timeRemaining: Int = 0,
    phase: WorkoutPhase = WorkoutPhase.WARMUP,
    setInfo: Pair<Int, Int>,
    repInfo: Pair<Int, Int>,
    themeColor: Color = Color(0xFF6BBA62)
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            themeColor
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(gradientBrush, RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = convertSecToTimeString(timeRemaining), fontSize = 110.sp, maxLines = 1)

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(text = "Сет: $setInfo", fontSize = 28.sp)
                Text(text = "Цикл: $repInfo", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = phase.phaseName, fontSize = 28.sp)
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
            .background(Color(0xFFF5F5F7), RoundedCornerShape(12.dp))
            .padding(12.dp)
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
                    .width(36.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "${maxBpm.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text(text = "${((maxBpm + minBpm) / 2).toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text(text = "${minBpm.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
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
            Text(text = "0с", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            if (totalSeconds > 5) {
                Text(text = "${totalSeconds / 2}с", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                Text(text = "${totalSeconds}с", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            }
        }
    }
}

fun convertSecToTimeString(timeSeconds: Int): String {
    val minutes = timeSeconds / 60
    val seconds = timeSeconds % 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}


@Preview(showBackground = true, apiLevel = 36)
@Composable
fun WorkoutScreenContentPreview() {
    WorkoutScreenContent(
        ActiveWorkoutUiState(),
        "00:00:00",
        "Название тренировки",
        false,
        Color(0xFF6BBA62),
        Pair(1, 1)
    )
}
