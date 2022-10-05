package com.jaikeerthick.composable_graphs.charts.barChart

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import com.jaikeerthick.composable_graphs.charts.common.FocusDirection
import com.jaikeerthick.composable_graphs.charts.common.SwipeDirection
import com.jaikeerthick.composable_graphs.charts.common.SwipeGestureRecognizer
import com.jaikeerthick.composable_graphs.charts.common.rememberActiveDataPointStateHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.log

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

    val coroutineScope = rememberCoroutineScope()
    val activeDataPointStateHolder = rememberActiveDataPointStateHolder(data.size, coroutineScope)

    val activeIndex by activeDataPointStateHolder.activeItemFlow.collectAsState()

    val activeBar: Pair<Offset, Offset>? = remember(activeIndex, barOffsetList) {
        if (activeIndex == null) return@remember null
        return@remember barOffsetList.getOrNull(activeIndex!!)
    }

    DisposableEffect(activeDataPointStateHolder) {

        val onFocusOutJob = coroutineScope.launch {
            Log.d("BarChart", "SH focus out evt sub")
            activeDataPointStateHolder.onFocusOutEvent.collect {
                Log.d("BarChart", "FO: $it")
            }
        }

        onDispose {
            Log.d("BarChart", "SH focus out evt dispose")
            onFocusOutJob.cancel()
        }
    }




    Column(
        modifier = Modifier
            .background(
                color = style.backgroundColor //            Color.LightGray
            )
            .fillMaxWidth()
            .padding(style.paddingValues)
            .wrapContentHeight()
            .pointerInput(true) {
                activeDataPointStateHolder.gestureRecognizer.pointerInputScope = this
            }
    ) {

        if (style.isHeaderVisible){
            header()
        }

        Row() {
            Button(onClick = {
                activeDataPointStateHolder.focusIn(FocusDirection.FRONT)
            }) {
                Text("Front")
            }
            Button(onClick = {
                activeDataPointStateHolder.focusIn(FocusDirection.BACK)
            }) {
                Text("Back")
            }
        }


        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(style.height)
                .padding(horizontal = 1.dp)
                .padding(top = 12.dp) // to prevent overlap with header
                .semantics {
                    contentDescription = "Bar chart of data" //                    customProp = "aaa"

                }
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
                style.backgroundColor,
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
                style.defaultDataPointStyle,
            )

            drawClickedRect(this, clickedBar, style.clickHighlightColor, basicDrawer)
            drawActiveRect(this, activeBar, basicDrawer)
        }

    }


}

private fun constructGraph(
    scope: DrawScope,
    dataList: List<Number>,
    barOffsetList: MutableList<Pair<Offset, Offset>>,
    basicChartDrawer: BasicChartDrawer,
    dataPointsStyles: Map<Int, BarChartDataPointStyle>,
    defaultDataPointStyle: BarChartDataPointStyle,
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

private fun drawActiveRect(
    scope: DrawScope, activeBar: Pair<Offset, Offset>?, basicChartDrawer: BasicChartDrawer
) {
    // click action
    activeBar?.let{

        scope.drawRect(
            color = Color.Green.copy(0.75f),
            topLeft = Offset(
                x = it.first.x - 5,
                y = it.first.y - 5
            ),
            size = Size(
                width = basicChartDrawer.xItemSpacing + 10,
                height = basicChartDrawer.paddingTopPx + basicChartDrawer.gridHeight - it.first.y + 10
            ),
            style = Stroke(5f)

        )

    }
}


