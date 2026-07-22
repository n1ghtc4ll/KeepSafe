package com.example.cardioproject.presentation

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private val SettingsBackground = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1D1B20)
private val TextPrimary = Color(0xFF000000)
private val CardBorder = Color(0xFFCAC4D0)
private val AccentPurple = Color(0xFF6750A4)
private val InputBackgroundGray = Color(0xFFEEEEEE)
private val TabataBackground = Color(0xFFF3EDF7)
private val ElementBackgroundWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSettingsScreen(
    onBackClick: () -> Unit = {},
    onStartClick: (WorkoutSession) -> Unit = {}
) {
    // Состояния общих настроек
    var workoutName by remember { mutableStateOf("Новая тренировка") }
    var selectedTag by remember { mutableStateOf(defaultTags[0]) }
    var isTabataEnabled by remember { mutableStateOf(true) }

    // Состояния параметров Табата-таймера
    var selectedProfile by remember { mutableStateOf("Стандартный") }
    val profiles = listOf("Стандартный", "Интенсивный", "Легкий", "Свой профиль")
    var isProfileDropdownExpanded by remember { mutableStateOf(false) }

    var prepTime by remember { mutableStateOf("00:10") }
    var intervalsCount by remember { mutableStateOf("8") }
    var workTime by remember { mutableStateOf("00:20") }
    var restTime by remember { mutableStateOf("00:10") }
    var coolDownTime by remember { mutableStateOf("01:00") }
    var cyclesCount by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Шапка с кнопкой назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextDark,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Настройка тренировки",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Название
        SettingInputRow(
            label = "Название",
            value = workoutName,
            onValueChange = { workoutName = it },
            isDropdown = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Тег
        SettingTagDropdown(
            label = "Тег",
            selectedTag = selectedTag,
            tags = defaultTags,
            onTagSelected = { selectedTag = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Свитч Табата-таймера
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Табата-таймер",
                fontSize = 20.sp,
                color = TextDark
            )
            Switch(
                checked = isTabataEnabled,
                onCheckedChange = { isTabataEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AccentPurple
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Карточка параметров таймера
        if (isTabataEnabled) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TabataBackground, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Параметры таймера",
                    fontSize = 20.sp,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = CardBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Готовые профили
                Text(
                    text = "Готовые профили",
                    fontSize = 20.sp,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(ElementBackgroundWhite, RoundedCornerShape(6.dp))
                            .clickable { isProfileDropdownExpanded = true }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(text = selectedProfile, fontSize = 16.sp, color = TextDark)
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            tint = TextDark,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                    DropdownMenu(
                        expanded = isProfileDropdownExpanded,
                        onDismissRequest = { isProfileDropdownExpanded = false }
                    ) {
                        profiles.forEach { profile ->
                            DropdownMenuItem(
                                text = { Text(profile) },
                                onClick = {
                                    selectedProfile = profile
                                    isProfileDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Ряды с параметрами
                TabataTimeRow(
                    label = "Пауза перед\nтренировкой",
                    value = prepTime,
                    onValueChange = { prepTime = it })
                Spacer(modifier = Modifier.height(16.dp))

                TabataNumberRow(
                    label = "Кол-во интервалов",
                    value = intervalsCount,
                    onValueChange = { intervalsCount = it })
                Spacer(modifier = Modifier.height(16.dp))

                TabataTimeRow(
                    label = "Упражнение",
                    value = workTime,
                    onValueChange = { workTime = it })
                Spacer(modifier = Modifier.height(16.dp))

                TabataTimeRow(label = "Отдых", value = restTime, onValueChange = { restTime = it })
                Spacer(modifier = Modifier.height(16.dp))

                TabataTimeRow(
                    label = "Перезарядка",
                    value = coolDownTime,
                    onValueChange = { coolDownTime = it })
                Spacer(modifier = Modifier.height(16.dp))

                TabataNumberRow(
                    label = "Кол-во кругов",
                    value = cyclesCount,
                    onValueChange = { cyclesCount = it })
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка Начать
        Button(
            onClick = {
                // TODO: Передать параметры таймера в логику таймера, когда она будет готова.
                // Формируем новую сессию и передаем в коллбэк для истории
                val formatterDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                val now = Date()

                val newSession = WorkoutSession(
                    id = UUID.randomUUID().toString(),
                    title = workoutName.ifBlank { "Новая тренировка" },
                    date = formatterDate.format(now),
                    time = formatterTime.format(now),
                    duration = "00:00:00", // Пока 0, обновится после окончания таймера
                    tag = selectedTag
                )
                onStartClick(newSession)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
        ) {
            Text(
                text = "Начать",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SettingInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isDropdown: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .width(241.dp)
                .height(44.dp)
                .background(InputBackgroundGray, RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = if (isDropdown) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 16.sp, color = TextDark),
                singleLine = true,
                enabled = !isDropdown
            )
            if (isDropdown) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = TextDark
                )
            }
        }
    }
}

@Composable
private fun SettingTagDropdown(
    label: String,
    selectedTag: WorkoutTag,
    tags: List<WorkoutTag>,
    onTagSelected: (WorkoutTag) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Box {
            Box(
                modifier = Modifier
                    .width(241.dp)
                    .height(44.dp)
                    .background(InputBackgroundGray, RoundedCornerShape(6.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(selectedTag.color, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = selectedTag.name, fontSize = 16.sp, color = TextDark)
                }
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = TextDark,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tags.forEach { tag ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(tag.color, RoundedCornerShape(4.dp))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(tag.name)
                            }
                        },
                        onClick = {
                            onTagSelected(tag)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabataTimeRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = TextDark,
            lineHeight = 22.sp,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            HalfDropIcon(modifier = Modifier.size(24.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 30.dp)
                    .background(ElementBackgroundWhite, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
private fun TabataNumberRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 30.dp)
                .background(ElementBackgroundWhite, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                ),
                singleLine = true
            )
        }
    }
}

@Composable
private fun HalfDropIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Внешний контур капли
        val path = Path().apply {
            moveTo(w / 2f, h * 0.1f)
            quadraticBezierTo(w * 0.9f, h * 0.5f, w * 0.85f, h * 0.75f)
            quadraticBezierTo(w * 0.75f, h * 0.95f, w / 2f, h * 0.95f)
            quadraticBezierTo(w * 0.25f, h * 0.95f, w * 0.15f, h * 0.75f)
            quadraticBezierTo(w * 0.1f, h * 0.5f, w / 2f, h * 0.1f)
            close()
        }

        drawPath(path = path, color = TextDark, style = Stroke(width = 2.dp.toPx()))

        // Закрашиваем левую половину капли
        val halfPath = Path().apply {
            moveTo(w / 2f, h * 0.1f)
            quadraticBezierTo(w * 0.1f, h * 0.5f, w * 0.15f, h * 0.75f)
            quadraticBezierTo(w * 0.25f, h * 0.95f, w / 2f, h * 0.95f)
            close()
        }
        drawPath(path = halfPath, color = TextDark)
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
fun WorkoutSettingsScreenPreview() {
    MaterialTheme {
        WorkoutSettingsScreen()
    }
}