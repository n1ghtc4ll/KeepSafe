package com.example.cardioproject.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardioproject.core.ui.components.AppThemeTopBar
import com.example.cardioproject.core.ui.theme.TabataPhaseColorsPalette
import com.example.cardioproject.core.ui.components.ColorPickerDialog
import com.example.cardioproject.core.ui.components.TabataParams
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme
import com.example.cardioproject.core.ui.theme.spacing
import com.example.cardioproject.settings.presentation.model.PhaseType
import com.example.cardioproject.settings.presentation.model.ProfileBuilderUiState
import com.example.cardioproject.settings.presentation.ui.components.BuilderLabelTextField

@Composable
fun ProfileBuilderScreen(
    state: ProfileBuilderUiState = ProfileBuilderUiState(),
    onBackClick: () -> Unit = {  },
    onSaveClick: () -> Unit = {  },
    onNameChange: (String) -> Unit = {  },
    onParamChange: (String, Int) -> Unit = { _, _ -> },
    onPhaseColorChange: (PhaseType, Long) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AppThemeTopBar(
                title = if (state.id == null) "Создание профиля" else "Редактирование",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                ) {
                    BuilderLabelTextField(
                        label = "Название",
                        value = state.name,
                        onValueChange = onNameChange
                    )

                    TabataParams(
                        tabataSetup = state.tabataSetup,
                        availableProfiles = null,
                        onCustomParamChange = onParamChange,
                        onCustomParamColorChange = onPhaseColorChange
                    )
                }

                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Сохранить",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileBuilderScreenPreview() {
    KeepSafeProjectTheme {
        ProfileBuilderScreen()
    }
}