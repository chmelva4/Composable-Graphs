package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.data.GraphData

enum class xAxisLabelsPosition {
    TOP, BOTTOM
}

data class XAxisLabels(
    val labels: List<GraphData>,
    val position: xAxisLabelsPosition = xAxisLabelsPosition.BOTTOM
)

fun DrawScope.drawXAxisLabels(labels: XAxisLabels, xItemSpacing: Float, xLabelOffset: Float, textColor: Int) {

    labels.labels.forEachIndexed {idx, label ->

        drawContext.canvas.nativeCanvas.drawText(
            label.text,
            (xItemSpacing * (idx)) + xLabelOffset, // x
            size.height, // y
            Paint().apply {
                color = textColor
                textAlign = Paint.Align.CENTER
                textSize = 12.sp.toPx()
            }
        )

    }
}