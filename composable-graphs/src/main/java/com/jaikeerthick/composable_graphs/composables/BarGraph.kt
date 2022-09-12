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

    val offsetList = remember { mutableListOf<Offset>() }

    val barOffsetList = remember { mutableListOf<Pair<Offset, Offset>>() }

    //val isBarClicked = remember { mutableStateOf(false) }
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

//            this.drawIntoCanvas { it.nativeCanvas. }


            val gridHeight = (size.height) - paddingBottom.toPx()
            val gridWidth = size.width - paddingRight.toPx()

            val maxPointsSize = dataList.size + 1

            // maximum of the data list
            val absMaxY = GraphHelper.getAbsoluteMax(dataList)
            val absMinY = 0
            println("max - $absMaxY, Min - $absMinY")

            val verticalStep = absMaxY.toInt() / dataList.size.toFloat()

            val yAxisLabels = YAxisLabels.fromGraphInputs(dataList)

            println("max point size - $maxPointsSize")


            val xItemSpacing = gridWidth / (maxPointsSize - 1)
            val yItemSpacing = gridHeight / (yAxisLabels.labels.size - 1)


            /**
             * Drawing Grid lines behind the graph on x and y axis
             */
            if (style.visibility.isGridVisible) {

                val horizontalGridLines = HorizontalGridLines(heightDp = 5)
                val verticalGridLines = VerticalGridLines(widthDp = 5)
                // lines inclined towards x axis
                drawVerticalGridLines(verticalGridLines, maxPointsSize, xItemSpacing, gridHeight)

                // lines inclined towards y axis
               drawHorizontalGridLines(horizontalGridLines, maxPointsSize, yItemSpacing, gridHeight, gridWidth)
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

            backgroundHighlights?.forEach { backgroundHighlight -> this.drawBackgroundHighlight(
                backgroundHighlight, verticalStep, yItemSpacing, gridWidth, gridHeight
            ) }


            /**
             * Calculating Offsets, add those into offsetList
             * Draw Rectangle using calculated values
             */
            offsetList.clear()

            //println("max points size - $maxPointsSize")
            for (i in 0 until maxPointsSize - 1) {

                val x1 = xItemSpacing * i
                //val x2 = xItemSpacing * (i + 1)
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

                drawRect(
                    brush = style.colors.fillGradient ?: Brush.verticalGradient(
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

            // click action
            clickedBar.value?.let{

                drawRect(
                    color = style.colors.clickHighlightColor,
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

    }


}
