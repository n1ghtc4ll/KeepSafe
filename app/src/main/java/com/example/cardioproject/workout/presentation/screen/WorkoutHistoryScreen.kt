package com.example.cardioproject.workout.presentation.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardioproject.core.ui.components.AppThemeTopBar
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.core.ui.theme.spacing
import com.example.cardioproject.workout.presentation.model.WorkoutSessionUi
import androidx.core.graphics.toColorInt
import com.example.cardioproject.core.common.presentation.model.WorkoutTagUi
import java.util.UUID


// Глобальные моковые теги для доступа из разных экранов
val defaultTags = mutableStateListOf(
    WorkoutTagUi("t1", "Общая", "#009951", false),
    WorkoutTagUi("t2", "Силовая", "#C00F0C", false),
    WorkoutTagUi("t3", "Легкая", "#E6A000", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    workouts: List<WorkoutSessionUi>,
    availableTags: List<WorkoutTagUi> = defaultTags,
    onBackClick: () -> Unit = {},
    onWorkoutClick: (String) -> Unit = {},
    onAddWorkout: (WorkoutSessionUi) -> Unit = {},
    onEditWorkout: (WorkoutSessionUi) -> Unit = {},
    onDeleteWorkout: (WorkoutSessionUi) -> Unit = {}
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showPeriodDialog by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf("Все") }

    var showEditDialog by remember { mutableStateOf(false) }
    var workoutToEdit by remember { mutableStateOf<WorkoutSessionUi?>(null) }

    val filteredWorkouts = workouts.filter { session ->
        val matchesSearch = session.title.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedTypeFilter == "Все" || session.tag.name == selectedTypeFilter
        matchesSearch && matchesType
    }

    Scaffold(
        topBar = {
            AppThemeTopBar(
                title = "История тренировок",
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    workoutToEdit = null
                    showEditDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, "Добавить тренировку")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    vertical = MaterialTheme.spacing.small,
                    horizontal = MaterialTheme.spacing.medium
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showFilterDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Фильтр", style = MaterialTheme.typography.bodyLarge)
                }

                Button(
                    onClick = { showPeriodDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Период", style = MaterialTheme.typography.bodyLarge)
                }
            }

            if (filteredWorkouts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Тренировок не найдено",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumSmall),
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
                    Text("Применить", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPeriodDialog = false }) {
                    Text("Отмена", color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { 
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Фильтр тренировок")
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Поиск по названию") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Тип тренировки:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    val types = listOf("Все") + availableTags.map { it.name }
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
                    Text("Готово", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    if (showEditDialog) {
        WorkoutEditDialog(
            initialSession = workoutToEdit,
            availableTags = availableTags,
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
    initialSession: WorkoutSessionUi?,
    availableTags: List<WorkoutTagUi>,
    onDismiss: () -> Unit,
    onSave: (WorkoutSessionUi) -> Unit
) {
    var title by remember { mutableStateOf(initialSession?.title ?: "") }
    var date by remember { mutableStateOf(initialSession?.date ?: "01.01.2023") }
    var time by remember { mutableStateOf(initialSession?.time ?: "12:00") }
    var duration by remember { mutableStateOf(initialSession?.duration ?: "00:30:00") }
    var tag by remember { mutableStateOf(initialSession?.tag ?: availableTags.firstOrNull() ?: defaultTags[0]) }

    var expandedTag by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialSession == null) "Добавить тренировку" else "Редактировать тренировку") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumSmall)
            ) {
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
                                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expandedTag,
                        onDismissRequest = { expandedTag = false }
                    ) {
                        availableTags.forEach { t ->
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
                    val newSession = WorkoutSessionUi(
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
                Text("Сохранить", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = MaterialTheme.colorScheme.secondary)
            }
        }
    )
}


@Composable
fun WorkoutItem(
    session: WorkoutSessionUi,
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumSmall)
                ) {
                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                            Text(
                                text = "Дата:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = session.date,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                            Text(
                                text = "Время:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = session.time,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        Text(
                            text = "Длительность:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = session.duration,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                try { Color(session.tag.color.toColorInt()) } catch(_: Exception) { MaterialTheme.colorScheme.primary },
                                RoundedCornerShape(8.dp)
                            )
                            .padding(
                                horizontal = MaterialTheme.spacing.mediumSmall,
                                vertical = MaterialTheme.spacing.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = session.tag.name,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Checkbox(
                    checked = session.isSelected,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.End)
            ) {
                IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Редактировать",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WorkoutHistoryScreenPreview() {
    KeepSafeProjectTheme {
        WorkoutHistoryScreen(
            workouts = listOf(
                WorkoutSessionUi(
                    id = "",
                    title = "Workout1",
                    date = "01.01.2000",
                    time = "00:00",
                    duration = "00:00",
                    tag = WorkoutTagUi("t1", "Общая", "#009951", false)
                ),
                WorkoutSessionUi(
                    id = "",
                    title = "Workout2",
                    date = "02.01.2000",
                    time = "00:00",
                    duration = "00:00",
                    tag = WorkoutTagUi("t2", "Силовая", "#C00F0C", false)
                )
            )
        )
    }
}
