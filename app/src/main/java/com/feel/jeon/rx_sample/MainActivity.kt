package com.feel.jeon.rx_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.feel.jeon.rx_sample.ui.day1.DayOneScreen
import com.feel.jeon.rx_sample.ui.day2.DayTwoScreen
import com.feel.jeon.rx_sample.ui.day3.DayThreeScreen
import com.feel.jeon.rx_sample.ui.day4.DayFourScreen
import com.feel.jeon.rx_sample.ui.day5.DayFiveScreen
import com.feel.jeon.rx_sample.ui.retrofit_rx.AppRetrofit
import com.feel.jeon.rx_sample.ui.theme.Rx_sampleTheme
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.annotations.Async.Schedule


class MainActivity : ComponentActivity() {
    private val pageState = mutableIntStateOf(1)
    private val titleState = mutableStateOf("Day1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogger()

        AppRetrofit.upbitApi.getMarketCode().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onSuccess = {
                            Logger.e(it.toString())
                },
                onError = {
                    Logger.e(it.message.toString())
                }
            ).dispose()

        setContent {
            Rx_sampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            MainTopAppBar()
                        }
                    ) { paddingValue ->
                        Box(modifier = Modifier.padding(paddingValue)) {
                            when (pageState.intValue) {
                                1 -> DayOneScreen()
                                2 -> DayTwoScreen()
                                3 -> DayThreeScreen()
                                4 -> DayFourScreen()
                                5 -> DayFiveScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainTopAppBar() {
        TopAppBar(title = { Text(text = "Day ${pageState.intValue}") },
            navigationIcon = {
                IconButton(onClick = {
                    if (pageState.intValue > 1) {
                        pageState.intValue -= 1
                        titleState.value = "Day${pageState.intValue}"
                    }
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            },
            actions = {
                IconButton(onClick = {
                    if (pageState.intValue < 6) {
                        pageState.intValue += 1
                        titleState.value = "Day${pageState.intValue}"
                    }
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
                }
            })
    }

    private fun initLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}