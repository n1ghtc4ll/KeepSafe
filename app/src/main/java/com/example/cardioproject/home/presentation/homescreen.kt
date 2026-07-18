package com.example.cardioproject.home.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.workout.presentation.screen.WorkoutSettingsScreen

// Палитра — только те значения, которые нужны Главному экрану
private val ScreenBackground = Color(0xFFF3F3F3)
private val TextPrimary = Color(0xFF000000)
private val TextDark = Color(0xFF1D1B20)
private val BatteryColor = Color(0xFF1C1B1F)
private val CardBorder = Color(0xFFCAC4D0)
private val CardGradientEnd = Color(0xFF4CAF50)
private val DividerColor = Color(0xFF79747E)
private val MutedMauve = Color(0xFF625B71)
private val AccentPurple = Color(0xFF6750A4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Используем встроенную тему Material3
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // Простейшая навигация между двумя экранами
    var currentScreen by remember { mutableStateOf("MAIN") }

    when (currentScreen) {
        "MAIN" -> HeartRateMainScreen(
            onStartWorkoutClick = { currentScreen = "SETTINGS" }
        )

        "SETTINGS" -> WorkoutSettingsScreen(
            onBackClick = { currentScreen = "MAIN" }
        )
    }
}

@Composable
fun HeartRateMainScreen(
    heartRate: Int = 190,
    deviceName: String = "Coospo HW9",
    batteryPercent: Int = 31,
    isConnected: Boolean = true,
    onSettingsClick: () -> Unit = {},
    onAddMeasurementsClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        // Батарея · статус подключения · настройки
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BatteryIndicator(percent = batteryPercent)

            Text(
                text = if (isConnected) "Подключено\n• $deviceName" else "Нет подключения",
                color = TextPrimary,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Настройки",
                    tint = TextDark,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Карточка с текущим пульсом
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        0f to Color.White,
                        0.35f to Color.White,
                        1f to CardGradientEnd
                    )
                )
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                .padding(start = 30.dp, top = 20.dp, end = 30.dp, bottom = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$heartRate",
                    fontSize = 180.sp,
                    lineHeight = 218.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = DividerColor
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "уд/мин",
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary
                )
            }
        }

        // Кнопка добавления замеров
        Button(
            onClick = onAddMeasurementsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(69.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MutedMauve),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Добавить новые замеры",
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 29.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Круглая кнопка старта тренировки
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(AccentPurple)
                    .clickable(onClick = onStartWorkoutClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Начать тренировку",
                    modifier = Modifier.width(150.dp),
                    color = Color.White,
                    fontSize = 26.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BatteryIndicator(percent: Int, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(22.dp)
                .background(BatteryColor, RoundedCornerShape(3.dp))
        )
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .width(3.dp)
                .height(8.dp)
                .background(BatteryColor, RoundedCornerShape(1.dp))
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "$percent%",
            color = TextPrimary,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
fun HeartRateMainScreenPreview() {
    MaterialTheme {
        HeartRateMainScreen()
    }
}