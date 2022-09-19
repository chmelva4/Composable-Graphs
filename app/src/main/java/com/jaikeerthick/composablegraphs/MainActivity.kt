package com.jaikeerthick.composablegraphs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.color.*
import com.jaikeerthick.composable_graphs.charts.BarChart
import com.jaikeerthick.composable_graphs.charts.barChart.BarChartStyle
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChart
import com.jaikeerthick.composable_graphs.charts.doublePointChart.DoublePointChartStyle
import com.jaikeerthick.composable_graphs.charts.common.GraphData
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChart
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartColors
import com.jaikeerthick.composable_graphs.charts.lineChart.LineChartStyle
import com.jaikeerthick.composable_graphs.decorations.BackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.HorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.HorizontalLine
import com.jaikeerthick.composable_graphs.decorations.HorizontalLineStyle
import com.jaikeerthick.composable_graphs.decorations.VerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels

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

                        LineChart(
                            xAxisLabels = XAxisLabels(listOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat").map {
                                GraphData.String(it)
                            }),
                            yAxisData = listOf(200, 40, 60, 450, 700, 30, 50),
                            style = LineChartStyle(canvasPaddingValues = PaddingValues(20.dp)),
                            decorations = listOf(
                                VerticalGridLines(),
                                HorizontalGridLines(),
//                                HorizontalGridLines(),
//                                BackgroundHighlight(100f, 300f, DeepPurple.copy(0.2f))
                            ),
                            onPointClicked = {
                                clickedValue.value = it
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
                        style = BarChartStyle(),
                        decorations = listOf(
                            VerticalGridLines(),
                            HorizontalGridLines(),
                        )
                    )

                    DoublePointChart(
                        dataList = listOf(Pair(3, 7), Pair(5, 10), Pair(11, 25), Pair(13, 17), Pair(0, 5)),
                        style = DoublePointChartStyle(),
                        decorations = listOf(
                            VerticalGridLines(),
                            HorizontalGridLines(),
                            BackgroundHighlight(7f, 12f, DeepPurple.copy(0.2f)),
                            HorizontalLine(7f, DeepPurple.copy(0.2f), 5f, HorizontalLineStyle.DASHED),
                            HorizontalLine(12f, DeepPurple.copy(0.2f), 5f, HorizontalLineStyle.DASHED),
                        )
                    )


                }
            }

        }

    }
}