package com.jaikeerthick.composablegraphs.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.BarChart
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartStyle
import com.jaikeerthick.composable_graphs.charts.barChart.BarWidth
import com.jaikeerthick.composable_graphs.charts.common.GraphData
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChart
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartStyle
import com.jaikeerthick.composable_graphs.decorations.BackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.HorizontalLine
import com.jaikeerthick.composable_graphs.decorations.HorizontalLineStyle
import com.jaikeerthick.composable_graphs.decorations.Label
import com.jaikeerthick.composable_graphs.decorations.LabelXPosition
import com.jaikeerthick.composable_graphs.decorations.LabelYPosition
import com.jaikeerthick.composable_graphs.decorations.PaddingPositionType
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels

@Composable
fun TelusSleepChart() {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .wrapContentHeight()
    ) {

        val textColor = Color(105, 110, 113)

        LineChart(
            xAxisLabels = XAxisLabels(listOf("7 JN", "8 JN", "9 JN", "10 JN").map { GraphData.String(it) }, color = textColor.toArgb()),
            yAxisData = listOf(6.2f, 7.75f, 7.8f, 8.3f),
            style = LineChartStyle(
                canvasPaddingValues = PaddingValues(20.dp)
            )

        )



    }
}

@Preview("Telus Sleep", widthDp = 300, heightDp = 300, showBackground = true, backgroundColor = 0xff0000)
@Composable()
fun TelusSleepPreview() {

    MaterialTheme {
        TelusSleepChart()
    }

}

@Composable
fun TelusApetiteChart() {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .wrapContentHeight()
    ) {

        val textColor = Color(105, 110, 113)
        val barColor = Color(95, 64, 133)
        val yellow = Color(243, 175, 86)
        val lightPurple = Color(214, 203, 227)
        val bg = Color(242, 239, 244)
        val labelTextColor = Color(88, 61, 120)
        val barWidthPx = LocalDensity.current.run { 10.dp.toPx() }
        val lineWidthPx = LocalDensity.current.run { 2.dp.toPx() }

        BarChart(
            xAxisLabels = XAxisLabels(listOf("7 JN", "8 JN", "9 JN", "10 JN").map { GraphData.String(it) }, color = textColor.toArgb()),
            dataList = listOf(1f, 3f, 4f, 2f),
            style = BarChartStyle(
//                height = 200.dp,
                canvasPaddingValues = PaddingValues(20.dp),
                defaultDataPointStyle = BarChartDataPointStyle(
                    Brush.horizontalGradient(listOf(barColor, barColor)),
                    BarWidth.PxWidth(barWidthPx)
                ),
                yAxisTextColor = textColor.toArgb()
            ),
            dataPointsStyles = mapOf(
                0 to BarChartDataPointStyle(Brush.horizontalGradient(listOf(yellow, yellow)), BarWidth.PxWidth(barWidthPx)),
                3 to BarChartDataPointStyle(Brush.horizontalGradient(listOf(lightPurple, lightPurple)), BarWidth.PxWidth(barWidthPx))
            ),
            decorations = listOf(
                BackgroundHighlight(3f, 4f, bg),
                HorizontalLine(3f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                Label(
                    "3",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(3f),
                    lightPurple,
                    textColor = labelTextColor.toArgb(),
                    textSize = 8.sp
                )
            ),


        )



    }
}

@Preview("Telus Apetite", widthDp = 300, heightDp = 300, showBackground = true, backgroundColor = 0xff0000)
@Composable()
fun TelusApetitePreview() {

    MaterialTheme {
        TelusApetiteChart()
    }

}