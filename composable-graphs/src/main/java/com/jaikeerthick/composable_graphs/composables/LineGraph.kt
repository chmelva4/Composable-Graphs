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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.decorations.CanvasDrawable
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun LineGraph(
    xAxisData: XAxisLabels? = null,
    yAxisData: List<Number>,
    header: @Composable() () -> Unit = {},
    style: LineGraphStyle = LineGraphStyle(),
    decorations: List<CanvasDrawable> = emptyList<CanvasDrawable>(),
    onPointClicked: (pair: Pair<Any,Any>) -> Unit = {},
) {

    val paddingRight: Dp = if (style.visibility.isYAxisLabelVisible) 20.dp else 0.dp
    val paddingBottom: Dp = if (style.visibility.isXAxisLabelVisible) 20.dp else 0.dp

    val offsetList = remember{ mutableListOf<Offset>() }
    val isPointClicked = remember { mutableStateOf(false) }
    val clickedPoint: MutableState<Offset?> = remember { mutableStateOf(null) }
    val presentXAxisLabels: XAxisLabels = xAxisData ?: XAxisLabels.createDefault(yAxisData)



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

            // XAxis labels handled at the top as they are needed for clicks
            val yAxisLabels = YAxisLabels.fromGraphInputs(yAxisData)
            val basicDrawer = BasicChartDrawer(
                this,
                size.width - paddingRight.toPx(),
                size.height - paddingBottom.toPx(),
                yAxisLabels,
                yAxisData,
                presentXAxisLabels.labels.size,
                0f
            )

            presentXAxisLabels.drawToCanvas(basicDrawer)
            yAxisLabels.drawToCanvas(basicDrawer)

            decorations.forEach { it.drawToCanvas(basicDrawer) }

            constructOffsetListAndDrawPoints(
            this,
                offsetList,
                yAxisData,
                basicDrawer.xItemSpacing,
                basicDrawer.yItemSpacing,
                basicDrawer.verticalStep,
                basicDrawer.gridHeight,
                style.colors.pointColor,
                5.dp.toPx()
            )

            paintGradientUnderTheGraphLine(
                this, offsetList, yAxisData, basicDrawer.gridHeight, basicDrawer.xItemSpacing, style.colors.fillGradient
            )

            drawLineConnectingPoints(this, offsetList, style.colors.lineColor, 2.dp.toPx())

            drawHighlightedPointAndCrossHair(
                this,
                clickedPoint,
                style.colors.clickHighlightColor,
                12.dp.toPx(),
                style.visibility.isCrossHairVisible,
                style.colors.crossHairColor,
                2.dp.toPx(),
                basicDrawer.gridHeight
            )





        }
    }


}

private fun constructOffsetListAndDrawPoints(
    scope: DrawScope,
    offsetList: MutableList<Offset>,
    yAxisData: List<Number>,
    xItemSpacing: Float,
    yItemSpacing: Float,
    verticalStep: Float,
    gridHeight: Float,
    pointColor: Color,
    pointRadiusPx: Float
) {
    offsetList.clear() // clearing list to avoid data duplication during recomposition

    for (i in yAxisData.indices) {

        val x1 = xItemSpacing * i
        val y1 = gridHeight - (yItemSpacing * (yAxisData[i].toFloat() / verticalStep.toFloat()))

        offsetList.add(
            Offset(
                x = x1,
                y = y1
            )
        )

        scope.drawCircle(
            color = pointColor,
            radius = pointRadiusPx,
            center = Offset(x1, y1)
        )
    }
}

private fun paintGradientUnderTheGraphLine(
    scope: DrawScope,
    offsetList: MutableList<Offset>,
    yAxisData: List<Number>,
    gridHeight: Float,
    xItemSpacing: Float,
    fillBrush: Brush?
) {
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

        offsetList.forEach { offset -> lineTo(offset.x, offset.y) }

        // ending point for gradient
        lineTo(
            x = xItemSpacing * (yAxisData.size - 1),
            y = gridHeight
        )

    }

    scope.drawPath(
        path = path,
        brush = fillBrush ?: Brush.verticalGradient(
            listOf(Color.Transparent, Color.Transparent)
        )
    )
}

private fun drawLineConnectingPoints(scope: DrawScope, offsetList: List<Offset>, lineColor: Color, lineWidth: Float) {
    /**
     * drawing line connecting all circles/points
     */
    scope.drawPoints(
        points = offsetList,
        color = lineColor,
        // Polygon mode draws the line that connects the points
        pointMode = PointMode.Polygon,
        strokeWidth = lineWidth,
    )
}

private fun drawHighlightedPointAndCrossHair(
    scope: DrawScope,
    clickedPoint: MutableState<Offset?>,
    clickHighlightColor: Color,
    clickHighlightRadius: Float,
    drawCrossHair: Boolean,
    crossHairColor: Color,
    crossHairLineWidth: Float,
    gridHeight: Float
) {

    /**
     * highlighting clicks when user clicked on the canvas
     */
    clickedPoint.value?.let {
        scope.drawCircle(
            color = clickHighlightColor,
            center = it,
            radius = clickHighlightRadius
        )
        if (drawCrossHair) {
            scope.drawLine(
                color = crossHairColor,
                start = Offset(it.x, 0f),
                end = Offset(it.x, gridHeight),
                strokeWidth = crossHairLineWidth,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(15f, 15f)
                )
            )
        }
    }
}


