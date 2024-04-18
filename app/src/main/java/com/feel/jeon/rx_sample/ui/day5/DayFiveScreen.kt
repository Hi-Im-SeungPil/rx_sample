package com.feel.jeon.rx_sample.ui.day5

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun rememberDayFiveStateHolder() = remember() {
    DayFiveStateHolder()
}

@Composable
fun DayFiveScreen() {
    val holder = rememberDayFiveStateHolder()
    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = { holder.exceptionCode() }) {
            Text(text = "exceptionCode")
        }
        TextButton(onClick = { holder.exceptionHandling() }) {
            Text(text = "exceptionHandling")
        }
        TextButton(onClick = { holder.onErrorReturn() }) {
            Text(text = "onErrorReturn")
        }
        TextButton(onClick = { holder.onErrorResumeNext() }) {
            Text(text = "onErrorResumeNext")
        }
        TextButton(onClick = { holder.retry() }) {
            Text(text = "retry")
        }
        TextButton(onClick = { holder.doOnEach() }) {
            Text(text = "doOnEach")
        }
        TextButton(onClick = { holder.doOnNext() }) {
            Text(text = "doOnNext")
        }
        TextButton(onClick = { holder.doOnSubscribe() }) {
            Text(text = "doOnSubscribe")
        }
        TextButton(onClick = { holder.doOnComplete() }) {
            Text(text = "doOnComplete")
        }
        TextButton(onClick = { holder.doOnError() }) {
            Text(text = "doOnError")
        }
        TextButton(onClick = { holder.doOnTerminate() }) {
            Text(text = "doOnTerminate")
        }
        TextButton(onClick = { holder.doOnDispose() }) {
            Text(text = "doOnDispose")
        }
        TextButton(onClick = { holder.doFinally() }) {
            Text(text = "doFinally")
        }
    }
}