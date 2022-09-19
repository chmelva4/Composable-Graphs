package com.jaikeerthick.composable_graphs.charts.doublePointChart

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.charts.common.BasicChartStyle
import com.jaikeerthick.composable_graphs.decorations.YAxisLabelsPosition

class DoublePointChartStyle(
    paddingValues: PaddingValues = PaddingValues(
        all = 12.dp
    ),
    canvasPaddingValues: PaddingValues = PaddingValues(
        start = 20.dp, end = 20.dp, top = 0.dp, bottom = 20.dp
    ),
    height: Dp = 300.dp,
    isYAxisLabelVisible: Boolean = false,
    isXAxisLabelVisible: Boolean = true,
    isHeaderVisible: Boolean = false,
    backgroundColor: Color = Color.Transparent,
    xAxisTextColor: Int = android.graphics.Color.GRAY,
    yAxisTextColor: Int = android.graphics.Color.BLACK,
    yAxisLabelsPosition: YAxisLabelsPosition = YAxisLabelsPosition.LEFT
): BasicChartStyle(
    paddingValues,
    canvasPaddingValues,
    height,
    isYAxisLabelVisible,
    isXAxisLabelVisible,
    isHeaderVisible,
    backgroundColor,
    xAxisTextColor,
    yAxisTextColor,
    yAxisLabelsPosition
) {}