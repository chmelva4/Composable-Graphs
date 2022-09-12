package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.color.LightGray

data class VerticalGridLines(
    val color: Color = Color.LightGray,
    val widthDp: Int = 2
)

fun DrawScope.drawVerticalGridLines(gridLines: VerticalGridLines, count: Int, xItemSpacing: Float, gridHeight: Float) {

    for (i in 0 until count) {
        drawLine(
            color = gridLines.color,
            start = Offset(xItemSpacing * (i), 0f),
            end = Offset(xItemSpacing * (i), gridHeight),
            strokeWidth = gridLines.widthDp.dp.toPx()
        )
    }

}