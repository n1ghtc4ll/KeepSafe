package com.example.cardioproject.settings.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.ui.theme.WhiteBg

@Composable
fun EditablePhaseRow(
    label: String,
    value: Int,
    isTimeField: Boolean,
    colorHex: Long?,
    onColorClick: (() -> Unit)?,
    onValueChange: (Int) -> Unit
) {
    var singleText by remember { mutableStateOf("") }
    var singleFocused by remember { mutableStateOf(false) }

    var mText by remember { mutableStateOf("00") }
    var sText by remember { mutableStateOf("00") }
    var mFocused by remember { mutableStateOf(false) }
    var sFocused by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        if (isTimeField) {
            val m = value / 60
            val s = value % 60
            if (!mFocused && !sFocused) {
                mText = String.format("%02d", m)
                sText = String.format("%02d", s)
            }
        } else {
            if (!singleFocused) {
                singleText = if (value == 0) "" else value.toString()
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (colorHex != null && onColorClick != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .clickable(onClick = onColorClick)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Box(
                modifier = Modifier
                    .size(width = if (isTimeField) 100.dp else 64.dp, height = 30.dp)
                    .background(WhiteBg, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isTimeField) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = mText,
                            onValueChange = { newM ->
                                val digits = newM.filter { it.isDigit() }
                                if (digits.length <= 3) {
                                    mText = digits
                                    val m = digits.toIntOrNull() ?: 0
                                    val s = sText.toIntOrNull() ?: 0
                                    onValueChange(m * 60 + s)
                                }
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(36.dp)
                                .onFocusChanged { focusState ->
                                    mFocused = focusState.isFocused
                                    if (!focusState.isFocused) {
                                        val m = mText.toIntOrNull() ?: 0
                                        mText = String.format("%02d", m)
                                    }
                                }
                        )

                        Text(":", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

                        BasicTextField(
                            value = sText,
                            onValueChange = { newS ->
                                val digits = newS.filter { it.isDigit() }
                                if (digits.length <= 2) {
                                    var num = digits.toIntOrNull() ?: 0
                                    if (num > 59) {
                                        num = 59
                                        sText = "59"
                                    } else {
                                        sText = digits
                                    }
                                    val m = mText.toIntOrNull() ?: 0
                                    onValueChange(m * 60 + num)
                                }
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .width(28.dp)
                                .onFocusChanged { focusState ->
                                    sFocused = focusState.isFocused
                                    if (!focusState.isFocused) {
                                        val s = sText.toIntOrNull() ?: 0
                                        sText = String.format("%02d", s)
                                    }
                                }
                        )
                    }
                } else {
                    BasicTextField(
                        value = singleText,
                        onValueChange = { newText ->
                            val digits = newText.filter { it.isDigit() }
                            if (digits.length <= 3) {
                                singleText = digits
                                onValueChange(digits.toIntOrNull() ?: 0)
                            }
                        },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { singleFocused = it.isFocused }
                    )
                }
            }
        }
    }
}