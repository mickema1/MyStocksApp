package com.example.mystocksapp.Api

import com.example.mystocksapp.data.StockDetails
import com.google.gson.annotations.SerializedName

data class StockDetailResponse (
        @SerializedName("results") val data: StockDetails?,   // results can be null
        @SerializedName("request_id") val requestId: String?,  // optional
        @SerializedName("count") val count: Int?,              // optional
        @SerializedName("status") val status: String?          // optional
)