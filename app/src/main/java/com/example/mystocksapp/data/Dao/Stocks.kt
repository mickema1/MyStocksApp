package com.example.mystocksapp.data.Dao

import com.google.gson.annotations.SerializedName


data class Stocks(
    @SerializedName("active") val active: Boolean,
    @SerializedName("cik") val cik: String,
    @SerializedName("composite_figi") val compositeFigi: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("last_updated_utc") val lastUpdatedUtc: String,
    @SerializedName("locale") val locale: String,
    @SerializedName("market") val market: String,
    @SerializedName("name") val name: String,
    @SerializedName("primary_exchange") val primaryExchange: String,
    @SerializedName("share_class_figi") val shareClassFigi: String,
    @SerializedName("ticker") val ticker: String,
    @SerializedName("type") val type: String
)