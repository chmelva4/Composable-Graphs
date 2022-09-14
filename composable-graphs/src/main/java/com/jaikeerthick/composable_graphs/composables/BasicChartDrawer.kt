package com.jaikeerthick.composable_graphs.composables

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.helper.GraphHelper

class BasicChartDrawer(
    val scope: DrawScope,
    val gridWidth: Float,
    val gridHeight: Float,
    val yAxisLabels: YAxisLabels,
    val dataList: List<Number>,
    val horizontalGridLinesCount: Int,
    val xLabelOffset: Float
) {

    val xItemSpacing: Float
        get() = gridWidth / if (dataList.isNotEmpty()) dataList.size else 1

    val yItemSpacing: Float
        get() = gridHeight / if (yAxisLabels.labels.isNotEmpty()) yAxisLabels.labels.size else 1

    val verticalStep: Float
        get() = GraphHelper.getAbsoluteMax(dataList).toFloat() / if (dataList.isNotEmpty()) dataList.size.toFloat() else 1f
}