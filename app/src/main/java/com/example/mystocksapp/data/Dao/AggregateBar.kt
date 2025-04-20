package com.example.mystocksapp.data.Dao

import com.google.gson.annotations.SerializedName

data class AggregateBar(
    @SerializedName("c") val close: Double,
    @SerializedName("h") val high: Double,
    @SerializedName("l") val low: Double,
    @SerializedName("n") val transactions: Int,
    @SerializedName("o") val open: Double,
    @SerializedName("t") val timestamp: Long,
    @SerializedName("v") val volume: Long,
    @SerializedName("vw") val vwap: Double
)