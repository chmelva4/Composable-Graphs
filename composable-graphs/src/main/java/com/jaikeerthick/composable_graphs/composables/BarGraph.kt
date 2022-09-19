package com.jaikeerthick.composable_graphs.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.color.Gradient1
import com.jaikeerthick.composable_graphs.color.Gradient2
import com.jaikeerthick.composable_graphs.decorations.CanvasDrawable
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.style.BarGraphStyle

/**
 * A minimal and stunning Graph
 */
@Composable
fun BarGraph(
    dataList: List<Number>,
    xAxisLabels: XAxisLabels? = null,
    header: @Composable() () -> Unit = {},
    style: BarGraphStyle = BarGraphStyle(),
    decorations: List<CanvasDrawable> = emptyList<CanvasDrawable>(),
    onBarClicked: (value: Any) -> Unit = {},
) {

    val barOffsetList = remember { mutableListOf<Pair<Offset, Offset>>() }
    val clickedBar: MutableState<Offset?> = remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .background(
//                color = style.colors.backgroundColor
            Color.LightGray
            )
            .fillMaxWidth()
            .padding(style.paddingValues)
            .wrapContentHeight()
    ) {

        if (style.visibility.isHeaderVisible){
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
                            onBarClicked(dataList[index])

                            clickedBar.value = it.first
                        }
                    }

                },
        ) {

            val safeSize = if (dataList.isNotEmpty()) dataList.size.toFloat() else 1f

            val yAxisLabels = YAxisLabels.fromGraphInputs(dataList)
            val presentXAxisLabels = xAxisLabels?: XAxisLabels.createDefault(dataList)
            val basicDrawer = BasicChartDrawer(
                this,
                size,
                20.dp.toPx(),
                20.dp.toPx(),
                0.dp.toPx(),
                20.dp.toPx(),
                presentXAxisLabels,
                yAxisLabels,
                dataList,
                customXDataOffset = 0f
            )

            drawPaddings(basicDrawer)



            presentXAxisLabels.drawToCanvas(basicDrawer)

            yAxisLabels.drawToCanvas(basicDrawer)

            decorations.forEach { it.drawToCanvas(basicDrawer) }

            constructGraph(
                this,
                dataList,
                barOffsetList,
                basicDrawer,
                style.colors.fillGradient
            )

            drawClickedRect(this, clickedBar, style.colors.clickHighlightColor, basicDrawer)
        }

    }


}

private fun constructGraph(
    scope: DrawScope,
    dataList: List<Number>,
    barOffsetList: MutableList<Pair<Offset, Offset>>,
    basicChartDrawer: BasicChartDrawer,
    barBrush: Brush?
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

        scope.drawRect(
            brush = barBrush ?: Brush.verticalGradient(
                listOf(Gradient1, Gradient2)
            ),
            topLeft = Offset(
                x = x1,
                y = y1
            ),
            size = Size(
                width = basicChartDrawer.xItemSpacing,
                height = basicChartDrawer.paddingTopPx +  basicChartDrawer.gridHeight - y1
            )
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


