package com.jaikeerthick.composable_graphs.charts.common

import com.jaikeerthick.composable_graphs.charts.utils.GraphHelper

sealed class YScale {
    fun chartYToCanvasY(y: Float, basicChartDrawer: BasicChartDrawer): Float {
        return basicChartDrawer.paddingTopPx + (basicChartDrawer.gridHeight * (1 - ((y - min) / (max - min))))
    }
    open fun setupValuesFromData(data: List<Number>) {}

    protected abstract var min: Float
    protected abstract var max: Float

    class ZeroToMaxScale(): YScale() {
        override fun setupValuesFromData(data: List<Number>) {
           max = GraphHelper.getAbsoluteMax(data).toFloat()
        }

        override var min = 0f
        override var max = 100f
    }

    class  CustomScale(
        override var min: Float,
        override var max: Float,
    ): YScale() {
    }

    class MinMaxAsNearestValue(
        val modValue: Float,
        dataMin: Float,
        dataMax: Float
    ): YScale() {

        override var min = 0f
        override var max = 100f

        init {
            max = dataMax - dataMax.mod(modValue) + modValue
            min = dataMin - dataMin.mod(modValue)
        }
    }

    class MinMaxAsNearestValueFromData(
        val modValue: Float,
    ): YScale() {

        override var min = 0f
        override var max = 100f

        override fun setupValuesFromData(data: List<Number>) {

            val dataMax = GraphHelper.getAbsoluteMax(data).toFloat()
            val dataMin = GraphHelper.getAbsoluteMin(data).toFloat()

            max = dataMax - dataMax.mod(modValue) + modValue
            min = dataMin - dataMin.mod(modValue)
        }
    }
}