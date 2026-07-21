package com.example.cardioproject.workout.presentation.screen

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
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.model.WorkoutSettingsUiState
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

private val SettingsBackground = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1D1B20)
private val TextPrimary = Color(0xFF000000)
private val CardBorder = Color(0xFFCAC4D0)
private val AccentPurple = Color(0xFF6750A4)
private val InputBackgroundGray = Color(0xFFEEEEEE)
private val TabataBackground = Color(0xFFF3EDF7)
private val ElementBackgroundWhite = Color(0xFFFFFFFF)


@Composable
fun WorkoutSettingsScreen(
    viewModel: WorkoutSettingsViewModel = koinViewModel(),
    onNavigateToActiveWorkout: (WorkoutSettings) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    WorkoutSettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onStartClick = {
            val finalSettings = viewModel.getFinalSettings()
            onNavigateToActiveWorkout(finalSettings)
        },
        onTitleChange = viewModel::onTitleChanged,
        onTagSelect = viewModel::onTagSelected,
        onTabataToggle = viewModel::onTabataToggled
    )
}

@Composable
fun WorkoutSettingsScreenContent(
    uiState: WorkoutSettingsUiState = WorkoutSettingsUiState(),
    onBackClick: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onTagSelect: (Tag) -> Unit = {},
    onTabataToggle: (Boolean) -> Unit = {},
    onProfileSelect: (TabataProfile) -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SettingsBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = TextDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Настройка тренировки",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingTextInputRow(
                label = "Название",
                value = uiState.title,
                onValueChange = onTitleChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingDropdownRow(
                label = "Тег",
                items = uiState.availableTags,
                selectedItem = uiState.selectedTag,
                onItemSelected = onTagSelect
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                    checked = uiState.isTabataEnabled,
                    onCheckedChange = onTabataToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AccentPurple
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Карточка параметров таймера
            if (uiState.isTabataEnabled) {
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

                    Text(
                        text = "Готовые профили",
                        fontSize = 20.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomDropdown(
                        items = uiState.availableProfiles,
                        selectedItem = uiState.selectedProfile,
                        itemLabel = { it.name },
                        onItemSelected = onProfileSelect,
                        backgroundColor = ElementBackgroundWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Ряды с параметрами
                    TabataTimeRow(label = "Пауза перед\nтренировкой")

                    Spacer(modifier = Modifier.height(16.dp))

                    TabataNumberRow(label = "Кол-во интервалов")
                    Spacer(modifier = Modifier.height(16.dp))

                    TabataTimeRow(label = "Упражнение")
                    Spacer(modifier = Modifier.height(16.dp))

                    TabataTimeRow(label = "Отдых")
                    Spacer(modifier = Modifier.height(16.dp))

                    TabataTimeRow(label = "Перезарядка")
                    Spacer(modifier = Modifier.height(16.dp))

                    TabataNumberRow(label = "Кол-во кругов")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartClick,
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
}
@Composable
private fun SettingTextInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = TextDark,
            modifier = Modifier.weight(0.35f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .weight(0.65f)
                .height(44.dp)
                .background(InputBackgroundGray, RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 18.sp, color = TextDark),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text("Введите название", color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun SettingDropdownRow(
    label: String,
    items: List<Tag>,
    selectedItem: Tag?,
    onItemSelected: (Tag) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = TextDark,
            modifier = Modifier.weight(0.35f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        CustomDropdown(
            items = items,
            selectedItem = selectedItem,
            itemLabel = { it.name },
            onItemSelected = onItemSelected,
            backgroundColor = InputBackgroundGray,
            modifier = Modifier
                .weight(0.65f)
                .height(44.dp)
        )
    }
}

@Composable
fun <T> CustomDropdown(
    items: List<T>,
    selectedItem: T?,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor, RoundedCornerShape(6.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedItem?.let(itemLabel) ?: "Не выбрано",
                color = TextDark,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Открыть список",
                tint = TextDark
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = itemLabel(item), color = TextDark) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TabataTimeRow(label: String) {
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
            Icon(
                imageVector = Icons.Filled.InvertColors,
                contentDescription = "Выбор цвета",
                tint = TextDark,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 30.dp)
                    .background(ElementBackgroundWhite, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "__ : __",
                    fontSize = 18.sp,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun TabataNumberRow(label: String) {
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
                .background(ElementBackgroundWhite, RoundedCornerShape(6.dp))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
fun WorkoutSettingsScreenContentPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            WorkoutSettingsScreenContent()
        }
    }
}