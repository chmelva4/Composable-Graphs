package com.jaikeerthick.composable_graphs.charts.common

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.charts.utils.GraphHelper

open class BasicChartDrawer(
    val scope: DrawScope,
    val canvasSize: Size,
    val paddingLeftPx: Float,
    val paddingRightPx: Float,
    val paddingTopPx: Float,
    val paddingBottomPx: Float,
    val xAxisLabels: XAxisLabels,
    val yAxisLabels: YAxisLabels,
    val dataList: List<Number>,
    val customXDataOffset: Float? = null,
    val customXLabelOffset: Float? = null,
) {

    val gridWidth: Float
        get() = canvasSize.width - paddingLeftPx - paddingRightPx

    val gridHeight: Float
        get() = canvasSize.height - paddingTopPx - paddingBottomPx

    val xItemSpacing: Float
        get() = gridWidth / if (dataList.isNotEmpty()) dataList.size else 1

    val yItemSpacing: Float
        get() = gridHeight / if (yAxisLabels.labels.isNotEmpty()) yAxisLabels.labels.size else 1

    val verticalStep: Float
        get() = GraphHelper.getAbsoluteMax(dataList).toFloat() / if (dataList.isNotEmpty()) dataList.size.toFloat() else 1f

    val yMax: Float
        get() = GraphHelper.getAbsoluteMax(dataList).toFloat()

    val maxX: Float
        get() = (dataList.size - 1).toFloat()

    val xLabelOffset: Float
        get() = customXLabelOffset ?: (xItemSpacing / 2)

    val xDataOffset: Float
        get() = customXDataOffset ?: (xItemSpacing / 2)
}