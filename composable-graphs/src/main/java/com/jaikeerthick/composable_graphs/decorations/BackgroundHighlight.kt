package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer
import com.jaikeerthick.composable_graphs.composables.chartXToCanvasX
import com.jaikeerthick.composable_graphs.composables.chartYtoCanvasY

data class BackgroundHighlight (
    val yStart: Float,
    val yEnd: Float,
    val color: Color,
): CanvasDrawable {
    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawBackgroundHighlight(
            this,
           basicChartDrawer
        )
    }
}

fun DrawScope.drawBackgroundHighlight(
    highlight: BackgroundHighlight, basicChartDrawer: BasicChartDrawer
) {
    drawRect(
        color = highlight.color,
        topLeft = Offset(chartXToCanvasX(0f, basicChartDrawer), chartYtoCanvasY(highlight.yEnd, basicChartDrawer)),
        size = Size(basicChartDrawer.gridWidth,  (basicChartDrawer.yItemSpacing * (highlight.yEnd - highlight.yStart) / basicChartDrawer.verticalStep))
    )
}