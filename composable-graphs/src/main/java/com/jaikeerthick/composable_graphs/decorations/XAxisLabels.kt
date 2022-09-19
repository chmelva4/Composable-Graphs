package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer
import com.jaikeerthick.composable_graphs.charts.common.GraphData



data class XAxisLabels(
    val labels: List<GraphData>,
    val position: XAxisLabelsPosition = XAxisLabelsPosition.BOTTOM,
    val color: Int = Color.Black.toArgb(),
): CanvasDrawable {

    companion object {
        fun createDefault(data: List<Number>, position: XAxisLabelsPosition, color: Int): XAxisLabels {
            return XAxisLabels(data.mapIndexed {idx, _ -> GraphData.Number(idx + 1)}, position, color)
        }
    }

    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawXAxisLabels(
            this, basicChartDrawer
        )
    }
}

enum class XAxisLabelsPosition {
    TOP, BOTTOM
}

fun DrawScope.drawXAxisLabels(labels: XAxisLabels, basicChartDrawer: BasicChartDrawer) {

    labels.labels.forEachIndexed {idx, label ->

        val y = if (labels.position == XAxisLabelsPosition.BOTTOM) basicChartDrawer.canvasSize.height.toFloat() else 0f

        drawContext.canvas.nativeCanvas.drawText(
            label.text,
            (basicChartDrawer.xItemSpacing * (idx)) + basicChartDrawer.xLabelOffset + basicChartDrawer.paddingLeftPx, // x
            y, // y
            Paint().apply {
                color = labels.color
                textAlign = Paint.Align.CENTER
                textSize = 12.sp.toPx()
            }
        )

    }
}