package com.example.cardioproject.anthrodiary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Colors (same light palette as SettingsScreen) ----------
private val BackgroundColor = Color(0xFFFFFFFF)
private val SurfaceColor = Color(0xFFF5F5F7)
private val AccentPurple = Color(0xFF7C6BFF)
private val TextPrimary = Color(0xFF1A1A1E)
private val TextSecondary = Color(0xFF737379)
private val DividerColor = Color(0xFFE0E0E4)

private val KeepSafeLightColorScheme = lightColorScheme(
    background = BackgroundColor,
    surface = SurfaceColor,
    primary = AccentPurple,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

// ---------- Data models ----------

data class AnthroMeasurementInput(
    val weight: String = "",
    val chest: String = "",
    val waist: String = "",
    val hips: String = "",
    val biceps: String = "",
)

data class PreviousAnthroRecord(
    val date: String,
    val weight: String,
    val chest: String,
    val waist: String,
    val hips: String,
    val biceps: String,
)

enum class BottomNavTab { HOME, WORKOUT_HISTORY, MEASUREMENTS_DIARY }

data class AnthroDiaryUiState(
    val currentDate: String = "--/--/----",
    val input: AnthroMeasurementInput = AnthroMeasurementInput(),
    val previousRecord: PreviousAnthroRecord? = PreviousAnthroRecord(
        date = "XX.XX.XXXX",
        weight = "###",
        chest = "###",
        waist = "###",
        hips = "###",
        biceps = "###",
    ),
    val selectedTab: BottomNavTab = BottomNavTab.MEASUREMENTS_DIARY,
)

// ---------- Screen ----------

@Composable
fun AnthroDiaryScreen(
    state: AnthroDiaryUiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
    onWeightChange: (String) -> Unit = {},
    onChestChange: (String) -> Unit = {},
    onWaistChange: (String) -> Unit = {},
    onHipsChange: (String) -> Unit = {},
    onBicepsChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onPreviousRecordPickerClick: () -> Unit = {},
    //onTabSelected: (BottomNavTab) -> Unit = {},
) {
    Scaffold(
        topBar = { AnthroDiaryTopBar(currentDate = state.currentDate, onBackClick = onBackClick) },
        //bottomBar = { AnthroDiaryBottomBar(selectedTab = state.selectedTab, onTabSelected = onTabSelected) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 12.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                MeasurementInputCard(
                    input = state.input,
                    onWeightChange = onWeightChange,
                    onChestChange = onChestChange,
                    onWaistChange = onWaistChange,
                    onHipsChange = onHipsChange,
                    onBicepsChange = onBicepsChange
                )

                Spacer(Modifier.height(20.dp))

                SaveButton(onClick = onSaveClick)

                Spacer(Modifier.height(24.dp))

                if (state.previousRecord != null) {
                    PreviousRecordCard(
                        record = state.previousRecord,
                        onPickerClick = onPreviousRecordPickerClick
                    )
                }
            }
        }
    }
}

// ---------- Components ----------

@Composable
private fun AnthroDiaryTopBar(currentDate: String, onBackClick: () -> Unit) {
    Surface(color = BackgroundColor) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.padding(top = 2.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextPrimary
                )
            }
            Column {
                Text(
                    text = "Антропометрический\nдневник",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = currentDate,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun MeasurementInputCard(
    input: AnthroMeasurementInput,
    onWeightChange: (String) -> Unit,
    onChestChange: (String) -> Unit,
    onWaistChange: (String) -> Unit,
    onHipsChange: (String) -> Unit,
    onBicepsChange: (String) -> Unit,
) {
    Surface(
        color = SurfaceColor,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            MeasurementInputRow(
                label = "Вес",
                unit = "кг",
                value = input.weight,
                onValueChange = onWeightChange
            )
            MeasurementInputRow(
                label = "Обхват груди",
                unit = "см",
                value = input.chest,
                onValueChange = onChestChange
            )
            MeasurementInputRow(
                label = "Обхват талии",
                unit = "см",
                value = input.waist,
                onValueChange = onWaistChange
            )
            MeasurementInputRow(
                label = "Обхват бедер",
                unit = "см",
                value = input.hips,
                onValueChange = onHipsChange
            )
            MeasurementInputRow(
                label = "Обхват бицепса",
                unit = "см",
                value = input.biceps,
                onValueChange = onBicepsChange,
                isLast = true
            )
        }
    }
}

@Composable
private fun MeasurementInputRow(
    label: String,
    unit: String,
    value: String,
    onValueChange: (String) -> Unit,
    isLast: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextPrimary, fontSize = 15.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(72.dp),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceColor,
                    unfocusedContainerColor = Color(0xFFE9E9EC),
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(text = unit, color = TextSecondary, fontSize = 14.sp)
        }
    }
    if (!isLast) {
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
    }
}

@Composable
private fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple, contentColor = Color.White)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = "Записать", fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PreviousRecordCard(
    record: PreviousAnthroRecord,
    onPickerClick: () -> Unit,
) {
    Surface(
        color = SurfaceColor,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onPickerClick)
                    .padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Предыдущая запись", color = TextPrimary, fontSize = 15.sp)
                Spacer(Modifier.width(8.dp))
                Text(text = "|", color = DividerColor, fontSize = 15.sp)
                Spacer(Modifier.width(8.dp))
                Text(text = record.date, color = TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Выбрать другую запись",
                    tint = TextSecondary
                )
            }
            HorizontalDivider(color = DividerColor, thickness = 1.dp)
            PreviousRecordRow(label = "Вес", value = "${record.weight} кг")
            PreviousRecordRow(label = "Обхват груди", value = "${record.chest} см")
            PreviousRecordRow(label = "Обхват талии", value = "${record.waist} см")
            PreviousRecordRow(label = "Обхват бедер", value = "${record.hips} см")
            PreviousRecordRow(label = "Обхват бицепса", value = "${record.biceps} см", isLast = true)
        }
    }
}

@Composable
private fun PreviousRecordRow(label: String, value: String, isLast: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextPrimary, fontSize = 15.sp)
        Text(text = value, color = TextSecondary, fontSize = 14.sp)
    }
    if (!isLast) {
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
    }
}

/*@Composable
private fun AnthroDiaryBottomBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
) {
    Surface(color = BackgroundColor, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Главная",
                selected = selectedTab == BottomNavTab.HOME,
                onClick = { onTabSelected(BottomNavTab.HOME) }
            )
            BottomNavItem(
                icon = Icons.Default.History,
                label = "История\nтренировок",
                selected = selectedTab == BottomNavTab.WORKOUT_HISTORY,
                onClick = { onTabSelected(BottomNavTab.WORKOUT_HISTORY) }
            )
            BottomNavItem(
                icon = Icons.Default.CalendarMonth,
                label = "Дневник\nзамеров",
                selected = selectedTab == BottomNavTab.MEASUREMENTS_DIARY,
                onClick = { onTabSelected(BottomNavTab.MEASUREMENTS_DIARY) }
            )
        }
    }
}*/

/*@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (selected) AccentPurple else TextSecondary
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp
        )
    }
}*/

// ---------- Preview ----------

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 780)
@Composable
private fun AnthroDiaryScreenPreview() {
    var state by remember { mutableStateOf(AnthroDiaryUiState()) }

    MaterialTheme(colorScheme = KeepSafeLightColorScheme) {
        AnthroDiaryScreen(
            state = state,
            onWeightChange = { state = state.copy(input = state.input.copy(weight = it)) },
            onChestChange = { state = state.copy(input = state.input.copy(chest = it)) },
            onWaistChange = { state = state.copy(input = state.input.copy(waist = it)) },
            onHipsChange = { state = state.copy(input = state.input.copy(hips = it)) },
            onBicepsChange = { state = state.copy(input = state.input.copy(biceps = it)) },
            //onTabSelected = { state = state.copy(selectedTab = it) }
        )
    }
}