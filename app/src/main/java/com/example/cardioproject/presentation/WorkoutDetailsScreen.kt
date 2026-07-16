package com.example.cardioproject.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundColor = Color(0xFFF4F4F6)
private val CardBackground = Color(0xFFFFFFFF)
private val AccentPurple = Color(0xFF6750A4)
private val TextDark = Color(0xFF1D1B20)
private val TextGray = Color(0xFF79747E)
private val GraphLineColor = Color(0xFFE91E63) // Розово-красный для графика пульса

@Composable
fun WorkoutDetailsScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Шапка
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextDark
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Утренняя пробежка",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(text = "Сегодня, 08:00 - 08:45", fontSize = 14.sp, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "График пульса",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(CardBackground, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            HeartRateGraph()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Статистика",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Средний пульс",
                value = "145",
                unit = "уд/мин"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Макс. пульс",
                value = "172",
                unit = "уд/мин"
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Сжигание",
                value = "420",
                unit = "ккал"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Длительность",
                value = "45",
                unit = "мин"
            )
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, unit: String) {
    Column(
        modifier = modifier
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = title, fontSize = 14.sp, color = TextGray)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AccentPurple)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = unit,
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun HeartRateGraph() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Рисуем направляющие линии (горизонтальные)
        val lineCount = 4
        for (i in 0..lineCount) {
            val y = height * (i.toFloat() / lineCount)
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(width, y),
                strokeWidth = 1f
            )
        }

        // Рисуем сам график (имитация данных пульса)
        val path = Path()
        path.moveTo(0f, height * 0.8f)
        path.lineTo(width * 0.1f, height * 0.7f)
        path.lineTo(width * 0.2f, height * 0.3f) // Пик
        path.lineTo(width * 0.3f, height * 0.4f)
        path.lineTo(width * 0.4f, height * 0.35f)
        path.lineTo(width * 0.5f, height * 0.2f) // Максимум
        path.lineTo(width * 0.6f, height * 0.5f)
        path.lineTo(width * 0.7f, height * 0.45f)
        path.lineTo(width * 0.8f, height * 0.6f)
        path.lineTo(width * 0.9f, height * 0.55f)
        path.lineTo(width, height * 0.75f)

        drawPath(
            path = path,
            color = GraphLineColor,
            style = Stroke(
                width = 4.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailsPreview() {
    WorkoutDetailsScreen()
}