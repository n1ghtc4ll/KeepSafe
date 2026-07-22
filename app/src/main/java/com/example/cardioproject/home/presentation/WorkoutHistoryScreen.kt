package com.example.cardioproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

private val BackgroundF3 = Color(0xFFF3F3F3)
private val ButtonPurple = Color(0xFF6750A4)
private val TextBlack = Color(0xFF1D1B20)
private val CardWhite = Color(0xFFFFFFFF)

data class WorkoutTag(
    val id: String,
    var name: String,
    var color: Color
)

data class WorkoutSession(
    val id: String,
    var title: String,
    val date: String,
    val time: String,
    val duration: String,
    var tag: WorkoutTag,
    var isSelected: Boolean = false
)

// Глобальные моковые теги для доступа из разных экранов
val defaultTags = mutableStateListOf(
    WorkoutTag("t1", "Общая", Color(0xFF009951)),
    WorkoutTag("t2", "Силовая", Color(0xFFC00F0C)),
    WorkoutTag("t3", "Легкая", Color(0xFFE6A000))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    workouts: List<WorkoutSession>,
    onBackClick: () -> Unit = {},
    onWorkoutClick: (String) -> Unit = {},
    onAddWorkout: (WorkoutSession) -> Unit = {},
    onEditWorkout: (WorkoutSession) -> Unit = {},
    onDeleteWorkout: (WorkoutSession) -> Unit = {}
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showPeriodDialog by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf("Все") }

    // Состояния для добавления/редактирования
    var showEditDialog by remember { mutableStateOf(false) }
    var workoutToEdit by remember { mutableStateOf<WorkoutSession?>(null) }

    val filteredWorkouts = workouts.filter { session ->
        val matchesSearch = session.title.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedTypeFilter == "Все" || session.tag.name == selectedTypeFilter
        matchesSearch && matchesType
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    workoutToEdit = null
                    showEditDialog = true
                },
                containerColor = ButtonPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Добавить тренировку")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundF3)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = TextBlack)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "История тренировок",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showFilterDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                ) {
                    Icon(Icons.Filled.List, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Фильтр", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { showPeriodDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Период", fontSize = 16.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (filteredWorkouts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Тренировок не найдено", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredWorkouts) { session ->
                        WorkoutItem(
                            session = session,
                            onClick = { onWorkoutClick(session.id) },
                            onEditClick = {
                                workoutToEdit = session
                                showEditDialog = true
                            },
                            onDeleteClick = { onDeleteWorkout(session) },
                            onCheckedChange = { isChecked ->
                                // Обновляем состояние isSelected у конкретной сессии
                                onEditWorkout(session.copy(isSelected = isChecked))
                            }
                        )
                    }
                }
            }
        }
    }

    if (showPeriodDialog) {
        var startDate by remember { mutableStateOf("01.07.2023") }
        var endDate by remember { mutableStateOf("31.07.2023") }

        AlertDialog(
            onDismissRequest = { showPeriodDialog = false },
            title = { Text("Выберите период") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("От (ДД.ММ.ГГГГ)") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("До (ДД.ММ.ГГГГ)") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPeriodDialog = false }) {
                    Text("Применить", color = ButtonPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPeriodDialog = false }) {
                    Text("Отмена", color = Color.Gray)
                }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Фильтр тренировок") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Поиск по названию") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Тип тренировки:", fontWeight = FontWeight.Bold)
                    val types = listOf("Все") + defaultTags.map { it.name }
                    types.forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTypeFilter = type }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedTypeFilter == type,
                                onClick = { selectedTypeFilter = type }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = type)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Готово", color = ButtonPurple)
                }
            }
        )
    }

    if (showEditDialog) {
        WorkoutEditDialog(
            initialSession = workoutToEdit,
            onDismiss = { showEditDialog = false },
            onSave = { session ->
                if (workoutToEdit == null) {
                    onAddWorkout(session)
                } else {
                    onEditWorkout(session)
                }
                showEditDialog = false
            }
        )
    }
}

@Composable
fun WorkoutEditDialog(
    initialSession: WorkoutSession?,
    onDismiss: () -> Unit,
    onSave: (WorkoutSession) -> Unit
) {
    var title by remember { mutableStateOf(initialSession?.title ?: "") }
    var date by remember { mutableStateOf(initialSession?.date ?: "01.01.2023") }
    var time by remember { mutableStateOf(initialSession?.time ?: "12:00") }
    var duration by remember { mutableStateOf(initialSession?.duration ?: "00:30:00") }
    var tag by remember { mutableStateOf(initialSession?.tag ?: defaultTags[0]) }

    var expandedTag by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialSession == null) "Добавить тренировку" else "Редактировать тренировку") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Дата") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Время") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Длительность") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Box {
                    OutlinedTextField(
                        value = tag.name,
                        onValueChange = { },
                        label = { Text("Тег") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedTag = true },
                        trailingIcon = {
                            IconButton(onClick = { expandedTag = true }) {
                                Icon(Icons.Filled.List, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expandedTag,
                        onDismissRequest = { expandedTag = false }
                    ) {
                        defaultTags.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.name) },
                                onClick = {
                                    tag = t
                                    expandedTag = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newSession = WorkoutSession(
                        id = initialSession?.id ?: UUID.randomUUID().toString(),
                        title = title.ifBlank { "Без названия" },
                        date = date,
                        time = time,
                        duration = duration,
                        tag = tag,
                        isSelected = initialSession?.isSelected ?: false
                    )
                    onSave(newSession)
                }
            ) {
                Text("Сохранить", color = ButtonPurple)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.Gray)
            }
        }
    )
}


@Composable
fun WorkoutItem(
    session: WorkoutSession,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Информация о тренировке
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = session.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBlack
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Дата:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = session.date, fontSize = 14.sp, color = TextBlack)

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "Время:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = session.time, fontSize = 14.sp, color = TextBlack)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Длительность:",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = session.duration,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextBlack
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(session.tag.color, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = session.tag.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Чекбокс выбора
                Checkbox(
                    checked = session.isSelected,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(checkedColor = ButtonPurple)
                )
            }

            // Кнопки управления (Изменить / Удалить)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Edit, contentDescription = "Редактировать", tint = Color.Gray)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Удалить", tint = Color.Red)
                }
            }
        }
    }
}