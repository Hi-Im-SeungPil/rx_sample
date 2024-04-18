package com.feel.jeon.rx_sample.ui.day3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.feel.jeon.rx_sample.ui.day1.DayOneStateHolder
import com.feel.jeon.rx_sample.ui.day2.rememberDayTwoStateHolder

@Composable
fun rememberDayThreeStateHolder() = remember() {
    DayThreeStateHolder()
}

@Composable
fun DayThreeScreen() {
    val holder = rememberDayThreeStateHolder()
    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = { holder.debounce() }) {
            Text(text = "debounce")
        }
        TextButton(onClick = { holder.distinct() }) {
            Text(text = "distinct")
        }
        TextButton(onClick = { holder.elementAt() }) {
            Text(text = "elementAt")
        }
        TextButton(onClick = { holder.filter() }) {
            Text(text = "filter")
        }
        TextButton(onClick = { holder.sample() }) {
            Text(text = "sample")
        }
        TextButton(onClick = { holder.skip() }) {
            Text(text = "skip")
        }
        TextButton(onClick = { holder.take() }) {
            Text(text = "take")
        }
        TextButton(onClick = { holder.all() }) {
            Text(text = "all")
        }
        TextButton(onClick = { holder.amb() }) {
            Text(text = "amb")
        }
    }
}