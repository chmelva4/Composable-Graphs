package com.jaikeerthick.composable_graphs.decorations

import android.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.helper.GraphHelper
import kotlin.math.roundToInt

data class YAxisLabels(
    val labels: List<String>
) {
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
}

fun DrawScope.drawYAxisLabels(labels: YAxisLabels, yItemSpacing: Float, gridHeight: Float, textColor: Int) {

    labels.labels.forEachIndexed {idx, label ->

        drawContext.canvas.nativeCanvas.drawText(
            label,
            size.width, //x
            gridHeight - yItemSpacing * (idx + 0), //y
            Paint().apply {
                color = textColor
                textAlign = Paint.Align.CENTER
                textSize = 12.sp.toPx()
            }
        )

    }
}