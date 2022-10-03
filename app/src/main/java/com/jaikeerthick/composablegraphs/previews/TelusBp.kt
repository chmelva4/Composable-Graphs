package com.jaikeerthick.composablegraphs.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.charts.common.GraphData
import com.jaikeerthick.composable_graphs.charts.common.PointStyle
import com.jaikeerthick.composable_graphs.charts.common.YScale
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChart
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChartStyle
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChart
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartStyle
import com.jaikeerthick.composable_graphs.decorations.AxisLabelItem
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
fun TelusBPChart() {
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
        val pointRadiusPx = LocalDensity.current.run { 4.dp.toPx() }
        val imgRadius = LocalDensity.current.run { 10.dp.toPx() }
        val defaultPointStyle = PointStyle.OutlinedPoint(pointRadiusPx, lineColor, lineWidthPx)

        val img = rememberVectorPainter(image = Icons.Filled.CheckCircle)
        val imgPointStyle = PointStyle.ImagePointStyle(img, imgRadius, imgRadius, yellow)



        val data = listOf(Pair(60, 115f), Pair(100f, 130f), Pair(0f, 0f), Pair(80f, 125f))
//        val dataMin = data.
        val yScale = YScale.CustomScale(60f, 140f)
        val yAxisLabels = YAxisLabels(yScale.getLabels(5).map { AxisLabelItem(it, it.toInt().toString()) }, textColor.toArgb())

        DoublePointChart(
            xAxisLabels = XAxisLabels(listOf("7 JN", "8 JN", "9 JN", "10 JN").map { GraphData.String(it) }, color = textColor.toArgb()),
            data = data,
            yAxisLabels = yAxisLabels,
            yScale = yScale,
            style = DoublePointChartStyle(
                backgroundColor = Color.White,
                canvasPaddingValues = PaddingValues(top = 10.dp, bottom = 30.dp, start = 30.dp),
                paddingValues = PaddingValues(12.dp),
//                isYAxisLabelVisible = false,
                isHeaderVisible = true,
                xAxisTextColor = labelTextColor.toArgb(),
                yAxisTextColor = labelTextColor.toArgb(),
                defaultDataPointStyle = DoublePointChartDataPointStyle(
                    bottomPointStyle = defaultPointStyle, topPointStyle = defaultPointStyle, lineColor = lineColor
                ),
//                drawCanvasPadding = true,
            ),
            header = { BasicChartHeader(
                title = "Hearth rate", rightCornerText = "2 hours ago", boldText = "125", theRestOfText = "hearth beats per minute",
            )},

            dataPointStyles = mapOf(
                0 to DoublePointChartDataPointStyle(
                    imgPointStyle, imgPointStyle, yellow
                ),
                1 to DoublePointChartDataPointStyle(
                    PointStyle.FilledPoint(pointRadiusPx, Color.Red), defaultPointStyle, lineColor
                )
            ),
            decorations = listOf(
                HorizontalLine(75f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                HorizontalLine(88f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                HorizontalLine(115f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                HorizontalLine(135f, lightPurple, lineWidthPx, HorizontalLineStyle.DASHED),
                BackgroundHighlight(75f, 88f, bg),
                BackgroundHighlight(115f, 135f, bg),
                Label(
                    "75",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(75f),
                    lightPurple,
                    textColor = darkPurple.toArgb(),
                    textSize = 12.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                ),
                Label(
                    "88",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(88f),
                    lightPurple,
                    textColor = darkPurple.toArgb(),
                    textSize = 12.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                ),
                Label(
                    "115",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(115f),
                    lightPurple,
                    textColor = darkPurple.toArgb(),
                    textSize = 12.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                ),
                Label(
                    "135",
                    LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT),
                    LabelYPosition.ChartPosition(135f),
                    lightPurple,
                    textColor = darkPurple.toArgb(),
                    textSize = 12.sp,
                    padding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                )
            ),
        )



    }
}

@Preview("Telus Blood Presure", widthDp = 300, heightDp = 300, showBackground = true, backgroundColor = 0xe5e5e5)
@Composable()
fun TelusBP() {

    MaterialTheme {
        TelusBPChart()
    }

}

