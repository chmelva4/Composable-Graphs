package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer
import com.jaikeerthick.composable_graphs.charts.chartYtoCanvasY
import com.jaikeerthick.composable_graphs.charts.utils.GraphHelper
import kotlin.math.roundToInt

data class YAxisLabels(
    val labels: List<AxisLabelItem>,
    val color: Int = Color.Black.toArgb(),
    val position: YAxisLabelsPosition = YAxisLabelsPosition.LEFT,
): CanvasDrawable {
    companion object {
        fun fromGraphInputs(inputs: List<Number>, color: Int, position: YAxisLabelsPosition): YAxisLabels {
            val absMaxY = GraphHelper.getAbsoluteMax(inputs)
            val verticalStep = absMaxY.toInt() / inputs.size.toFloat()

            val yAxisLabelList = mutableListOf<AxisLabelItem>()


            for (i in 0..inputs.size) {
                yAxisLabelList.add(AxisLabelItem(verticalStep * i))
            }
            return YAxisLabels(yAxisLabelList, color, position)
        }
    }

    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawYAxisLabels(
            this, basicChartDrawer
        )
    }
}

enum class YAxisLabelsPosition {LEFT, RIGHT}

fun DrawScope.drawYAxisLabels(labels: YAxisLabels, basicChartDrawer: BasicChartDrawer) {

    val x = if (labels.position == YAxisLabelsPosition.LEFT) 0 else basicChartDrawer.canvasSize.width
    val align = if (labels.position == YAxisLabelsPosition.LEFT) Paint.Align.LEFT else Paint.Align.RIGHT

    val textPaint = Paint()
    textPaint.apply {
        color = labels.color
        textAlign = align
        textSize = 12.sp.toPx()
    }

    labels.labels.forEachIndexed {idx, label ->

//        Log.d("DECORATION", "draw y label val: $label locaction x: ${basicChartDrawer.paddingLeftPx + basicChartDrawer.gridWidth} y: ${basicChartDrawer.yItemSpacing * idx}")

        val textBounds = Rect()
        textPaint.getTextBounds(label.label, 0, label.label.length, textBounds)

        drawContext.canvas.nativeCanvas.drawText(
            label.label,
            x.toFloat(),
            chartYtoCanvasY(label.chartValue, basicChartDrawer) - textBounds.exactCenterY(), //y
            textPaint
        )

    }
}