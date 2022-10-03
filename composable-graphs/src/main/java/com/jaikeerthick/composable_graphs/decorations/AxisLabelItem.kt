package com.jaikeerthick.composable_graphs.decorations

class AxisLabelItem(
    val chartValue: Float,
    label: String? = null
) {
    val label: String = label ?: chartValue.toString()
}
