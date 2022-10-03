package com.jaikeerthick.composable_graphs.charts.doublePointChart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.charts.common.PointStyle
import com.jaikeerthick.composable_graphs.color.DeepPurple
import com.jaikeerthick.composable_graphs.color.GraphAccent
import com.jaikeerthick.composable_graphs.color.GraphAccent2

data class DoublePointChartDataPointStyle(

    val bottomPointStyle: PointStyle = PointStyle.FilledPoint(20f, GraphAccent),
    val topPointStyle: PointStyle = PointStyle.FilledPoint(20f, GraphAccent),

    val lineColor: Color = DeepPurple,
    val lineWidth: Dp = 2.dp,
)