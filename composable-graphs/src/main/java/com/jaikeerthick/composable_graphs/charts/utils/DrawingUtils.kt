package com.jaikeerthick.composable_graphs.charts.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer

fun drawPaddings(basicChartDrawer: BasicChartDrawer) {

    val color = Color.Red.copy(0.2f)

    basicChartDrawer.scope.drawRect(
        color, Offset(0f, 0f), Size(width = basicChartDrawer.canvasSize.width, height = basicChartDrawer.paddingTopPx)
    )
    basicChartDrawer.scope.drawRect(
        color, Offset(0f, 0f), Size(width = basicChartDrawer.paddingLeftPx, height = basicChartDrawer.canvasSize.height)
    )
    basicChartDrawer.scope.drawRect(
        color,
        Offset(0f, basicChartDrawer.paddingTopPx + basicChartDrawer.gridHeight),
        Size(width = basicChartDrawer.canvasSize.width, height = basicChartDrawer.paddingBottomPx)
    )
    basicChartDrawer.scope.drawRect(
        color,
        Offset(basicChartDrawer.paddingLeftPx + basicChartDrawer.gridWidth, 0f),
        Size(width = basicChartDrawer.paddingRightPx, height = basicChartDrawer.canvasSize.height)
    )
}

fun Brush.Companion.fromColor(color: Color): Brush = horizontalGradient(listOf(color, color))