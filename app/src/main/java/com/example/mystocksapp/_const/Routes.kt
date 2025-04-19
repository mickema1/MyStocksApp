package com.example.mystocksapp._const

object Routes {

    const val StocksList = "stocksList"
    const val Settings = "settings"
    const val StocksDetail = "stocksDetail/{ticker}"
    const val SavedStocks = "savedStocks"

    fun stockDetail(ticker: String): String {
        return "stocksDetail/$ticker"
    }

}