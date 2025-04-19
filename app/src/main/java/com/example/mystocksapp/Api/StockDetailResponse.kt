package com.example.mystocksapp.Api

import com.example.mystocksapp.data.StockDetails
import com.google.gson.annotations.SerializedName

data class StockDetailResponse (
        @SerializedName("results") val data: StockDetails?,
        @SerializedName("request_id") val requestId: String?,
        @SerializedName("count") val count: Int?,
        @SerializedName("status") val status: String?
)