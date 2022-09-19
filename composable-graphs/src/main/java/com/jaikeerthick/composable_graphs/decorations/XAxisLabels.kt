package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer
import com.jaikeerthick.composable_graphs.data.GraphData

enum class xAxisLabelsPosition {
    TOP, BOTTOM
}

data class XAxisLabels(
    val labels: List<GraphData>,
    val position: xAxisLabelsPosition = xAxisLabelsPosition.BOTTOM,
    val color: Int = Color.Black.toArgb(),
): CanvasDrawable {

    companion object {
        fun createDefault(data: List<Number>): XAxisLabels {
            return XAxisLabels(data.mapIndexed {idx, _ -> GraphData.Number(idx + 1)})
        }
    }

    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawXAxisLabels(
            this, basicChartDrawer
        )
    }
}

fun DrawScope.drawXAxisLabels(labels: XAxisLabels, basicChartDrawer: BasicChartDrawer) {

    labels.labels.forEachIndexed {idx, label ->

        drawContext.canvas.nativeCanvas.drawText(
            label.text,
            (basicChartDrawer.xItemSpacing * (idx)) + basicChartDrawer.xLabelOffset + basicChartDrawer.paddingLeftPx, // x
            size.height, // y
            Paint().apply {
                color = labels.color
                textAlign = Paint.Align.CENTER
                textSize = 12.sp.toPx()
            }
        )

    }
}