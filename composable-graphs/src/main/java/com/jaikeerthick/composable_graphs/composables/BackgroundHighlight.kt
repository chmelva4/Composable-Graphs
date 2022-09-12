package com.jaikeerthick.composable_graphs.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class BackgroundHighlight (
    val yStart: Float,
    val yEnd: Float,
    val color: Color,
): CanvasDrawable {

    var verticalStep = 0f
    var gridHeight = 0f
    var yItemSpacing = 0f
    var gridWidth = 0f
//    var yItemSpacing = 0f

    override fun drawToCanvas(scope: DrawScope) {

        scope.drawRect(
            color = color,
            topLeft = Offset(0f, gridHeight - (yItemSpacing * (yEnd) / verticalStep)),
            size = Size(gridWidth,  (yItemSpacing * (yEnd - yStart) / verticalStep))
        )

//        scope.drawRect(
//            color = color,
//            topLeft = Offset(0f, 0f),
//            size = Size(gridWidth, gridHeight)
//        )
    }
}