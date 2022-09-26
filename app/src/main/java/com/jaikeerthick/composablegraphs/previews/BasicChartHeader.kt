package com.jaikeerthick.composablegraphs.previews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicChartHeader(
    title: String,
    rightCornerText: String,
    boldText: String,
    theRestOfText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.alignByBaseline())
            Spacer(modifier = Modifier.weight(1f))
            Text(rightCornerText, fontSize = 12.sp, color = Color(161, 164, 167), modifier = Modifier.alignByBaseline())
        }
        Row (){
            Text(text = boldText, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.alignByBaseline())
            Spacer(modifier = Modifier.width(4.dp))
            Text(theRestOfText, fontSize = 14.sp, modifier = Modifier.alignByBaseline())
        }
    }
}

@Preview
@Composable
fun headerPreview() {
    MaterialTheme {
        BasicChartHeader(title = "Apetite", "2 hours ago", "2", "meals out of 3")
    }
}