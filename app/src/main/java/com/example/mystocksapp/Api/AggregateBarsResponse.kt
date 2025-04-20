package com.example.mystocksapp.Api

import com.example.mystocksapp.data.Dao.AggregateBar
import com.google.gson.annotations.SerializedName

data class AggregateBarsResponse(
    @SerializedName("adjusted") val adjusted: Boolean,
    @SerializedName("next_url") val nextUrl: String?,
    @SerializedName("queryCount") val queryCount: Int,
    @SerializedName("request_id") val requestId: String,
    @SerializedName("results") val results: List<AggregateBar>?,
    @SerializedName("resultsCount") val resultsCount: Int,
    @SerializedName("status") val status: String,
    @SerializedName("ticker") val ticker: String
)