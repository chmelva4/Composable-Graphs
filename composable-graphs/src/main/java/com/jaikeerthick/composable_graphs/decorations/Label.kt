package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
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
        basicChartDrawer.scope.drawLabel(this, basicChartDrawer)
    }
}

fun DrawScope.drawLabel(label: Label, basicChartDrawer: BasicChartDrawer) {
    val textPaint = Paint()

    val align = when(label.xPosition) {
         is LabelXPosition.PaddingPosition -> when(label.xPosition.type) {
             PaddingPositionType.LEFT -> Paint.Align.LEFT
             else -> Paint.Align.RIGHT
         }
        else -> Paint.Align.CENTER
    }

    textPaint.apply {
        color = label.textColor
        textAlign = align
        textSize = 12.sp.toPx()
    }

    val textBounds = Rect()
    textPaint.getTextBounds(label.text, 0, label.text.length, textBounds)

    val rectTop = label.yPosition.getYValue(basicChartDrawer) - (textBounds.height() / 2) - 10
    val rectLeft = when(label.xPosition) {
        is LabelXPosition.PaddingPosition -> when(label.xPosition.type) {
            PaddingPositionType.LEFT -> label.xPosition.getXValue(basicChartDrawer) - 10
            else -> label.xPosition.getXValue(basicChartDrawer) - textBounds.width() - 10
        }
        else -> label.xPosition.getXValue(basicChartDrawer) - textBounds.exactCenterX() - 10
    }

    drawRect(
        label.backgroundColor,
        Offset(rectLeft, rectTop),
        Size(textBounds.width().toFloat() + 20, textBounds.height().toFloat() + 20)
    )

    drawContext.canvas.nativeCanvas.drawText(
        label.text,
        label.xPosition.getXValue(basicChartDrawer),
        label.yPosition.getYValue(basicChartDrawer) - textBounds.exactCenterY(),
        textPaint
    )


    //        basicChartDrawer.scope.drawContext.canvas.nativeCanvas.drawText()
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
            return chartXToCanvasX(x - 1, basicChartDrawer)
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