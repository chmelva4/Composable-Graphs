package com.jaikeerthick.composablegraphs.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.BarChart
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartStyle
import com.jaikeerthick.composable_graphs.charts.barChart.BarWidth
import com.jaikeerthick.composable_graphs.charts.common.GraphData
import com.jaikeerthick.composable_graphs.charts.common.YScale
import com.jaikeerthick.composable_graphs.charts.drawPaddings
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChart
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartStyle
import com.jaikeerthick.composable_graphs.decorations.BackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.HorizontalLine
import com.jaikeerthick.composable_graphs.decorations.HorizontalLineStyle
import com.jaikeerthick.composable_graphs.decorations.Label
import com.jaikeerthick.composable_graphs.decorations.LabelXPosition
import com.jaikeerthick.composable_graphs.decorations.LabelYPosition
import com.jaikeerthick.composable_graphs.decorations.PaddingPositionType
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.YAxisLabels

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
        val barColor = Color(95, 64, 133)
        val yellow = Color(243, 175, 86)
        val lightPurple = Color(214, 203, 227)
        val bg = Color(242, 239, 244)
        val labelTextColor = Color(88, 61, 120)
        val barWidthPx = LocalDensity.current.run { 10.dp.toPx() }
        val lineWidthPx = LocalDensity.current.run { 2.dp.toPx() }

        val yAxisLabels = YAxisLabels(listOf("6.5", "7", "7.5", "8", "8.5"), textColor.toArgb())

        LineChart(
            xAxisLabels = XAxisLabels(listOf("7 JN", "8 JN", "9 JN", "10 JN").map { GraphData.String(it) }, color = textColor.toArgb()),
            yAxisData = listOf(6.5f, 7.75f, 7.8f, 8.3f),
            yAxisLabels = yAxisLabels,
            yScale = YScale.MinMaxAsNearestValue(.5f, 6.3f, 8.2f),
            style = LineChartStyle(
                canvasPaddingValues = PaddingValues(horizontal = 30.dp, vertical = 5.dp),
                paddingValues = PaddingValues(12.dp),
//                isYAxisLabelVisible = false,
                isHeaderVisible = true,
                xAxisTextColor = labelTextColor.toArgb(),
//                drawCanvasPadding = true,
            ),
            header = { BasicChartHeader(
                title = "Sleep", rightCornerText = "2 hours ago", boldText = "8.3", theRestOfText = "hours"
            )},
            decorations = listOf(
                BackgroundHighlight(8f, 8.5f, bg),
                HorizontalLine(8.0f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                Label(
                    "8",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(8f),
                    lightPurple,
                    textColor = textColor.toArgb(),
                    textSize = 8.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 25.dp)
                )
            ),
            dataPointStyles = mapOf(
                0 to LineChartDataPointStyle(yellow),
                1 to LineChartDataPointStyle(yellow),
                2 to LineChartDataPointStyle(Color.Black),
            )

        )



    }
}

@Preview("Telus Sleep", widthDp = 300, heightDp = 300, showBackground = true, backgroundColor = 0xe5e5e5)
@Composable()
fun TelusSleepPreview() {

    MaterialTheme {
        TelusSleepChart()
    }

}

