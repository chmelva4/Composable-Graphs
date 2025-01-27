package com.jaikeerthick.composable_graphs.charts.lineChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
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
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun LineChart(
    data: List<Number>,
    style: LineChartStyle = LineChartStyle(),
    xAxisLabels: XAxisLabels? = null,
    yAxisLabels: YAxisLabels = YAxisLabels.fromGraphInputs(data, Color.Black.toArgb(), YAxisLabelsPosition.LEFT),
    yScale: YScale = YScale.ZeroToMaxScale(),
    header: @Composable() () -> Unit = {},
    decorations: List<CanvasDrawable> = emptyList<CanvasDrawable>(),
    dataPointStyles: Map<Int, LineChartDataPointStyle> = emptyMap(),
    onPointClicked: (pair: Pair<Any,Any>) -> Unit = {},
) {

    val offsetList = remember{ mutableListOf<Offset>() }
    val isPointClicked = remember { mutableStateOf(false) }
    val clickedPoint: MutableState<Offset?> = remember { mutableStateOf(null) }
    val presentXAxisLabels: XAxisLabels = xAxisLabels ?: XAxisLabels.createDefault(data, XAxisLabelsPosition.BOTTOM, style.xAxisTextColor)

    val currentDensity = LocalDensity.current

    Column(
        modifier = Modifier
            .background(
                color = style.backgroundColor
            )
            .fillMaxWidth()
            .padding(style.paddingValues),
//            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ){

        if (style.isHeaderVisible){
            header()
        }


        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(style.height)
                .padding(horizontal = 1.dp)
                .padding(top = 12.dp) //                .graphicsLayer(alpha = 0.99f)
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
                            onPointClicked(Pair(presentXAxisLabels.labels[index].text, data[index]))
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
            yScale.setupValuesFromData(data)
            val basicDrawer = BasicChartDrawer(
                this,
                size,
                style.canvasPaddingValues.calculateLeftPadding(LayoutDirection.Ltr).toPx(),
                style.canvasPaddingValues.calculateLeftPadding(LayoutDirection.Ltr).toPx(),
                style.canvasPaddingValues.calculateTopPadding().toPx(),
                style.canvasPaddingValues.calculateBottomPadding().toPx(),
                presentXAxisLabels,
                yAxisLabels,
                data,
                yScale,
                style.backgroundColor
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

            paintGradientUnderTheGraphLine(
                this, offsetList, data, basicDrawer, style.fillGradient
            )

            drawLineConnectingPoints(this, offsetList, style.lineColor, style.lineWidth.toPx())


            drawPoints(basicDrawer, offsetList, dataPointStyles, style.defaultDataPointStyle, currentDensity)

            drawHighlightedPointAndCrossHair(
                this,
                clickedPoint,
                style.clickHighlightColor,
                style.clickHighlightRadius.toPx(),
                style.isCrossHairVisible,
                style.crossHairColor,
                style.crossHairLineWidth.toPx(),
                basicDrawer.gridHeight
            )





        }
    }


}

private fun constructOffsetList(
    offsetList: MutableList<Offset>,
    data: List<Number>,
    basicChartDrawer: BasicChartDrawer,
) {
    offsetList.clear() // clearing list to avoid data duplication during recomposition

    data.forEachIndexed { idx, dataPoint ->
        val x1 = chartXToCanvasX(idx.toFloat(), basicChartDrawer)
        val y1 = chartYtoCanvasY(dataPoint.toFloat(), basicChartDrawer)

        offsetList.add(
            Offset(
                x = x1,
                y = y1
            )
        )
    }
}

private fun drawPoints(
    basicChartDrawer: BasicChartDrawer,
    offsetList: MutableList<Offset>,
    dataPointStyles: Map<Int, LineChartDataPointStyle>,
    defaultStyle: LineChartDataPointStyle,
    currentDensity: Density,
) {
    offsetList.forEachIndexed { idx, offset ->
        val style = dataPointStyles.getOrElse(idx) {defaultStyle}

        style.pointStyle.drawToCanvas(offset.x, offset.y, basicChartDrawer)
    }
}

private fun paintGradientUnderTheGraphLine(
    scope: DrawScope,
    offsetList: MutableList<Offset>,
    yAxisData: List<Number>,
    basicChartDrawer: BasicChartDrawer,
    fillBrush: Brush
) {
    /**
     * Drawing Gradient fill for the plotted points
     * Create Path from the offset list with start and end point to complete the path
     * then draw path using brush
     */
    val path = Path().apply {
        // starting point for gradient
        moveTo(
            x = chartXToCanvasX(0f, basicChartDrawer),
            y = chartYtoCanvasY(0f, basicChartDrawer)
        )

        offsetList.forEach { offset -> lineTo(offset.x, offset.y) }

//         ending point for gradient
        lineTo(
            x = offsetList.last().x,
            y = chartYtoCanvasY(0f, basicChartDrawer)
        )

    }

    scope.drawPath(
        path = path,
//        color = Color.Red
        brush = fillBrush
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
//        blendMode = BlendMode.DstOver
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


