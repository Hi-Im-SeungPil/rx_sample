package com.feel.jeon.rx_sample.ui.day2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.feel.jeon.rx_sample.ui.day1.DayOneStateHolder
import com.feel.jeon.rx_sample.ui.day1.rememberDayOneStateHolder

@Composable
fun rememberDayTwoStateHolder() = remember() {
    DayTwoStateHolder()
}

@Composable
fun DayTwoScreen() {
    val holder = rememberDayTwoStateHolder()
    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = { holder.map() }) {
            Text(text = "map")
        }
        TextButton(onClick = { holder.flatMap() }) {
            Text(text = "flatMap")
        }
        TextButton(onClick = { holder.switchMap() }) {
            Text(text = "switchMap")
        }
        TextButton(onClick = { holder.concatMap() }) {
            Text(text = "concatMap")
        }
        TextButton(onClick = { holder.buffer() }) {
            Text(text = "buffer")
        }
        TextButton(onClick = { holder.scan() }) {
            Text(text = "scan")
        }
        TextButton(onClick = { holder.groupBy() }) {
            Text(text = "groupBy")
        }
    }
}