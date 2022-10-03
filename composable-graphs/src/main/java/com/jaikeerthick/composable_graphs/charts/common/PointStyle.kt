package com.jaikeerthick.composable_graphs.charts.common

import android.graphics.Point
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
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

    class ImagePointStyle(
        val image: Painter,
        val widthPx: Float,
        val heightPx: Float,
        val tintColor: Color = Color.Black
    ): PointStyle() {
        override fun drawToCanvas(x: Float, y: Float, basicChartDrawer: BasicChartDrawer) {

            basicChartDrawer.scope.drawRect(basicChartDrawer.canvasBgColor, Offset(x - widthPx /2, y - heightPx / 2), Size(widthPx, heightPx))


            with(image) {
                basicChartDrawer.scope.translate(x - widthPx / 2, y - heightPx / 2) {
                    basicChartDrawer.scope.draw(Size(widthPx, heightPx), colorFilter = ColorFilter.tint(tintColor))
                }
            }

        }
    }

}

fun DrawScope.drawImagePointStyle(imagePointStyle: PointStyle.ImagePointStyle, basicChartDrawer: BasicChartDrawer) {
//    imagePointStyle.painter.
}
