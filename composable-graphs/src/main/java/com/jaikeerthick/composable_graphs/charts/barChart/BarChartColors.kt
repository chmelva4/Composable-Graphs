package com.jaikeerthick.composable_graphs.charts.barChart

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jaikeerthick.composable_graphs.color.Gradient1
import com.jaikeerthick.composable_graphs.color.Gradient2
import com.jaikeerthick.composable_graphs.color.PointHighlight

data class BarChartColors(

    val fillGradient: Brush = Brush.verticalGradient(
        listOf(Gradient1, Gradient2)
    ),
)
