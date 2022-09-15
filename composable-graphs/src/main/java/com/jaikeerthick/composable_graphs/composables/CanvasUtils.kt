package com.jaikeerthick.composable_graphs.composables

fun chartXToCanvasX(x: Float, basicChartDrawer: BasicChartDrawer): Float =
    basicChartDrawer.paddingLeftPx + basicChartDrawer.xItemSpacing * x

fun chartYtoCanvasY(y: Float, basicChartDrawer: BasicChartDrawer): Float =
    basicChartDrawer.paddingTopPx + basicChartDrawer.gridHeight - ( basicChartDrawer.yItemSpacing * (y / basicChartDrawer.verticalStep))