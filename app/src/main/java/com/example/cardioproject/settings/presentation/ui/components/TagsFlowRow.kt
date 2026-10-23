package com.example.cardioproject.settings.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.cardioproject.core.ui.theme.GraySurface
import com.example.cardioproject.settings.presentation.model.WorkoutTag

@Composable
fun TagsFlowRow(
    tags: List<WorkoutTag>,
    onEditTag: (WorkoutTag) -> Unit
) {
    SimpleFlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalGap = 8.dp,
        verticalGap = 8.dp
    ) {
        tags.forEach { tag ->
            TagChip(tag = tag, onEditClick = { onEditTag(tag) })
        }
    }
}

/**
 * Minimal wrap-row layout that places children left-to-right, wrapping to a new line
 * when the current row would exceed the available width. Implemented directly with
 * [androidx.compose.ui.layout.Layout] instead of foundation's FlowRow to avoid API-version mismatches between
 * the compiled app and the IDE preview renderer.
 */
@Composable
fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 8.dp,
    verticalGap: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val horizontalGapPx = horizontalGap.roundToPx()
        val verticalGapPx = verticalGap.roundToPx()
        val maxWidth = constraints.maxWidth
        val itemConstraints = Constraints(maxWidth = maxWidth)
        val placeables = measurables.map { it.measure(itemConstraints) }

        data class FlowLine(
            val items: MutableList<Placeable> = mutableListOf(),
            var width: Int = 0,
            var height: Int = 0
        )

        val rows = mutableListOf(FlowLine())
        placeables.forEach { placeable ->
            var row = rows.last()
            val extraWidth =
                if (row.items.isEmpty()) placeable.width else horizontalGapPx + placeable.width
            if (row.items.isNotEmpty() && row.width + extraWidth > maxWidth) {
                row = FlowLine()
                rows.add(row)
            }
            row.items.add(placeable)
            row.width += if (row.items.size == 1) placeable.width else horizontalGapPx + placeable.width
            row.height = maxOf(row.height, placeable.height)
        }

        val totalHeight =
            rows.sumOf { it.height } + verticalGapPx * (rows.size - 1).coerceAtLeast(0)

        layout(maxWidth, totalHeight) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                row.items.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + horizontalGapPx
                }
                y += row.height + verticalGapPx
            }
        }
    }
}

@Composable
fun TagChip(tag: WorkoutTag, onEditClick: () -> Unit) {
    val tagColor = try {
        Color(tag.color.toColorInt())
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Surface(
        color = GraySurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(tagColor)
            )
            Spacer(Modifier.width(6.dp))

            Text(
                text = tag.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )
            if (tag.editable) {
                Spacer(Modifier.width(6.dp))
                IconButton(onClick = onEditClick, modifier = Modifier.size(18.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать тег",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}