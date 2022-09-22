package com.jaikeerthick.composable_graphs.charts.doublePointChart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.color.DeepPurple
import com.jaikeerthick.composable_graphs.color.GraphAccent
import com.jaikeerthick.composable_graphs.color.GraphAccent2

data class DoublePointChartDataPointStyle(
    val bottomPointColor: Color = GraphAccent,
    val topPointColor: Color = GraphAccent2,
    val lineColor: Color = DeepPurple,

    val topPointRadius: Dp = 3.dp,
    val bottomPointRadius: Dp = 3.dp,
    val lineWidth: Dp = 2.dp,
)