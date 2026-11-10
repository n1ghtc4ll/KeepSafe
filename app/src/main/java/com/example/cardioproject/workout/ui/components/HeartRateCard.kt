package com.example.cardioproject.workout.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.workout.presentation.util.getZoneColorForBpm

@Composable
fun HeartRateCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    heartRate: Int?,
    zones: List<HeartRateZone>
) {
    val targetColor = getZoneColorForBpm(heartRate, zones)
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(500)
    )

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainer,
            animatedColor
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(gradientBrush)
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${heartRate ?: "–"}",
                fontSize = 120.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                lineHeight = 110.sp
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 4.dp),
                thickness = 1.dp,
                color = Color(0x40000000)
            )

            Text(
                text = "уд/мин",
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}
