@file:OptIn(ExperimentalUnitApi::class)

package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.chartXToCanvasX
import com.jaikeerthick.composable_graphs.charts.chartYtoCanvasY
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer

class Label(
    val text: String,
    val xPosition: LabelXPosition,
    val yPosition: LabelYPosition,
    val backgroundColor: Color = Color.Transparent,
    val padding: PaddingValues = PaddingValues(Dp(4f)),
    val textColor: Int = Color.Black.toArgb(),
    val textSize: TextUnit = TextUnit(12f, TextUnitType.Sp),
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
        textSize = label.textSize.toPx()
    }

    val textBounds = Rect()
    textPaint.getTextBounds(label.text, 0, label.text.length, textBounds)

    val rectTop = label.yPosition.getYValue(basicChartDrawer) - textBounds.height() / 2 - label.padding.calculateTopPadding().toPx()
    val rectLeft = when(label.xPosition) {
        is LabelXPosition.PaddingPosition -> when(label.xPosition.type) {
            PaddingPositionType.LEFT -> label.xPosition.getXValue(basicChartDrawer) - label.padding.calculateLeftPadding(LayoutDirection.Ltr).toPx()
            else -> label.xPosition.getXValue(basicChartDrawer) - textBounds.width() - label.padding.calculateLeftPadding(LayoutDirection.Ltr).toPx()
        }
        else -> label.xPosition.getXValue(basicChartDrawer) - textBounds.exactCenterX() - label.padding.calculateLeftPadding(LayoutDirection.Ltr).toPx()
    }

    drawRect(
        label.backgroundColor,
        Offset(rectLeft, rectTop),
        Size(
            textBounds.width().toFloat()
                    + label.padding.calculateLeftPadding(LayoutDirection.Ltr).toPx()
                    + label.padding.calculateRightPadding(LayoutDirection.Ltr).toPx(),
            textBounds.height().toFloat()
                    + label.padding.calculateTopPadding().toPx()
                    + label.padding.calculateBottomPadding().toPx()
        )
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