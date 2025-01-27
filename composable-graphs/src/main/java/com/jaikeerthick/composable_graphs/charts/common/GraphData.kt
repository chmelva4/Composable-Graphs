package com.jaikeerthick.composable_graphs.charts.common

sealed class GraphData(val text: kotlin.String) {

    data class Number(val item: kotlin.Number): GraphData(item.toString())
    
    data class String(val item: kotlin.String): GraphData(item)

}