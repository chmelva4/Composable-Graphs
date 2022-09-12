package com.jaikeerthick.composable_graphs.composables

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels

class BasicChartDrawer(
    val scope: DrawScope,
    val gridWidth: Float,
    val gridHeight: Float,
    val verticalStep: Float,
    val yAxisLabels: YAxisLabels,
    val dataCount: Int
) {

}