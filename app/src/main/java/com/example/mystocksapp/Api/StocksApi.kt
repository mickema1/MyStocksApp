package com.example.mystocksapp.Api

import com.example.mystocksapp.data.Stocks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StocksApi {
    @GET("v3/reference/tickers?&market=stocks")
    suspend fun getStocksList(
        @Query("search") query: String,
        @Query("market") market: String = "stocks",
        @Query("limit") limit: Int = 1000
        ): Response<StocksResponse<List<Stocks>>>
}