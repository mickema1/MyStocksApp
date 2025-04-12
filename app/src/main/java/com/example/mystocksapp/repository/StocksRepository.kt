package com.example.mystocksapp.repository

import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.Api.StocksResponse
import com.example.mystocksapp.data.Stocks

class StocksRepository(private val stocksApi: StocksApi) {

    suspend fun getStockList(): StocksResponse<List<Stocks>>? {
        val response = stocksApi.getStocksList()
        return if (response.isSuccessful) {
            response.body() // Returns the list of stocks
        } else {
            //todo error handling
            null
        }
    }
}