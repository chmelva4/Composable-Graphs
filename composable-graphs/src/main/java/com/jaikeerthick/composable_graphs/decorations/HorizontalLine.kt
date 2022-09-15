package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer

data class HorizontalLine(val y: Float, val color: Color, val widthPx: Float,  val style: HorizontalLineStyle = HorizontalLineStyle.FULL): CanvasDrawable {

    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawHorizontalLine(this, basicChartDrawer.verticalStep, basicChartDrawer.yItemSpacing, basicChartDrawer.gridWidth, basicChartDrawer.gridHeight)
    }
}

enum class HorizontalLineStyle { FULL, DASHED }

fun DrawScope.drawHorizontalLine(
    line: HorizontalLine, verticalStep: Float, yItemSpacing: Float, gridWidth: Float, gridHeight: Float
) {

    val pathEffect = when (line.style) {
        HorizontalLineStyle.DASHED -> PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
        else -> null
    }

    drawLine(
        color = line.color,
        start = Offset(0f, gridHeight - (yItemSpacing * (line.y) / verticalStep)),
        end = Offset(gridWidth, gridHeight - (yItemSpacing * (line.y) / verticalStep)),
        strokeWidth = line.widthPx,
        pathEffect = pathEffect
    )
}