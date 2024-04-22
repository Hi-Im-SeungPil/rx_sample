package com.feel.jeon.rx_sample.ui.retrofit_rx.res

class ApiResponse : ArrayList<ApiResponseItem>()

data class ApiResponseItem(
    val english_name: String,
    val korean_name: String,
    val market: String,
    val market_event: MarketEvent,
    val market_warning: String
)

data class Caution(
    val CONCENTRATION_OF_SMALL_ACCOUNTS: Boolean,
    val DEPOSIT_AMOUNT_SOARING: Boolean,
    val GLOBAL_PRICE_DIFFERENCES: Boolean,
    val PRICE_FLUCTUATIONS: Boolean,
    val TRADING_VOLUME_SOARING: Boolean
)

data class MarketEvent(
    val caution: Caution,
    val warning: Boolean
)

