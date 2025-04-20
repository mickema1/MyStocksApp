package com.example.mystocksapp.Api

import com.google.gson.annotations.SerializedName

data class StocksResponse<T>(
    @SerializedName("results") val data: T,
    @SerializedName("request_id") val requestId: String?,
    @SerializedName("count") val count: Int?,
    @SerializedName("status") val status: String?
)