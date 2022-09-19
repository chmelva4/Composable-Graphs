package com.jaikeerthick.composable_graphs.charts.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.decorations.YAxisLabelsPosition

open class BasicChartStyle(
    val paddingValues: PaddingValues = PaddingValues(
        all = 12.dp
    ),
    val canvasPaddingValues: PaddingValues = PaddingValues(
        start = 20.dp, end = 20.dp, top = 0.dp, bottom = 20.dp
    ),
    val height: Dp = 300.dp,

    val isYAxisLabelVisible: Boolean = false,
    val isXAxisLabelVisible: Boolean = true,
    val isHeaderVisible: Boolean = false,

    val backgroundColor: Color = Color.Transparent,
    val xAxisTextColor: Int = android.graphics.Color.GRAY,
    val yAxisTextColor: Int = android.graphics.Color.BLACK,

    val yAxisLabelsPosition: YAxisLabelsPosition = YAxisLabelsPosition.LEFT
) {

}