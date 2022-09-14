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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.decorations.HorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.VerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.decorations.drawHorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.drawVerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.drawXAxisLabels
import com.jaikeerthick.composable_graphs.decorations.drawYAxisLabels
import com.jaikeerthick.composable_graphs.helper.GraphHelper
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun LineGraph(
    xAxisData: XAxisLabels? = null,
    yAxisData: List<Number>,
    header: @Composable() () -> Unit = {},
    style: LineGraphStyle = LineGraphStyle(),
    onPointClicked: (pair: Pair<Any,Any>) -> Unit = {},
) {

    val paddingRight: Dp = if (style.visibility.isYAxisLabelVisible) 20.dp else 0.dp
    val paddingBottom: Dp = if (style.visibility.isXAxisLabelVisible) 20.dp else 0.dp

    val offsetList = remember{ mutableListOf<Offset>() }
    val isPointClicked = remember { mutableStateOf(false) }
    val clickedPoint: MutableState<Offset?> = remember { mutableStateOf(null) }


    Column(
        modifier = Modifier
            .background(
                color = style.colors.backgroundColor
            )
            .fillMaxWidth()
            .padding(style.paddingValues)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ){

        if (style.visibility.isHeaderVisible){
            header()
        }

        val presentXAxisLabels: XAxisLabels = xAxisData ?: XAxisLabels(yAxisData.mapIndexed {idx, _ -> GraphData.Number(idx + 1)})

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(style.height)
                .padding(horizontal = 10.dp)
                .pointerInput(true) {

                    detectTapGestures { p1: Offset ->

                        val shortest = offsetList.find { p2: Offset ->

                            /** Pythagorean Theorem
                             * Using Pythagorean theorem to calculate distance between two points :
                             * p1 =  p1(x,y) which is the touch point
                             * p2 =  p2(x,y)) which is the point plotted on graph
                             * Formula: c = sqrt(a² + b²), where a = (p1.x - p2.x) & b = (p1.y - p2.y),
                             * c is the distance between p1 & p2
                            Pythagorean Theorem */

                            val distance = sqrt(
                                (p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2)
                            )
                            val pointRadius = 15.dp.toPx()

                            distance <= pointRadius
                        }

                        shortest?.let {

                            clickedPoint.value = it
                            isPointClicked.value = true

                            //
                            val index = offsetList.indexOf(it)
                            onPointClicked(Pair(presentXAxisLabels.labels[index].text, yAxisData[index]))
                        }

                    }

                },
        ) {


            //println("Entered scope")
            /**
             * xItemSpacing, yItemSpacing => space between each item that lies on the x and y axis
             * (size.width - 16.dp.toPx())
             *               ~~~~~~~~~~~~~ => padding saved for the end of the axis
             */

            val gridHeight = (size.height) - paddingBottom.toPx()
            val gridWidth = size.width - paddingRight.toPx()

            // the maximum points for x and y axis to plot (maintain uniformity)
            val maxPointsSize: Int = minOf(presentXAxisLabels.labels.size, yAxisData.size)

            // maximum of the y data list
            val absMaxY = GraphHelper.getAbsoluteMax(yAxisData)

            val verticalStep = absMaxY.toInt() / maxPointsSize.toFloat()

            // if there is less x labels than points, take less points
            val uniformInputs = yAxisData.take(maxPointsSize)

           val yAxisLabels = YAxisLabels.fromGraphInputs(uniformInputs)


            val xItemSpacing = gridWidth / (maxPointsSize - 1)
            val yItemSpacing = gridHeight / (yAxisLabels.labels.size - 1)



            /**
             * Drawing Grid lines inclined towards x axis
             */
            if (style.visibility.isGridVisible) {
                val horizontalGridLines = HorizontalGridLines(heightPx = 1)
                val verticalGridLines = VerticalGridLines(widthPx = 1)
                // lines inclined towards x axis
                drawVerticalGridLines(verticalGridLines, maxPointsSize, xItemSpacing, gridHeight)
                // lines inclined towards y axis
                drawHorizontalGridLines(horizontalGridLines, maxPointsSize, yItemSpacing, gridHeight, gridWidth)
            }

            /**
             * Drawing text labels over the x- axis
             */
            if (style.visibility.isXAxisLabelVisible) {
                drawXAxisLabels(labels = presentXAxisLabels, xItemSpacing, 0f, style.colors.xAxisTextColor)
            }

            /**
             * Drawing text labels over the y- axis
             */
            if (style.visibility.isYAxisLabelVisible) {
               drawYAxisLabels(yAxisLabels, yItemSpacing, gridHeight, style.colors.yAxisTextColor)
            }


            // plotting points
            /**
             * Plotting points on the Graph
             */

            offsetList.clear() // clearing list to avoid data duplication during recomposition

            for (i in 0 until maxPointsSize) {

                val x1 = xItemSpacing * i
                val y1 = gridHeight - (yItemSpacing * (yAxisData[i].toFloat() / verticalStep.toFloat()))

                offsetList.add(
                    Offset(
                        x = x1,
                        y = y1
                    )
                )

                drawCircle(
                    color = style.colors.pointColor,
                    radius = 5.dp.toPx(),
                    center = Offset(x1, y1)
                )
            }


            /**
             * Drawing Gradient fill for the plotted points
             * Create Path from the offset list with start and end point to complete the path
             * then draw path using brush
             */
            val path = Path().apply {
                // starting point for gradient
                moveTo(
                    x = 0f,
                    y = gridHeight
                )

                for (i in 0 until maxPointsSize) {
                    lineTo(offsetList[i].x, offsetList[i].y)
                }

                // ending point for gradient
                lineTo(
                    x = xItemSpacing * (yAxisData.size - 1),
                    y = gridHeight
                )

            }

            drawPath(
                path = path,
                brush = style.colors.fillGradient ?: Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Transparent)
                )
            )


            /**
             * drawing line connecting all circles/points
             */
            drawPoints(
                points = offsetList.subList(
                    fromIndex = 0,
                    toIndex = maxPointsSize
                ),
                color = style.colors.lineColor,
                pointMode = PointMode.Polygon,
                strokeWidth = 2.dp.toPx(),
            )

            /**
             * highlighting clicks when user clicked on the canvas
             */
            clickedPoint.value?.let {
                drawCircle(
                    color = style.colors.clickHighlightColor,
                    center = it,
                    radius = 12.dp.toPx()
                )
                if (style.visibility.isCrossHairVisible) {
                    drawLine(
                        color = style.colors.crossHairColor,
                        start = Offset(it.x, 0f),
                        end = Offset(it.x, gridHeight),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(15f, 15f)
                        )
                    )
                }
            }

        }
    }


}


