package com.example.mystocksapp.Api

import com.example.mystocksapp.data.Stocks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StocksApi {
    @GET("v3/reference/tickers?")
    suspend fun getStocksList(): Response<StocksResponse<List<Stocks>>>
}