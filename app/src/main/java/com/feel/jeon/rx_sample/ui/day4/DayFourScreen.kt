package com.feel.jeon.rx_sample.ui.day4

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun rememberDayFourStateHolder() = remember() {
    DayFourStateHolder()
}

@Composable
fun DayFourScreen() {
    val holder = rememberDayFourStateHolder()
    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = { holder.combinedLatest() }) {
            Text(text = "combinedLatest")
        }
        TextButton(onClick = { holder.zip() }) {
            Text(text = "zip")
        }
        TextButton(onClick = { holder.merge() }) {
            Text(text = "merge")
        }
    }
}