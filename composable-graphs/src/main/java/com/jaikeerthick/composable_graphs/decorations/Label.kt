package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.jaikeerthick.composable_graphs.charts.chartXToCanvasX
import com.jaikeerthick.composable_graphs.charts.chartYtoCanvasY
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer

class Label(
    val text: String,
    val xPosition: LabelXPosition,
    val yPosition: LabelYPosition,
    val backgroundColor: Color,
    val textColor: Int = Color.Black.toArgb(),
): CanvasDrawable
{
    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {

        val p = Paint()


//        basicChartDrawer.scope.drawContext.canvas.nativeCanvas.drawText()

    }
}

sealed class LabelXPosition() {

    abstract fun getXValue(basicChartDrawer: BasicChartDrawer): Float

    class CanvasPosition(val x: Float): LabelXPosition() {
        override fun getXValue(basicChartDrawer: BasicChartDrawer): Float {
            return x
        }
    }

    class ChartPosition(val x: Float): LabelXPosition() {
        override fun getXValue(basicChartDrawer: BasicChartDrawer): Float {
            return chartXToCanvasX(x, basicChartDrawer)
        }
    }

    class PaddingPosition(val type: PaddingPositionType): LabelXPosition() {
        override fun getXValue(basicChartDrawer: BasicChartDrawer): Float {
            return when(type) {
                PaddingPositionType.LEFT -> 0f
                else -> basicChartDrawer.canvasSize.width
            }
        }
    }
}

sealed class LabelYPosition() {

    abstract fun getYValue(basicChartDrawer: BasicChartDrawer): Float

    class CanvasPosition(val y: Float): LabelYPosition() {
        override fun getYValue(basicChartDrawer: BasicChartDrawer): Float {
            return y
        }
    }

    class ChartPosition(val y: Float): LabelYPosition() {
        override fun getYValue(basicChartDrawer: BasicChartDrawer): Float {
            return chartYtoCanvasY(y, basicChartDrawer)
        }
    }

    class PaddingPosition(val type: PaddingPositionType): LabelYPosition() {
        override fun getYValue(basicChartDrawer: BasicChartDrawer): Float {
            return when(type) {
                PaddingPositionType.TOP -> 0f
                else -> basicChartDrawer.canvasSize.height
            }
        }
    }
}

enum class PaddingPositionType {LEFT, RIGHT, TOP, BOTTOM}