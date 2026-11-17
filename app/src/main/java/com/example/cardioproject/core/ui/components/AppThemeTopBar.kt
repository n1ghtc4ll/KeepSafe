package com.example.cardioproject.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.cardioproject.core.ui.theme.KeepSafeProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppThemeTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit) = {  },
    actions: @Composable (RowScope.() -> Unit) = { }
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = actions
    )
}

@Composable
fun AppThemeTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit) = {  },
    actions: @Composable (RowScope.() -> Unit) = {  }
) {
    AppThemeTopBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        modifier = modifier,
        onBackClick = onBackClick,
        actions = actions
    )
}

@Preview(showBackground = true)
@Composable
fun AppThemeTopBarPreview() {
    KeepSafeProjectTheme {
        AppThemeTopBar(
            title = "Section1"
        )
    }
}