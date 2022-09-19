package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer
import com.jaikeerthick.composable_graphs.composables.chartXToCanvasX

data class VerticalGridLines(
    val color: Color = Color.LightGray,
    val widthPx: Int = 2
): CanvasDrawable {
    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawVerticalGridLines(
            this,
            basicChartDrawer
        )
    }
}

fun DrawScope.drawVerticalGridLines(gridLines: VerticalGridLines, basicChartDrawer: BasicChartDrawer) {

    for (i in 0 until basicChartDrawer.xAxisLabels.labels.size) {

        val x = basicChartDrawer.paddingLeftPx + (basicChartDrawer.xItemSpacing * i) + basicChartDrawer.xItemSpacing / 2


        drawLine(
            color = gridLines.color,
            start = Offset( x, basicChartDrawer.paddingTopPx),
            end = Offset(x, basicChartDrawer.paddingTopPx + basicChartDrawer.gridHeight),
            strokeWidth = gridLines.widthPx.toFloat()
        )
    }

}