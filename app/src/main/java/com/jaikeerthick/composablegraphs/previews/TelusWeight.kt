package com.jaikeerthick.composablegraphs.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.common.GraphData
import com.jaikeerthick.composable_graphs.charts.common.YScale
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChart
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartStyle
import com.jaikeerthick.composable_graphs.decorations.AxisLabelItem
import com.jaikeerthick.composable_graphs.decorations.HorizontalLine
import com.jaikeerthick.composable_graphs.decorations.HorizontalLineStyle
import com.jaikeerthick.composable_graphs.decorations.Label
import com.jaikeerthick.composable_graphs.decorations.LabelXPosition
import com.jaikeerthick.composable_graphs.decorations.LabelYPosition
import com.jaikeerthick.composable_graphs.decorations.PaddingPositionType
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels

@Composable
fun TelusWeightChart() {
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
        val lineColor = Color(118, 85, 160)
        val yellow = Color(243, 175, 86)
        val lightPurple = Color(214, 203, 227)
        val darkPurple = Color(70, 42, 105)
        val bg = Color(242, 239, 244)
        val labelTextColor = Color(88, 61, 120)
        val lineWidthPx = LocalDensity.current.run { 2.dp.toPx() }



        val data = listOf(75.25f, 75.4f, 75.5f, 75.2f)
//        val dataMin = data.
        val yScale = YScale.MinMaxAsNearestValue(10f, data.minOf { it }, data.maxOf { it })
        val yAxisLabels = YAxisLabels(yScale.getLabels(5).map { AxisLabelItem(it) }, textColor.toArgb())

        LineChart(
            xAxisLabels = XAxisLabels(listOf("7 JN", "8 JN", "9 JN", "10 JN").map { GraphData.String(it) }, color = textColor.toArgb()),
            data = data,
            yAxisLabels = yAxisLabels,
            yScale = yScale,
            style = LineChartStyle(
                canvasPaddingValues = PaddingValues(horizontal = 30.dp, vertical = 5.dp),
                paddingValues = PaddingValues(12.dp),
//                isYAxisLabelVisible = false,
                isHeaderVisible = true,
                lineColor = lineColor,
                xAxisTextColor = labelTextColor.toArgb(),
                yAxisTextColor = labelTextColor.toArgb(),
                defaultDataPointStyle = LineChartDataPointStyle(lineColor)
//                drawCanvasPadding = true,
            ),
            header = { BasicChartHeader(
                title = "Weight", rightCornerText = "2 hours ago", boldText = "75.2", theRestOfText = "kg", badgeText = "Personal"
            )},
            decorations = listOf(
                HorizontalLine(72.0f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                HorizontalLine(76.0f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                Label(
                    "76",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(76f),
                    lightPurple,
                    textColor = textColor.toArgb(),
                    textSize = 14.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 25.dp)
                ),
                Label(
                    "74",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(74f),
                    lightPurple,
                    textColor = textColor.toArgb(),
                    textSize = 14.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 25.dp)
                ),
                Label(
                    "72",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(72f),
                    darkPurple,
                    textColor = Color.White.toArgb(),
                    textSize = 14.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 25.dp)
                )
            ),
        )



    }
}

@Preview("Telus weight", widthDp = 300, heightDp = 300, showBackground = true, backgroundColor = 0xe5e5e5)
@Composable()
fun TelusWeight() {

    MaterialTheme {
        TelusWeightChart()
    }

}

