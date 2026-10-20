package com.example.cardioproject.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // 22.sp — Главные заголовки экранов в TopBar (Настройки, Создание профиля)
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 28.sp
    ),
    // 20.sp — Подзаголовки карточек (Параметры таймера) и крупные лейблы
    titleMedium = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    ),
    // 18.sp — Средние лейблы (названия фаз: Упражнение, Отдых)
    titleSmall = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    // 16.sp — Вводимый текст внутри полей (BasicTextField)
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    // 15.sp — Основной текст списков (названия устройств, тумблеры, строки)
    bodyMedium = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    // 14.sp — Текст внутри тегов/чипов и зон пульса
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    ),
    // 13.sp — Заголовки секций (SectionTitle) и вторичный текст (заряд батареи)
    labelMedium = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp
    )
)