package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

data class BackgroundHighlight (
    val yStart: Float,
    val yEnd: Float,
    val color: Color,
) {
}

fun DrawScope.drawBackgroundHighlight(highlight: BackgroundHighlight, verticalStep: Float, yItemSpacing: Float, gridWidth: Float, gridHeight: Float) {
    drawRect(
        color = highlight.color,
        topLeft = Offset(0f, gridHeight - (yItemSpacing * (highlight.yEnd) / verticalStep)),
        size = Size(gridWidth,  (yItemSpacing * (highlight.yEnd - highlight.yStart) / verticalStep))
    )
}