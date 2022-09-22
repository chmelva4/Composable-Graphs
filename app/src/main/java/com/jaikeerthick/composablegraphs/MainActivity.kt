package com.jaikeerthick.composablegraphs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.color.*
import com.jaikeerthick.composable_graphs.charts.BarChart
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartColors
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartStyle
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChart
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChartStyle
import com.jaikeerthick.composable_graphs.charts.common.GraphData
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChartDataPointStyle
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChart
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartColors
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartStyle
import com.jaikeerthick.composable_graphs.decorations.BackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.HorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.HorizontalLine
import com.jaikeerthick.composable_graphs.decorations.HorizontalLineStyle
import com.jaikeerthick.composable_graphs.decorations.Label
import com.jaikeerthick.composable_graphs.decorations.LabelXPosition
import com.jaikeerthick.composable_graphs.decorations.LabelYPosition
import com.jaikeerthick.composable_graphs.decorations.PaddingPositionType
import com.jaikeerthick.composable_graphs.decorations.VerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.decorations.XAxisLabelsPosition
import com.jaikeerthick.composable_graphs.decorations.YAxisLabelsPosition

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(color = Color.White),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(modifier = Modifier.fillMaxWidth()){

                        val clickedValue: MutableState<Pair<Any,Any>?> = remember{ mutableStateOf(null) }

                        val complementColor = LineChartColors(Color(206, 26, 54).copy(alpha = 0.3f))

                        LineChart(
                            xAxisLabels = XAxisLabels(listOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat").map {
                                GraphData.String(it)
                            }),
                            yAxisData = listOf(200, 40, 60, 450, 700, 30, 50),
                            style = LineChartStyle(canvasPaddingValues = PaddingValues(20.dp), defaultColors = LineChartColors(Color(192, 26, 206))),
                            decorations = listOf(
                                VerticalGridLines(),
                                HorizontalGridLines(),
                                //                                HorizontalGridLines(),
                                //                                BackgroundHighlight(100f, 300f, DeepPurple.copy(0.2f))
                            ),
                            dataPointStyles = mapOf(1 to complementColor, 3 to complementColor),
                            onPointClicked = {
                            }
                        )

//                        clickedValue.value?.let {
//                            Row(
//                                modifier = Modifier
//                                    .padding(all = 25.dp)
//                            ) {
//                                Text(text = "Value: ", color = Color.Gray)
//                                Text(
//                                    text = "${it.first}, ${it.second}",
//                                    color = GraphAccent2,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            }
//                        }

                    }

                    BarChart(
                        dataList = listOf(10, 23, 50, 12, 66, 78) ,
                        style = BarChartStyle(canvasPaddingValues = PaddingValues(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 20.dp)),
                        decorations = listOf(
                            VerticalGridLines(),
                            HorizontalGridLines(),
                        ),
                        xAxisLabels = XAxisLabels(listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun").map {
                            GraphData.String(it)
                        }, XAxisLabelsPosition.TOP),
                        dataPointsStyles = mapOf(2 to BarChartColors(Brush.horizontalGradient(listOf(Color.Red, Color.Red))))

                    )

                    DoublePointChart(
                        dataList = listOf(Pair(3, 7), Pair(5, 10), Pair(11, 25), Pair(13, 17), Pair(0, 5)),
                        style = DoublePointChartStyle(yAxisLabelsPosition = YAxisLabelsPosition.RIGHT),
                        dataPointStyles = mapOf(1 to DoublePointChartDataPointStyle(topPointColor = Color.Red, bottomPointRadius = 6.dp, lineWidth = 12.dp)),
                        decorations = listOf(
                            VerticalGridLines(),
                            HorizontalGridLines(),
                            BackgroundHighlight(7f, 12f, DeepPurple.copy(0.2f)),
                            HorizontalLine(7f, DeepPurple.copy(0.2f), 5f, HorizontalLineStyle.DASHED),
                            HorizontalLine(12f, DeepPurple.copy(0.2f), 5f, HorizontalLineStyle.DASHED),
                            Label(
                                "12",
                                LabelXPosition.PaddingPosition(PaddingPositionType.LEFT),
                                LabelYPosition.ChartPosition(12f),
                                Color.Green.copy(0.35f),
                            ),
                            Label("7", LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT), LabelYPosition.ChartPosition(7f), Color.Green.copy(0.8f)),
                        )
                    )


                }
            }

        }

    }
}

@Preview("BarChartPreview", widthDp = 300, heightDp = 300, showBackground = true)
@Composable()
fun BarChartPreview() {
    BarChart(
        dataList = listOf(10, 23, 50, 12, 66, 78) ,
        style = BarChartStyle(canvasPaddingValues = PaddingValues(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 20.dp)),
        decorations = listOf(
            VerticalGridLines(),
            HorizontalGridLines(),
        ),
        xAxisLabels = XAxisLabels(listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun").map {
            GraphData.String(it)
        }, XAxisLabelsPosition.TOP)
    )
}

@Preview("LineChartPreview", widthDp = 300, heightDp = 300, showBackground = true)
@Composable()
fun LineChartPreview() {

    val complementColor = LineChartColors(Color(206, 26, 54))

    LineChart(
        xAxisLabels = XAxisLabels(listOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat").map {
            GraphData.String(it)
        }),
        yAxisData = listOf(200, 40, 60, 450, 700, 30, 50),
        style = LineChartStyle(canvasPaddingValues = PaddingValues(20.dp), defaultColors = LineChartColors(Color(192, 26, 206))),
        decorations = listOf(
            VerticalGridLines(),
            HorizontalGridLines(),
            //                                HorizontalGridLines(),
            //                                BackgroundHighlight(100f, 300f, DeepPurple.copy(0.2f))
        ),
        dataPointStyles = mapOf(1 to complementColor, 3 to complementColor),
        onPointClicked = {
        }
    )
}

@Preview("DoublePointChartPreview", widthDp = 300, heightDp = 300, showBackground = true)
@Composable()
fun DoublePointChartPreview() {
    DoublePointChart(
        dataList = listOf(Pair(3, 7), Pair(5, 10), Pair(11, 25), Pair(13, 17), Pair(0, 5)),
        style = DoublePointChartStyle(yAxisLabelsPosition = YAxisLabelsPosition.RIGHT, isXAxisLabelVisible = false),
        decorations = listOf(
            VerticalGridLines(),
            HorizontalGridLines(),
            BackgroundHighlight(7f, 12f, DeepPurple.copy(0.2f)),
            HorizontalLine(7f, DeepPurple.copy(0.2f), 5f, HorizontalLineStyle.DASHED),
            HorizontalLine(12f, DeepPurple.copy(0.2f), 5f, HorizontalLineStyle.DASHED),
            Label(
                "12",
                LabelXPosition.PaddingPosition(PaddingPositionType.LEFT),
                LabelYPosition.ChartPosition(12f),
                Color.Green.copy(0.35f),
            ),
            Label("7", LabelXPosition.PaddingPosition(PaddingPositionType.RIGHT), LabelYPosition.ChartPosition(7f), Color.Green.copy(0.8f)),
        )
    )
}
