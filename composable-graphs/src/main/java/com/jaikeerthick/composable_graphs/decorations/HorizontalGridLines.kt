package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

data class HorizontalGridLines(
    val color: Color = Color.LightGray,
    val heightDp: Int = 2
)

fun DrawScope.drawHorizontalGridLines(gridLines: HorizontalGridLines, count: Int, yItemSpacing: Float, gridHeight: Float, gridWidth: Float) {

    for (i in 0 until count) {
        drawLine(
            color = gridLines.color,
            start = Offset(0f, gridHeight - yItemSpacing * (i + 0)),
            end = Offset(gridWidth, (gridHeight) - yItemSpacing * (i + 0)),
            strokeWidth = gridLines.heightDp.dp.toPx()
        )
    }

}