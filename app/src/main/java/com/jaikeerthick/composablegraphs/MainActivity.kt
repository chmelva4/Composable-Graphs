package com.jaikeerthick.composablegraphs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.color.*
import com.jaikeerthick.composable_graphs.composables.BarGraph
import com.jaikeerthick.composable_graphs.composables.DoublePointGraph
import com.jaikeerthick.composable_graphs.composables.LineGraph
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.decorations.BackgroundHighlight
import com.jaikeerthick.composable_graphs.decorations.HorizontalGridLines
import com.jaikeerthick.composable_graphs.decorations.VerticalGridLines
import com.jaikeerthick.composable_graphs.decorations.XAxisLabels
import com.jaikeerthick.composable_graphs.style.BarGraphStyle
import com.jaikeerthick.composable_graphs.style.BarGraphVisibility
import com.jaikeerthick.composable_graphs.style.BarGraphXAxisLabelStyle
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.style.LinearGraphVisibility
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                val style = BarGraphStyle(
                    visibility = BarGraphVisibility(
                        isYAxisLabelVisible = true,
//                        isGridVisible = true

                    ),
                    xAxisLabelStyle = BarGraphXAxisLabelStyle.CENTERED
                )
                val style2 = LineGraphStyle(
                    visibility = LinearGraphVisibility(
                        isHeaderVisible = true,
                        isGridVisible = true,
                        isYAxisLabelVisible = true,
                        isCrossHairVisible = true
                    ),
                    colors = LinearGraphColors(
                        lineColor = GraphAccent2,
                        pointColor = GraphAccent2,
                        clickHighlightColor = PointHighlight2,
                        fillGradient = Brush.verticalGradient(
                            listOf(Gradient3, Gradient2)
                        )
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(color = Color.White),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(modifier = Modifier.fillMaxWidth()){

                        val clickedValue: MutableState<Pair<Any,Any>?> = remember{ mutableStateOf(null) }

                        LineGraph(
                            xAxisData = XAxisLabels(listOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat").map {
                                GraphData.String(it)
                            }),
                            yAxisData = listOf(200, 40, 60, 450, 700, 30, 50),
                            style = style2,
                            decorations = listOf(VerticalGridLines(), HorizontalGridLines()),
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

                    BarGraph(
                        dataList = listOf(10, 23, 50, 12, 66) ,
                        style = style,
                        decorations = listOf(HorizontalGridLines(), VerticalGridLines(), BackgroundHighlight(5f, 10f, Color.Cyan.copy(0.35f)))
                    )

                    DoublePointGraph(
                        dataList = listOf(Pair(3, 7), Pair(5, 10), Pair(11, 23), Pair(13, 17), Pair(0, 5)),
                        style = style,
                        decorations = listOf(VerticalGridLines(), HorizontalGridLines())
                    )


                }
            }

        }

    }
}