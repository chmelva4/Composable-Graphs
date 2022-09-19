package com.jaikeerthick.composable_graphs.charts

import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer

fun chartXToCanvasX(x: Float, basicChartDrawer: BasicChartDrawer): Float =
    basicChartDrawer.paddingLeftPx + basicChartDrawer.xDataOffset + basicChartDrawer.xItemSpacing * x

fun chartYtoCanvasY(y: Float, basicChartDrawer: BasicChartDrawer): Float =
    basicChartDrawer.paddingTopPx + basicChartDrawer.gridHeight - ( basicChartDrawer.yItemSpacing * (y / basicChartDrawer.verticalStep))