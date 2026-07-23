package com.example.cardioproject.anthrodiary.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnthroDiaryRoute(
    onBackClick: () -> Unit = {}
) {
    val viewModel: AnthroDiaryViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    val saveError by viewModel.saveError.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Показываем ошибку валидации в Snackbar и сразу сбрасываем состояние,
    // чтобы при пересоздании композиции (например, повороте экрана) сообщение
    // не всплыло повторно.
    LaunchedEffect(saveError) {
        saveError?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.consumeSaveError()
        }
    }

    AnthroDiaryScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onWeightChange = viewModel::onWeightChange,
        onChestChange = viewModel::onChestChange,
        onWaistChange = viewModel::onWaistChange,
        onHipsChange = viewModel::onHipsChange,
        onBicepsChange = viewModel::onBicepsChange,
        onSaveClick = viewModel::onSaveClick,
        onPreviousRecordPickerClick = viewModel::onPreviousRecordPickerClick,
        //onTabSelected = viewModel::onTabSelected,
    )
}