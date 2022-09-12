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
import com.jaikeerthick.composable_graphs.color.LightGray
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.decorations.BackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.HorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.VerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.decorations.drawBackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.drawHorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.drawVerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.drawXAxisLabels
import com.jaikeerthick.composable_graphs.decorations.drawYAxisLabels
import com.jaikeerthick.composable_graphs.helper.GraphHelper
import com.jaikeerthick.composable_graphs.style.BarGraphStyle

/**
 * A minimal and stunning Graph
 */
@Composable
fun BarGraph(
    dataList: List<Number>,
    xAxisData: XAxisLabels? = null,
    header: @Composable() () -> Unit = {},
    style: BarGraphStyle = BarGraphStyle(),
    onBarClicked: (value: Any) -> Unit = {},
    backgroundHighlights: List<BackgroundHighlight>? = null
) {

    val paddingRight: Dp = if (style.visibility.isYAxisLabelVisible) 20.dp else 0.dp
    val paddingBottom: Dp = if (style.visibility.isXAxisLabelVisible) 20.dp else 0.dp


    val barOffsetList = remember { mutableListOf<Pair<Offset, Offset>>() }
    val clickedBar: MutableState<Offset?> = remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .background(
                color = style.colors.backgroundColor
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

            val gridHeight = size.height - paddingBottom.toPx()
            val gridWidth = size.width - paddingRight.toPx()

            // maximum of the data list
            val absMaxY = GraphHelper.getAbsoluteMax(dataList)

            val verticalStep = absMaxY.toInt() / dataList.size.toFloat()

            val yAxisLabels = YAxisLabels.fromGraphInputs(dataList)

            val xItemSpacing = gridWidth / (dataList.size)
            val yItemSpacing = gridHeight / (yAxisLabels.labels.size - 1)


            /**
             * Drawing Grid lines behind the graph on x and y axis
             */
            if (style.visibility.isGridVisible) {
                val horizontalGridLines = HorizontalGridLines(heightDp = 5)
                val verticalGridLines = VerticalGridLines(widthDp = 5)
                // lines inclined towards x axis
                drawVerticalGridLines(verticalGridLines, dataList.size + 1, xItemSpacing, gridHeight)
                // lines inclined towards y axis
               drawHorizontalGridLines(horizontalGridLines, dataList.size + 1, yItemSpacing, gridHeight, gridWidth)
            }


            /**
             * Drawing text labels over the y- axis
             */
            if (style.visibility.isYAxisLabelVisible) {
                drawYAxisLabels(yAxisLabels, yItemSpacing, gridHeight, style.colors.yAxisTextColor)
            }

            /**
             * Drawing text labels over the x- axis
             */
            if (style.visibility.isXAxisLabelVisible) {
                val drawXAxisData = xAxisData ?: XAxisLabels(dataList.mapIndexed { idx, _ -> GraphData.Number(idx + 1) })
                drawXAxisLabels(drawXAxisData, xItemSpacing, xItemSpacing / 2, style.colors.xAxisTextColor)
            }

            constructGraph(
                this,
                dataList,
                barOffsetList,
                gridHeight,
                xItemSpacing,
                yItemSpacing,
                verticalStep,
                style.colors.fillGradient
            )

            drawClickedRect(this, clickedBar, style.colors.clickHighlightColor, xItemSpacing, gridHeight)
        }

    }


}

private fun constructGraph(
    scope: DrawScope,
    dataList: List<Number>,
    barOffsetList: MutableList<Pair<Offset, Offset>>,
    gridHeight: Float,
    xItemSpacing: Float,
    yItemSpacing: Float,
    verticalStep: Float,
    barBrush: Brush?
) {
    for (i in dataList.indices) {

        val x1 = xItemSpacing * i
        val y1 =
            gridHeight - (yItemSpacing * (dataList[i].toFloat() / verticalStep))

        barOffsetList.add(
            Pair(
                first = Offset(
                    x = x1,
                    y = y1
                ),
                second = Offset(
                    x = (xItemSpacing * (i + 1)),
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
                width = xItemSpacing,
                height = gridHeight - y1
            )
        )

    }
}

private fun drawClickedRect(
    scope: DrawScope, clickedBar: MutableState<Offset?>, highlightColor: Color, xItemSpacing: Float, gridHeight: Float
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
                width = xItemSpacing,
                height = gridHeight - it.y
            )
        )

    }
}


