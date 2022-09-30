package com.jaikeerthick.composable_graphs.charts.doublePointChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.charts.chartXToCanvasX
import com.jaikeerthick.composable_graphs.charts.chartYtoCanvasY
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer
import com.jaikeerthick.composable_graphs.charts.common.YScale
import com.jaikeerthick.composable_graphs.charts.drawPaddings
import com.jaikeerthick.composable_graphs.decorations.CanvasDrawable
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.XAxisLabelsPosition
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabelsPosition

/**
 * A minimal and stunning Graph
 */
@Composable
fun DoublePointChart(
    data: List<Pair<Number, Number>>,
    style: DoublePointChartStyle = DoublePointChartStyle(),
    xAxisLabels: XAxisLabels? = null,
    yAxisLabels: YAxisLabels = YAxisLabels.fromGraphInputs(data.map { it.first } + data.map { it.second }, style.yAxisTextColor, YAxisLabelsPosition.LEFT),
    yScale: YScale = YScale.ZeroToMaxScale(),
    header: @Composable() () -> Unit = {},

    decorations: List<CanvasDrawable> = emptyList<CanvasDrawable>(),
    dataPointStyles: Map<Int, DoublePointChartDataPointStyle> = emptyMap(),
) {

    val offsetList = remember { mutableListOf<Pair<Offset, Offset>>()}

    val currentDensity = LocalDensity.current

    Column(
        modifier = Modifier
            .background(
                color = style.backgroundColor
            )
            .fillMaxWidth()
            .padding(style.paddingValues)
            .wrapContentHeight()
    ) {

        if (style.isHeaderVisible){
            header()
        }


        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(style.height)
                .padding(horizontal = 1.dp)
                .padding(top = 12.dp) // to prevent overlap with header
                ,
        ) {

            val maxList = data.map { it.second }
            yScale.setupValuesFromData(maxList)
            val presentXAxisLabels = xAxisLabels ?: XAxisLabels.createDefault(maxList, XAxisLabelsPosition.BOTTOM, style.xAxisTextColor)
            val basicDrawer = BasicChartDrawer(
                this,
                size,
                style.canvasPaddingValues.calculateLeftPadding(LayoutDirection.Ltr).toPx(),
                style.canvasPaddingValues.calculateLeftPadding(LayoutDirection.Ltr).toPx(),
                style.canvasPaddingValues.calculateTopPadding().toPx(),
                style.canvasPaddingValues.calculateBottomPadding().toPx(),
                presentXAxisLabels,
                yAxisLabels,
                maxList,
                yScale
            )

            if (style.drawCanvasPaddings) drawPaddings(basicDrawer)
            if (style.isXAxisLabelVisible) presentXAxisLabels.drawToCanvas(basicDrawer)
            if (style.isYAxisLabelVisible) yAxisLabels.drawToCanvas(basicDrawer)

            decorations.forEach { it.drawToCanvas(basicDrawer) }

            constructOffsetList(
                offsetList,
                data,
                basicDrawer
            )

            drawLineConnectingPoints(this, offsetList, dataPointStyles, style.defaultDataPointStyle, currentDensity)
            drawPoints(this, offsetList, dataPointStyles, style.defaultDataPointStyle, currentDensity)
        }

    }

}

private fun constructOffsetList(
    offsetList: MutableList<Pair<Offset, Offset>>,
    data: List<Pair<Number, Number>>,
    basicChartDrawer: BasicChartDrawer,
) {
    offsetList.clear() // clearing list to avoid data duplication during recomposition

    for (i in data.indices) {

        val x1 = chartXToCanvasX(i.toFloat(), basicChartDrawer)
        val y1 = chartYtoCanvasY(data[i].first.toFloat(), basicChartDrawer)
        val y2 = chartYtoCanvasY(data[i].second.toFloat(), basicChartDrawer)

        offsetList.add(
            Pair(Offset(x1, y1), Offset(x1, y2))
        )
    }
}

private fun drawPoints(
    scope: DrawScope,
    offsetList: List<Pair<Offset, Offset>>,
    dataPointStyles: Map<Int, DoublePointChartDataPointStyle>,
    defaultStyle: DoublePointChartDataPointStyle,
    currentDensity: Density
) {

    offsetList.forEachIndexed { idx, offset ->

        val style = dataPointStyles.getOrElse(idx) {defaultStyle}

        val bottomRadiusPx = with(currentDensity) { style.bottomPointRadius.toPx() }
        val topRadiusPx = with(currentDensity) { style.topPointRadius.toPx() }

        scope.drawCircle(color = style.bottomPointColor, radius = bottomRadiusPx, center = offset.first)
        scope.drawCircle(color = style.topPointColor, radius = topRadiusPx, center = offset.second) }
}

private fun drawLineConnectingPoints(
    scope: DrawScope,
    offsetList: List<Pair<Offset, Offset>>,
    dataPointStyles: Map<Int, DoublePointChartDataPointStyle>,
    defaultStyle: DoublePointChartDataPointStyle,
    currentDensity: Density
) {

    offsetList.forEachIndexed { idx, points ->

        val style = dataPointStyles.getOrElse(idx) {defaultStyle}

        val lineWidthPx = with(currentDensity) { style.lineWidth.toPx() }

        scope.drawLine(color = style.lineColor, start = points.first, end = points.second, strokeWidth = lineWidthPx)
    }

}




