package com.jaikeerthick.composable_graphs.charts.common

import android.graphics.Point
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import com.jaikeerthick.composable_graphs.color.GraphAccent

sealed class PointStyle {

    abstract fun drawToCanvas(x: Float, y: Float, basicChartDrawer: BasicChartDrawer)


    class FilledPoint(val radiusPx: Float, val color: Color = GraphAccent): PointStyle() {
        override fun drawToCanvas(x: Float, y: Float, basicChartDrawer: BasicChartDrawer) {

            basicChartDrawer.scope.drawCircle(
                color = basicChartDrawer.canvasBgColor,
                radius = radiusPx,
                center = Offset(x, y)
            )

            basicChartDrawer.scope.drawCircle(
                color = color,
                radius = radiusPx,
                center = Offset(x, y)
            )
        }
    }

    class OutlinedPoint(val radiusPx: Float, val color: Color = GraphAccent, val strokeWidthPx: Float): PointStyle() {
        override fun drawToCanvas(x: Float, y: Float, basicChartDrawer: BasicChartDrawer) {

            basicChartDrawer.scope.drawCircle(
                color = basicChartDrawer.canvasBgColor,
                radius = radiusPx,
                center = Offset(x, y)
            )

            basicChartDrawer.scope.drawCircle(
                color = color,
                radius = radiusPx,
                center = Offset(x, y),
                style = Stroke(strokeWidthPx)
            )
        }
    }

}
