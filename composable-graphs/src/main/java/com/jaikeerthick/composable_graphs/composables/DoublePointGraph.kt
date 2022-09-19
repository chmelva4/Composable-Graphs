package com.jaikeerthick.composable_graphs.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.decorations.CanvasDrawable
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels
import com.jaikeerthick.composable_graphs.style.BarGraphStyle

/**
 * A minimal and stunning Graph
 */
@Composable
fun DoublePointGraph(
    dataList: List<Pair<Number, Number>>,
    xAxisLabels: XAxisLabels? = null,
    header: @Composable() () -> Unit = {},
    style: BarGraphStyle = BarGraphStyle(),
    decorations: List<CanvasDrawable> = emptyList<CanvasDrawable>(),
) {

    val offsetList = remember { mutableListOf<Pair<Offset, Offset>>()}

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
                ,
        ) {

            val maxList = dataList.map { it.second }
            val yAxisLabels = YAxisLabels.fromGraphInputs(maxList)
            val presentXAxisLabels = xAxisLabels ?: XAxisLabels.createDefault(maxList)
            val basicDrawer = BasicChartDrawer(
                this,
                size,
                20.dp.toPx(),
                20.dp.toPx(),
                0.dp.toPx(),
                20.dp.toPx(),
                presentXAxisLabels,
                yAxisLabels,
                maxList,
            )


            presentXAxisLabels.drawToCanvas(basicDrawer)

            yAxisLabels.drawToCanvas(basicDrawer)

            decorations.forEach { it.drawToCanvas(basicDrawer) }

            constructOffsetList(
                offsetList,
                dataList,
                basicDrawer.xItemSpacing,
                basicDrawer.yItemSpacing,
                basicDrawer.verticalStep,
                basicDrawer.gridHeight,
            )

            drawLineConnectingPoints(this, offsetList, Color.Magenta, 2.dp.toPx())
            drawPoints(this, offsetList,  Color.Red, 3.dp.toPx())
        }

    }

}

private fun constructOffsetList(
    offsetList: MutableList<Pair<Offset, Offset>>,
    data: List<Pair<Number, Number>>,
    xItemSpacing: Float,
    yItemSpacing: Float,
    verticalStep: Float,
    gridHeight: Float,
) {
    offsetList.clear() // clearing list to avoid data duplication during recomposition

    for (i in data.indices) {

        val x1 = xItemSpacing * i
        val y1 = gridHeight - (yItemSpacing * (data[i].first.toFloat() / verticalStep.toFloat()))
        val y2 = gridHeight - (yItemSpacing * (data[i].second.toFloat() / verticalStep.toFloat()))

        offsetList.add(
            Pair(Offset(x1, y1), Offset(x1, y2))
        )
    }
}

private fun drawPoints(
    scope: DrawScope,
    offsetList: List<Pair<Offset, Offset>>,
    pointColor: Color,
    pointRadiusPx: Float
) {

    offsetList.forEach {
        scope.drawCircle(color = pointColor, radius = pointRadiusPx, center = Offset(it.first.x, it.first.y))
        scope.drawCircle(color = pointColor, radius = pointRadiusPx, center = Offset(it.second.x, it.second.y)) }
}

private fun drawLineConnectingPoints(
    scope: DrawScope,
    offsetList: List<Pair<Offset, Offset>>,
    lineColor: Color,
    lineWidth: Float
) {

    offsetList.forEach {
        scope.drawLine(color = lineColor, start = it.first, end = it.second, strokeWidth = lineWidth)
    }

}




