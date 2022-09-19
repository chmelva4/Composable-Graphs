package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer
import com.jaikeerthick.composable_graphs.composables.chartXToCanvasX
import com.jaikeerthick.composable_graphs.composables.chartYtoCanvasY

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

    for (i in 0 until basicChartDrawer.yAxisLabels.labels.size) {
        drawLine(
            color = gridLines.color,
            start = Offset(chartXToCanvasX(0f, basicChartDrawer), chartYtoCanvasY(basicChartDrawer.yItemSpacing * i, basicChartDrawer)),
            end = Offset(basicChartDrawer.gridWidth, chartYtoCanvasY(basicChartDrawer.yItemSpacing * i, basicChartDrawer)),
            strokeWidth = gridLines.heightPx.toFloat()
        )
    }

}