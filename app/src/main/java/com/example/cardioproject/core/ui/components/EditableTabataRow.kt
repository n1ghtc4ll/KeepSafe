package com.example.cardioproject.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.core.ui.theme.TabataPhaseColorsPalette
import com.example.cardioproject.core.ui.theme.spacing
import com.example.cardioproject.settings.presentation.model.PhaseType

@Composable
fun EditableTabataRow(
    label: String,
    value: Int,
    isReadOnly: Boolean,
    isTimeField: Boolean,
    colorHex: Long? = null,
    onValueChange: (Int) -> Unit,
    onColorChange: (Long) -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }

    var singleText by remember { mutableStateOf("") }
    var singleFocused by remember { mutableStateOf(false) }

    var mText by remember { mutableStateOf(TextFieldValue("00")) }
    var sText by remember { mutableStateOf(TextFieldValue("00")) }

    var mFocused by remember { mutableStateOf(false) }
    var sFocused by remember { mutableStateOf(false) }

    val mFocusRequester = remember { FocusRequester() }
    val sFocusRequester = remember { FocusRequester() }

    LaunchedEffect(value, isReadOnly) {
        if (isTimeField) {
            val minutes = value / 60
            val seconds = value % 60

            if (isReadOnly || (!mFocused && !sFocused)) {
                mText = TextFieldValue(String.format("%02d", minutes))
                sText = TextFieldValue(String.format("%02d", seconds))
            }
        } else {
            if (isReadOnly || !singleFocused) {
                singleText = value.toString()
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
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            if (colorHex != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .clickable(onClick = { if (!isReadOnly) showColorPicker = true })
                )
            }

            Box(
                modifier = Modifier
                    .size(width = if (isTimeField) 100.dp else 64.dp, height = 30.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isTimeField) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = mText,
                            onValueChange = { newValue ->
                                if (!isReadOnly) {
                                    val digits = newValue.text.filter { it.isDigit() }.take(3)
                                    mText = newValue.copy(
                                        text = digits,
                                        selection = TextRange(digits.length)
                                    )

                                    val minutes = digits.toIntOrNull() ?: 0
                                    val seconds = sText.text.toIntOrNull() ?: 0

                                    onValueChange(minutes * 60 + seconds)
                                }
                            },
                            readOnly = isReadOnly,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color =
                                    if (isReadOnly) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .width(36.dp)
                                .focusRequester(mFocusRequester)
                                .onFocusChanged {
                                    val wasFocused = mFocused
                                    mFocused = it.isFocused

                                    if (wasFocused && !it.isFocused && !isReadOnly) {
                                        val minutes = mText.text.toIntOrNull() ?: 0
                                        mText = TextFieldValue(
                                            String.format("%02d", minutes)
                                        )
                                    }
                                }
                                .pointerInput(isReadOnly) {
                                    if (!isReadOnly) {
                                        while (true) {
                                            val tapped = awaitPointerEventScope {
                                                awaitFirstDown(pass = PointerEventPass.Initial)

                                                waitForUpOrCancellation(
                                                    pass = PointerEventPass.Initial
                                                ) != null
                                            }

                                            if (tapped) {
                                                mFocusRequester.requestFocus()

                                                withFrameNanos { }

                                                mText = mText.copy(
                                                    selection = TextRange(0, mText.text.length)
                                                )
                                            }
                                        }
                                    }
                                }
                        )

                        Text(
                            text = ":",
                            fontSize = 16.sp,
                            color =
                                if (isReadOnly) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.primary
                        )

                        BasicTextField(
                            value = sText,
                            onValueChange = { newValue ->
                                if (!isReadOnly) {
                                    val digits = newValue.text.filter { it.isDigit() }.take(2)
                                    val seconds = (digits.toIntOrNull() ?: 0)
                                        .coerceAtMost(59)

                                    sText = newValue.copy(
                                        text = digits,
                                        selection = TextRange(digits.length)
                                    )

                                    val minutes = mText.text.toIntOrNull() ?: 0
                                    onValueChange(minutes * 60 + seconds)
                                }
                            },
                            readOnly = isReadOnly,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color =
                                    if (isReadOnly) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .width(28.dp)
                                .focusRequester(sFocusRequester)
                                .onFocusChanged {
                                    val wasFocused = sFocused
                                    sFocused = it.isFocused

                                    if (wasFocused && !it.isFocused && !isReadOnly) {
                                        val seconds = (sText.text.toIntOrNull() ?: 0)
                                            .coerceAtMost(59)
                                        sText = TextFieldValue(
                                            String.format("%02d", seconds)
                                        )
                                    }
                                }
                                .pointerInput(isReadOnly) {
                                    if (!isReadOnly) {
                                        while (true) {
                                            val tapped = awaitPointerEventScope {
                                                awaitFirstDown(pass = PointerEventPass.Initial)

                                                waitForUpOrCancellation(
                                                    pass = PointerEventPass.Initial
                                                ) != null
                                            }

                                            if (tapped) {
                                                sFocusRequester.requestFocus()

                                                withFrameNanos { }

                                                sText = sText.copy(
                                                    selection = TextRange(0, sText.text.length)
                                                )
                                            }
                                        }
                                    }
                                }
                        )
                    }
                } else {
                    BasicTextField(
                        value = singleText,
                        onValueChange = { newText ->
                            if (!isReadOnly) {
                                val digits = newText.filter { it.isDigit() }
                                if (digits.length <= 3) {
                                    singleText = digits
                                    onValueChange(digits.toIntOrNull() ?: 0)
                                }
                            }
                        },
                        readOnly = isReadOnly,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color =
                                if (isReadOnly) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                singleFocused = it.isFocused
                            }
                    )
                }
            }
        }
    }

    if (showColorPicker && colorHex != null) {
        ColorPickerDialog(
            initialColor = colorHex,
            colorsPalette = TabataPhaseColorsPalette,
            onColorSelected = { selectedColorHex ->
                onColorChange(selectedColorHex)
                showColorPicker = false
            },
            onDismiss = { showColorPicker = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditableTabataRowPreview() {
    EditableTabataRow(
        label = "Label1",
        value = 0,
        isReadOnly = false,
        isTimeField = true,
        colorHex = 0,
        onValueChange = { },
        onColorChange = {  }
    )
}
