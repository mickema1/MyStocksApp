package com.example.mystocksapp.data.Dao

import com.google.gson.annotations.SerializedName

data class StockNews(
    @SerializedName("id") val apiId : String,

    //publisher
    @SerializedName("name") val name : String,
    @SerializedName("homepage_url") val homepageUrl: String,
    @SerializedName("logo_url") val logo: String,
    @SerializedName("favicon_url") val favicon: String,

    //article info
    @SerializedName("title") val title : String,
    @SerializedName("author") val author : String,
    @SerializedName("published_utc") val publishedUtc : String,
    @SerializedName("article_url") val articleUrl : String,
    @SerializedName("tickers") val tickers : List<String>,
    @SerializedName("image_url") val image : String,
    @SerializedName("description") val description : String,
    @SerializedName("keywords") val keywords : List<String>,

    )
