package com.example.mystocksapp._const

object Routes {

    const val StocksList = "stocksList"
    const val StocksDetail = "stocksDetail/{ticker}"
    const val SavedStocks = "savedStocks"
    const val NewsDetail = "newsDetail/{newsId}"
    const val NewsList = "newsList"

    fun stockDetail(ticker: String): String {
        return "stocksDetail/$ticker"
    }

    fun newsDetail(newsId: Long): String {
        return "newsDetail/$newsId"
    }

}