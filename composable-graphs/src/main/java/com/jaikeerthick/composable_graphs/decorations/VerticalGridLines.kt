package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer

data class VerticalGridLines(
    val color: Color = Color.LightGray,
    val widthPx: Int = 2
): CanvasDrawable {
    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawVerticalGridLines(
            this,
            basicChartDrawer.yAxisLabels.labels.size,
            basicChartDrawer.xItemSpacing,
            basicChartDrawer.gridHeight,
              basicChartDrawer.gridHeight - basicChartDrawer.yItemSpacing * (basicChartDrawer.yAxisLabels.labels.size - 1)
        )
    }
}

fun DrawScope.drawVerticalGridLines(gridLines: VerticalGridLines, count: Int, xItemSpacing: Float, gridHeight: Float, verticalStart: Float) {

    for (i in 0 until count) {
        drawLine(
            color = gridLines.color,
            start = Offset(xItemSpacing * (i), verticalStart),
            end = Offset(xItemSpacing * (i), gridHeight),
            strokeWidth = gridLines.widthPx.toFloat()
        )
    }

}