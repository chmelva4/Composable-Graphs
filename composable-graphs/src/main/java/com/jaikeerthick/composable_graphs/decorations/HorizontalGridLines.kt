package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer
import com.jaikeerthick.composable_graphs.charts.chartYtoCanvasY

data class HorizontalGridLines(
    val color: Color = Color.LightGray,
    val heightPx: Int = 2
): CanvasDrawable {
    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawHorizontalGridLines(
            this,
            basicChartDrawer
        )
    }
}

fun DrawScope.drawHorizontalGridLines(gridLines: HorizontalGridLines, basicChartDrawer: BasicChartDrawer) {

    for (label in basicChartDrawer.yAxisLabels.labels) {
        drawLine(
            color = gridLines.color,
            start = Offset(basicChartDrawer.paddingLeftPx, chartYtoCanvasY(label.toFloat(), basicChartDrawer)),
            end = Offset(basicChartDrawer.paddingLeftPx + basicChartDrawer.gridWidth , chartYtoCanvasY(label.toFloat(), basicChartDrawer)),
            strokeWidth = gridLines.heightPx.toFloat()
        )
    }

}