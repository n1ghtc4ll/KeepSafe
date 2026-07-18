package com.example.cardioproject.workout.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    heartRate: Int,
    time: String,
    trainingName: String
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
                    IconButton(onClick = { TODO() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { TODO() }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться")
                    }
                },
                /*colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF3F3F3) // Цвет фона шапки из дизайна
                )*/
            )
        },
/*        bottomBar = {
            TODO()
        },*/
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
                heartRate = heartRate,
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { TODO() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB73225)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Стоп", fontSize = 18.sp, color = Color.White)
                }

                Button(
                    onClick = { TODO() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B52A3)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Пауза", fontSize = 18.sp, color = Color.White)
                }
            }

            ZonesGraphCard(
                modifier = Modifier.weight(1f)
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
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = heartRate.toString(),
            fontSize = 110.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            lineHeight = 110.sp
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp),
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
        // Переданный modifier ставим в самое начало
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

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun WorkoutScreenPreview() {
    WorkoutScreen(190, "00:00:00", "Название тренировки")
}
