package com.jaikeerthick.composable_graphs.charts.barChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.charts.chartXToCanvasX
import com.jaikeerthick.composable_graphs.charts.chartYtoCanvasY
import com.jaikeerthick.composable_graphs.charts.common.BasicChartDrawer
import com.jaikeerthick.composable_graphs.charts.common.YScale
import com.jaikeerthick.composable_graphs.charts.utils.drawPaddings
import com.jaikeerthick.composable_graphs.decorations.CanvasDrawable
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.XAxisLabelsPosition
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabelsPosition

@Composable
fun BarChart(
    data: List<Number>,
    style: BarChartStyle = BarChartStyle(),
    xAxisLabels: XAxisLabels? = null,
    yAxisLabels: YAxisLabels = YAxisLabels.fromGraphInputs(data, style.yAxisTextColor, YAxisLabelsPosition.LEFT),
    yScale: YScale = YScale.ZeroToMaxScale(),
    header: @Composable() () -> Unit = {},
    decorations: List<CanvasDrawable> = emptyList<CanvasDrawable>(),
    dataPointsStyles: Map<Int, BarChartDataPointStyle> = emptyMap(),
    onBarClicked: (value: Any) -> Unit = {},
) {

    val barOffsetList = remember { mutableListOf<Pair<Offset, Offset>>() }
    val clickedBar: MutableState<Offset?> = remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .background(
                color = style.backgroundColor
//            Color.LightGray
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
                .pointerInput(true) {

                    detectTapGestures { p1: Offset ->

                        val click = barOffsetList.find {

                            (it.first.x < p1.x) && (it.second.x > p1.x) // check if our touch point is between left and right side of the bar
                                    && (p1.y > it.first.y) // check if our touch point is below the top of the bar
                        }

                        click?.let {

                            val index = barOffsetList.indexOf(it)
                            onBarClicked(data[index])

                            clickedBar.value = it.first
                        }
                    }

                },
        ) {

            val presentXAxisLabels = xAxisLabels?: XAxisLabels.createDefault(data, XAxisLabelsPosition.BOTTOM, style.xAxisTextColor)
            yScale.setupValuesFromData(data)
            val basicDrawer = BasicChartDrawer(
                this,
                size,
                style.canvasPaddingValues.calculateLeftPadding(LayoutDirection.Ltr).toPx(),
                style.canvasPaddingValues.calculateRightPadding(LayoutDirection.Ltr).toPx(),
                style.canvasPaddingValues.calculateTopPadding().toPx(),
                style.canvasPaddingValues.calculateBottomPadding().toPx(),
                presentXAxisLabels,
                yAxisLabels,
                data,
                yScale,
                customXDataOffset = 0f
            )

            if (style.drawCanvasPaddings) drawPaddings(basicDrawer)
            if (style.isXAxisLabelVisible) presentXAxisLabels.drawToCanvas(basicDrawer)
            if (style.isYAxisLabelVisible) yAxisLabels.drawToCanvas(basicDrawer)

            decorations.forEach { it.drawToCanvas(basicDrawer) }

            constructGraph(
                this,
                data,
                barOffsetList,
                basicDrawer,
                dataPointsStyles,
                style.defaultDataPointStyle
            )

            drawClickedRect(this, clickedBar, style.clickHighlightColor, basicDrawer)
        }

    }


}

private fun constructGraph(
    scope: DrawScope,
    dataList: List<Number>,
    barOffsetList: MutableList<Pair<Offset, Offset>>,
    basicChartDrawer: BasicChartDrawer,
    dataPointsStyles: Map<Int, BarChartDataPointStyle>,
    defaultDataPointStyle: BarChartDataPointStyle
) {
    barOffsetList.clear()
    for (i in dataList.indices) {

        val x1 = chartXToCanvasX(i.toFloat(), basicChartDrawer)
        val y1 = chartYtoCanvasY(dataList[i].toFloat(), basicChartDrawer)

        barOffsetList.add(
            Pair(
                first = Offset(
                    x = x1,
                    y = y1
                ),
                second = Offset(
                    x = chartXToCanvasX((i + 1).toFloat(), basicChartDrawer),
                    y = y1
                ),
            )
        )

        val style = dataPointsStyles.getOrElse(i) {defaultDataPointStyle}

        val x = style.barWidth.getLeftSideXCoordinate(chartXToCanvasX((i).toFloat(), basicChartDrawer), chartXToCanvasX((i + 1).toFloat(), basicChartDrawer))
        val width = style.barWidth.getSize(chartXToCanvasX((i).toFloat(), basicChartDrawer), chartXToCanvasX((i + 1).toFloat(), basicChartDrawer))

        scope.drawRoundRect(
            brush = style.fillGradient,
            topLeft = Offset(
                x = x,
                y = y1
            ),
            size = Size(
                width = width,
                height = basicChartDrawer.paddingTopPx +  basicChartDrawer.gridHeight - y1
            ),
            cornerRadius = CornerRadius(style.cornerRadiusPx, style.cornerRadiusPx)
        )

    }
}

private fun drawClickedRect(
    scope: DrawScope, clickedBar: MutableState<Offset?>, highlightColor: Color, basicChartDrawer: BasicChartDrawer
) {
    // click action
    clickedBar.value?.let{

        scope.drawRect(
            color = highlightColor,
            topLeft = Offset(
                x = it.x,
                y = it.y
            ),
            size = Size(
                width = basicChartDrawer.xItemSpacing,
                height = basicChartDrawer.paddingTopPx + basicChartDrawer.gridHeight - it.y
            )
        )

    }
}


