package com.example.mystocksapp.data

import com.google.gson.annotations.SerializedName

data class StockDetails(
    @SerializedName("count") val count: Int?,
    @SerializedName("request_id") val requestId: String,
    @SerializedName("status") val status: String?,

    // TickerDetails
    @SerializedName("active") val active: Boolean?,
    @SerializedName("cik") val cik: String?,
    @SerializedName("composite_figi") val compositeFigi: String?,
    @SerializedName("currency_name") val currencyName: String?,
    @SerializedName("delisted_utc") val delistedUtc: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("homepage_url") val homepageUrl: String?,
    @SerializedName("list_date") val listDate: String?,
    @SerializedName("locale") val locale: String?,
    @SerializedName("market") val market: String?,
    @SerializedName("market_cap") val marketCap: Double?,
    @SerializedName("name") val name: String,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("primary_exchange") val primaryExchange: String?,
    @SerializedName("round_lot") val roundLot: Int?,
    @SerializedName("share_class_figi") val shareClassFigi: String?,
    @SerializedName("share_class_shares_outstanding") val shareClassSharesOutstanding: Long?,
    @SerializedName("sic_code") val sicCode: String?,
    @SerializedName("sic_description") val sicDescription: String?,
    @SerializedName("ticker") val ticker: String,
    @SerializedName("ticker_root") val tickerRoot: String?,
    @SerializedName("ticker_suffix") val tickerSuffix: String?,
    @SerializedName("total_employees") val totalEmployees: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("weighted_shares_outstanding") val weightedSharesOutstanding: Long?,

    // Address
    @SerializedName("address.address1") val address1: String?,
    @SerializedName("address.city") val city: String?,
    @SerializedName("address.postal_code") val postalCode: String?,
    @SerializedName("address.state") val state: String?,

    // Branding
    @SerializedName("branding.icon_url") val iconUrl: String?,
    @SerializedName("branding.logo_url") val logoUrl: String?
)