package com.example.cardioproject.anthrodiary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.anthrodiary.domain.model.AnthroMeasurement
import com.example.cardioproject.anthrodiary.domain.usecase.AnthroMeasurementRawInput
import com.example.cardioproject.anthrodiary.domain.usecase.ObserveMeasurementsUseCase
import com.example.cardioproject.anthrodiary.domain.usecase.SaveMeasurementResult
import com.example.cardioproject.anthrodiary.domain.usecase.SaveMeasurementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnthroDiaryViewModel(
    private val observeMeasurements: ObserveMeasurementsUseCase,
    private val saveMeasurement: SaveMeasurementUseCase,
) : ViewModel() {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))

    private val _uiState = MutableStateFlow(
        AnthroDiaryUiState(currentDate = dateFormatter.format(Date()))
    )
    val uiState: StateFlow<AnthroDiaryUiState> = _uiState.asStateFlow()

    // Отдельно от uiState, потому что это одноразовое сообщение об ошибке (для Snackbar/Toast),
    // а не часть постоянного состояния экрана.
    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    private var history: List<AnthroMeasurement> = emptyList()

    // Индекс в history — какую запись показываем в карточке "Предыдущая запись".
    // 0 = самая свежая. Стрелка вниз в UI пролистывает к более старым записям и обратно к началу.
    private var previousRecordCursor = 0

    init {
        viewModelScope.launch {
            observeMeasurements().collect { measurements ->
                history = measurements
                previousRecordCursor = previousRecordCursor.coerceIn(0, (measurements.size - 1).coerceAtLeast(0))
                _uiState.value = _uiState.value.copy(
                    previousRecord = measurements.getOrNull(previousRecordCursor)?.toUiRecord()
                )
            }
        }
    }

    fun onWeightChange(value: String) = updateInput { it.copy(weight = value) }
    fun onChestChange(value: String) = updateInput { it.copy(chest = value) }
    fun onWaistChange(value: String) = updateInput { it.copy(waist = value) }
    fun onHipsChange(value: String) = updateInput { it.copy(hips = value) }
    fun onBicepsChange(value: String) = updateInput { it.copy(biceps = value) }

    private inline fun updateInput(transform: (AnthroMeasurementInput) -> AnthroMeasurementInput) {
        _uiState.value = _uiState.value.copy(input = transform(_uiState.value.input))
    }

    fun onSaveClick() {
        val input = _uiState.value.input
        viewModelScope.launch {
            when (
                val result = saveMeasurement(
                    AnthroMeasurementRawInput(
                        weight = input.weight,
                        chest = input.chest,
                        waist = input.waist,
                        hips = input.hips,
                        biceps = input.biceps
                    )
                )
            ) {
                is SaveMeasurementResult.Success -> {
                    _saveError.value = null
                    previousRecordCursor = 0 // после сохранения показываем самую свежую запись
                    _uiState.value = _uiState.value.copy(input = AnthroMeasurementInput())
                }
                is SaveMeasurementResult.ValidationError -> {
                    _saveError.value = result.message
                }
            }
        }
    }

    /** Стрелка вниз у "Предыдущая запись" — пролистывает историю по кругу. */
    fun onPreviousRecordPickerClick() {
        if (history.isEmpty()) return
        previousRecordCursor = (previousRecordCursor + 1) % history.size
        _uiState.value = _uiState.value.copy(
            previousRecord = history[previousRecordCursor].toUiRecord()
        )
    }

    fun onTabSelected(tab: BottomNavTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun consumeSaveError() {
        _saveError.value = null
    }

    private fun AnthroMeasurement.toUiRecord(): PreviousAnthroRecord = PreviousAnthroRecord(
        date = dateFormatter.format(Date(measuredAtEpochMillis)),
        weight = weightKg.toDisplayString(),
        chest = chestCm.toDisplayString(),
        waist = waistCm.toDisplayString(),
        hips = hipsCm.toDisplayString(),
        biceps = bicepsCm.toDisplayString()
    )
}

/** "72.0" -> "72", "72.5" -> "72.5" — чтобы не показывать лишний ".0" для целых значений. */
private fun Float.toDisplayString(): String =
    if (this == this.toInt().toFloat()) this.toInt().toString() else this.toString()