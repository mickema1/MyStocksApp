package com.example.mystocksapp.Api

import com.example.mystocksapp.data.Dao.StockDetails
import com.example.mystocksapp.data.Dao.Stocks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StocksApi {
    @GET("v3/reference/tickers?&market=stocks")
    suspend fun getStocksList(
        @Query("search") query: String,
        @Query("limit") limit: Int = 1000
        ): Response<StocksResponse<List<Stocks>>>

    @GET("v3/reference/tickers/{ticker}")
    suspend fun getStockDetails(
        @Path("ticker") ticker: String
    ): Response<StocksResponse<StockDetails>>

    @GET("v2/aggs/ticker/{stocksTicker}/range/{multiplier}/{timespan}/{from}/{to}")
    suspend fun getAggregateGraph(
        @Path("stocksTicker") stocksTicker: String,
        @Path("multiplier") multiplier: Int,
        @Path("timespan") timespan: String,
        @Path("from") from: String,
        @Path("to") to: String,

    ): Response<AggregateBarsResponse>

    @GET("/v2/reference/news")
    suspend fun getStockNews(
        @Query("published_utc") publishedUtc:String,
        @Query("published_utc.gt") publishedUtcGt:String,
        @Query("published_utc.lte") publishedUtcLte:String,
        @Query("order") order:String = "asc",
        @Query("limit") limit:Int = 1000,
        @Query("sort") sort:String = "published_utc"
    ) : Response<StocksResponse<StockDetails>>

}