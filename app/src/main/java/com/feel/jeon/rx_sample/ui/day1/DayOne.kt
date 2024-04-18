package com.feel.jeon.rx_sample.ui.day1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun rememberDayOneStateHolder(

) = remember() {
    DayOneStateHolder()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayOneScreen() {
    val holder = rememberDayOneStateHolder()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Day1") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
                    }
                })
        }
    ) { paddingValue ->
        Column(modifier = Modifier.padding(paddingValue)) {
            TextButton(onClick = { holder.observableCreate() }) {
                Text(text = "Observable Create")
            }
            TextButton(onClick = { holder.just() }) {
                Text(text = "just")
            }
            TextButton(onClick = { holder.fromXxx() }) {
                Text(text = "from XXX")
            }
            TextButton(onClick = { holder.emptyNeverError() }) {
                Text(text = "emptyNeverError")
            }
            TextButton(onClick = { holder.interval() }) {
                Text(text = "interval")
            }
            TextButton(onClick = { holder.range() }) {
                Text(text = "range")
            }
            TextButton(onClick = { holder.timer() }) {
                Text(text = "timer")
            }
            TextButton(onClick = { holder.defer() }) {
                Text(text = "defer")
            }
            TextButton(onClick = { holder.disposable() }) {
                Text(text = "disposable")
            }
            TextButton(onClick = { holder.connectableObservable() }) {
                Text(text = "connectableObservable")
            }
            TextButton(onClick = { holder.autoConnect() }) {
                Text(text = "autoConnect")
            }
        }
    }
}