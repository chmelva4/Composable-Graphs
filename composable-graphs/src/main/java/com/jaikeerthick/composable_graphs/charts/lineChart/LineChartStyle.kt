package com.jaikeerthick.composable_graphs.charts.lineChart

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.charts.common.BasicChartStyle
import com.jaikeerthick.composable_graphs.color.GraphAccent
import com.jaikeerthick.composable_graphs.color.PointHighlight

class LineChartStyle(
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
    yAxisTextColor: Int = android.graphics.Color.GRAY,

    val isCrossHairVisible: Boolean = false,
    val lineColor: Color = GraphAccent,
    val clickHighlightColor: Color = PointHighlight,
    val crossHairColor: Color = Color.LightGray,
    val fillGradient: Brush = Brush.verticalGradient(
        listOf(Color.Transparent, Color.Transparent)
    ),
    val defaultColors: LineChartColors = LineChartColors()

): BasicChartStyle(
    paddingValues,
    canvasPaddingValues,
    height,
    isYAxisLabelVisible,
    isXAxisLabelVisible,
    isHeaderVisible,
    backgroundColor,
    xAxisTextColor,
    yAxisTextColor
) {

}
