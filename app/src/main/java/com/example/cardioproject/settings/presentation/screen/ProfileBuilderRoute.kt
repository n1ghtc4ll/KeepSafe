package com.example.cardioproject.settings.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cardioproject.settings.presentation.viewmodel.ProfileBuilderViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileBuilderRoute(
    profileId: String? = null,
    onNavigateBack: () -> Unit
) {
    val viewModel: ProfileBuilderViewModel = koinViewModel(
        parameters = { parametersOf(profileId) }
    )
    val state by viewModel.uiState.collectAsState()

    ProfileBuilderScreen(
        state = state,
        onBackClick = onNavigateBack,
        onSaveClick = {
            viewModel.saveProfile(onSuccess = onNavigateBack)
        },
        onNameChange = viewModel::onNameChange,
        onParamChange = viewModel::onParamChange,
        onPhaseColorChange = viewModel::onPhaseColorChange
    )
}