package com.example.cardioproject.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundF3 = Color(0xFFF3F3F3)
private val ButtonPurple = Color(0xFF6750A4)
private val ButtonGray = Color(0xFF625B71)
private val TextBlack = Color(0xFF000000)
private val CardWhite = Color(0xFFFFFFFF)

// Обновленная модель данных под новый дизайн
data class WorkoutSession(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val duration: String,
    val type: String, // Для фильтрации
    var isSelected: Boolean = false // Состояние галочки
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    onBackClick: () -> Unit = {},
    onWorkoutClick: (String) -> Unit = {}
) {
    // Демонстрационные данные
    val initialWorkouts = remember {
        listOf(
            WorkoutSession(
                "1",
                "Интервальная тренировка",
                "12.07.2023",
                "08:00",
                "00:45:00",
                "Интервальная"
            ),
            WorkoutSession("2", "Силовая тренировка", "10.07.2023", "18:30", "01:20:00", "Силовая"),
            WorkoutSession("3", "Легкая пробежка", "08.07.2023", "07:00", "00:30:00", "Кардио"),
            WorkoutSession("4", "Табата интенсив", "05.07.2023", "19:00", "00:20:00", "Табата")
        )
    }

    // Состояния списка и фильтров
    var workouts by remember { mutableStateOf(initialWorkouts) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf("Все") }

    // Логика фильтрации
    val filteredWorkouts = workouts.filter { session ->
        val matchesSearch = session.title.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedTypeFilter == "Все" || session.type == selectedTypeFilter
        matchesSearch && matchesType
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundF3)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Шапка
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(49.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = TextBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "История тренировок",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextBlack
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Параметры фильтрации (Кнопки)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка "Посмотреть"
                Button(
                    onClick = {
                        // Находим первую выбранную тренировку и открываем её
                        val selected = workouts.firstOrNull { it.isSelected }
                        if (selected != null) {
                            onWorkoutClick(selected.id)
                        }
                    },
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(text = "Посмотреть", fontSize = 16.sp, color = Color.White)
                }

                // Правая группа кнопок (Фильтр и Период)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Иконка фильтра (открывает диалог поиска)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(ButtonGray, RoundedCornerShape(16.dp))
                            .clickable { showFilterDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        // Используем стандартную иконку настроек/фильтра как заглушку,
                        // так как кастомная иконка фильтра требует вектора
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                            contentDescription = "Фильтр",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Кнопка Период
                    Button(
                        onClick = { /* TODO: Выбор периода */ },
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonGray),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "Период", fontSize = 16.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Содержимое списка
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp) // Gap: 5px из макета
            ) {
                items(filteredWorkouts) { session ->
                    WorkoutItem(
                        session = session,
                        onCheckedChange = { isChecked ->
                            // Обновляем состояние галочки в списке
                            workouts = workouts.map {
                                if (it.id == session.id) it.copy(isSelected = isChecked) else it
                            }
                        }
                    )
                }
            }
        }
    }

    // Диалоговое окно для Поиска и Фильтрации (чтобы не ломать дизайн главного экрана)
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Поиск и фильтр") },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Поиск по названию") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Тип тренировки:", fontWeight = FontWeight.Bold)
                    val types = listOf("Все", "Интервальная", "Силовая", "Кардио", "Табата")
                    types.forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTypeFilter = type }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = (selectedTypeFilter == type),
                                onClick = { selectedTypeFilter = type }
                            )
                            Text(text = type)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Применить", color = ButtonPurple)
                }
            }
        )
    }
}

@Composable
fun WorkoutItem(
    session: WorkoutSession,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardWhite)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Левая часть с информацией
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Название
            Text(
                text = session.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = TextBlack
            )

            // Дата и время
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Дата",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = session.date,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextBlack
                )

                Spacer(modifier = Modifier.width(30.dp))

                Text(
                    text = "Время начала",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = session.time,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextBlack
                )
            }

            // Продолжительность
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Продолжительность",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = TextBlack
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = session.duration,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextBlack
                )
            }
        }

        // Правая часть с чекбоксом
        Spacer(modifier = Modifier.width(10.dp))
        Checkbox(
            checked = session.isSelected,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = TextBlack,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
    }
}