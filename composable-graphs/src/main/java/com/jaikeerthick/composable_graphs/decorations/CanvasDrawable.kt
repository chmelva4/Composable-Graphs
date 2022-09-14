package com.jaikeerthick.composable_graphs.decorations

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jaikeerthick.composable_graphs.composables.BasicChartDrawer

interface CanvasDrawable {

    fun drawToCanvas(basicChartDrawer: BasicChartDrawer)
}

