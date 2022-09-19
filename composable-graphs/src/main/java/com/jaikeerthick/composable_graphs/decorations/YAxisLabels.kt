package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer
import com.jaikeerthick.composable_graphs.composables.chartYtoCanvasY
import com.jaikeerthick.composable_graphs.helper.GraphHelper
import kotlin.math.roundToInt

data class YAxisLabels(
    val labels: List<String>,
    val color: Int = Color.Black.toArgb()
): CanvasDrawable {
    companion object {
        fun fromGraphInputs(inputs: List<Number>): YAxisLabels {
            val absMaxY = GraphHelper.getAbsoluteMax(inputs)
            val verticalStep = absMaxY.toInt() / inputs.size.toFloat()

            val yAxisLabelList = mutableListOf<String>()


            for (i in 0..inputs.size) {
                yAxisLabelList.add((verticalStep * i).roundToInt().toString())
            }
            return YAxisLabels(yAxisLabelList)
        }
    }

    override fun drawToCanvas(basicChartDrawer: BasicChartDrawer) {
        basicChartDrawer.scope.drawYAxisLabels(
            this, basicChartDrawer
        )
    }
}

fun DrawScope.drawYAxisLabels(labels: YAxisLabels, basicChartDrawer: BasicChartDrawer) {

    labels.labels.forEachIndexed {idx, label ->

        drawContext.canvas.nativeCanvas.drawText(
            label,
            basicChartDrawer.paddingLeftPx + basicChartDrawer.gridWidth,
            chartYtoCanvasY(basicChartDrawer.yItemSpacing * idx, basicChartDrawer), //y
            Paint().apply {
                color = labels.color
                textAlign = Paint.Align.CENTER
                textSize = 12.sp.toPx()
            }
        )

    }
}