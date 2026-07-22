package com.example.cardioproject.presentation

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
private val GraphLineColor = Color(0xFFE91E63)

// Цвета для создания новых тегов
private val TagColors = listOf(
    Color(0xFF009951), Color(0xFFC00F0C), Color(0xFFE6A000),
    Color(0xFF1976D2), Color(0xFF9C27B0), Color(0xFF607D8B)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsScreen(
    workout: WorkoutSession,
    onBackClick: () -> Unit = {},
    onWorkoutUpdated: (WorkoutSession) -> Unit = {}
) {
    // Инициализируем состояния значениями из переданной тренировки
    var workoutTitle by remember(workout.id) { mutableStateOf(workout.title) }
    var isEditingTitle by remember { mutableStateOf(false) }

    var currentTag by remember(workout.id) { mutableStateOf(workout.tag) }
    var showTagManager by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
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

            Column(modifier = Modifier.weight(1f)) {
                if (isEditingTitle) {
                    OutlinedTextField(
                        value = workoutTitle,
                        onValueChange = { workoutTitle = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                isEditingTitle = false
                                // Сохраняем новое название в глобальный список
                                onWorkoutUpdated(workout.copy(title = workoutTitle))
                            }) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Сохранить",
                                    tint = AccentPurple
                                )
                            }
                        }
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = workoutTitle,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        IconButton(
                            onClick = { isEditingTitle = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Редактировать",
                                modifier = Modifier.size(16.dp),
                                tint = TextGray
                            )
                        }
                    }
                }

                // Отображаем реальные данные из тренировки
                Text(
                    text = "${workout.date}, ${workout.time} • ${workout.duration}",
                    fontSize = 14.sp,
                    color = TextGray
                )
            }

            IconButton(onClick = { /* TODO: Логика экспорта в Excel */ }) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_save),
                    contentDescription = "Экспорт в Excel",
                    tint = AccentPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Тег тренировки:", fontSize = 16.sp, color = TextDark)
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .background(currentTag.color, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentTag.name, color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { showTagManager = true },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Изменить", fontSize = 12.sp)
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
                value = "145", // В будущем здесь будет расчет из данных
                unit = "уд/мин"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Макс. пульс",
                value = "172", // В будущем здесь будет расчет из данных
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
                value = "420", // Расчет на основе веса/пульса
                unit = "ккал"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Длительность",
                // Оставляем минуты от длительности для красоты
                value = workout.duration.split(":").getOrNull(1) ?: "00",
                unit = "мин"
            )
        }
    }

    if (showTagManager) {
        TagManagementDialog(
            currentTag = currentTag,
            onTagSelected = { newTag ->
                currentTag = newTag
                showTagManager = false
                // Сохраняем обновленный тег
                onWorkoutUpdated(workout.copy(tag = newTag))
            },
            onDismiss = { showTagManager = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagManagementDialog(
    currentTag: WorkoutTag,
    onTagSelected: (WorkoutTag) -> Unit,
    onDismiss: () -> Unit
) {
    var tags by remember { mutableStateOf(defaultTags.toList()) }
    var isAddingNew by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }
    var newTagColor by remember { mutableStateOf(TagColors[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Управление тегами") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isAddingNew) {
                    tags.forEach { tag ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = tag.id == currentTag.id,
                                    onClick = { onTagSelected(tag) }
                                )
                                Box(
                                    modifier = Modifier
                                        .background(tag.color, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(tag.name, color = Color.White, fontSize = 12.sp)
                                }
                            }
                            IconButton(
                                onClick = { tags = tags.filter { it.id != tag.id } },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Filled.Delete, "Удалить", tint = Color.Red)
                            }
                        }
                    }

                    TextButton(onClick = { isAddingNew = true }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Создать новый тег")
                    }
                } else {
                    OutlinedTextField(
                        value = newTagName,
                        onValueChange = { newTagName = it },
                        label = { Text("Название тега") },
                        singleLine = true
                    )

                    Text("Цвет:", fontSize = 14.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TagColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(color, RoundedCornerShape(16.dp))
                                    .padding(2.dp)
                                    .background(
                                        if (color == newTagColor) Color.White.copy(alpha = 0.5f) else Color.Transparent,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { newTagColor = color }
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { isAddingNew = false }) {
                            Text("Отмена")
                        }
                        Button(
                            onClick = {
                                if (newTagName.isNotBlank()) {
                                    val newTag = WorkoutTag(
                                        java.util.UUID.randomUUID().toString(),
                                        newTagName,
                                        newTagColor
                                    )
                                    tags = tags + newTag
                                    defaultTags.add(newTag)
                                    isAddingNew = false
                                    newTagName = ""
                                }
                            }
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!isAddingNew) {
                TextButton(onClick = onDismiss) {
                    Text("Закрыть")
                }
            }
        }
    )
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

        val path = Path()
        path.moveTo(0f, height * 0.8f)
        path.lineTo(width * 0.1f, height * 0.7f)
        path.lineTo(width * 0.2f, height * 0.3f)
        path.lineTo(width * 0.3f, height * 0.4f)
        path.lineTo(width * 0.4f, height * 0.35f)
        path.lineTo(width * 0.5f, height * 0.2f)
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
    val sampleWorkout = WorkoutSession(
        id = "1",
        title = "Утренняя пробежка",
        date = "15.07.2023",
        time = "08:00",
        duration = "00:45:00",
        tag = defaultTags[0]
    )
    WorkoutDetailsScreen(workout = sampleWorkout)
}