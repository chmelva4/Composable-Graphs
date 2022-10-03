package com.jaikeerthick.composable_graphs.charts.lineChart

import android.app.PendingIntent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.charts.common.PointStyle
import com.jaikeerthick.composable_graphs.color.GraphAccent
import com.jaikeerthick.composable_graphs.color.PointHighlight

data class LineChartDataPointStyle(
    val pointStyle: PointStyle = PointStyle.FilledPoint(5f, GraphAccent)
)
