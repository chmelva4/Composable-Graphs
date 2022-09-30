package com.jaikeerthick.composable_graphs.charts.barChart

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jaikeerthick.composable_graphs.color.Gradient1
import com.jaikeerthick.composable_graphs.color.Gradient2

data class BarChartDataPointStyle(

    val fillGradient: Brush = Brush.verticalGradient(
        listOf(Gradient1, Gradient2)
    ),
    val barWidth: BarWidth = BarWidth.FullWidth(),
    val cornerRadiusPx: Float = 0f
)

sealed class BarWidth() {

    abstract fun getLeftSideXCoordinate(minX: Float, maxX: Float): Float
    abstract fun getSize(minX: Float, maxX: Float): Float

    class FullWidth(): BarWidth() {
        override fun getLeftSideXCoordinate(minX: Float, maxX: Float): Float {
            return minX
        }

        override fun getSize(minX: Float, maxX: Float): Float {
            return maxX - minX
        }
    }

    class PercentageWidth(
        private val percentage: Float
    ): BarWidth() {
        override fun getLeftSideXCoordinate(minX: Float, maxX: Float): Float {
            val maxSize = maxX - minX
            val actualSize = getSize(minX, maxX)
            return minX + ((maxSize - actualSize) / 2f)
        }

        override fun getSize(minX: Float, maxX: Float): Float {
            return (maxX - minX) * percentage
        }
    }

    class PxWidth(
        private val pxWidth: Float
    ): BarWidth() {
        override fun getLeftSideXCoordinate(minX: Float, maxX: Float): Float {
            val maxSize = maxX - minX
            val actualSize = getSize(minX, maxX)
            return minX + ((maxSize - actualSize) / 2f)
        }

        override fun getSize(minX: Float, maxX: Float): Float {
            return pxWidth
        }
    }

}
