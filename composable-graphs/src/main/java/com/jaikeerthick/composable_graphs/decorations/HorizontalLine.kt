package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer
import com.jaikeerthick.composable_graphs.composables.chartXToCanvasX
import com.jaikeerthick.composable_graphs.composables.chartYtoCanvasY

data class HorizontalLine(val y: Float, val color: Color, val widthPx: Float,  val style: HorizontalLineStyle = HorizontalLineStyle.FULL): CanvasDrawable {

    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawHorizontalLine(this, basicChartDrawer)
    }
}

enum class HorizontalLineStyle { FULL, DASHED }

fun DrawScope.drawHorizontalLine(
    line: HorizontalLine, basicChartDrawer: BasicChartDrawer
) {

    val pathEffect = when (line.style) {
        HorizontalLineStyle.DASHED -> PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
        else -> null
    }

    drawLine(
        color = line.color,
        start = Offset(basicChartDrawer.paddingLeftPx, chartYtoCanvasY(line.y, basicChartDrawer)),
        end = Offset(basicChartDrawer.paddingLeftPx + basicChartDrawer.gridWidth, chartYtoCanvasY(line.y, basicChartDrawer)),
        strokeWidth = line.widthPx,
        pathEffect = pathEffect
    )
}