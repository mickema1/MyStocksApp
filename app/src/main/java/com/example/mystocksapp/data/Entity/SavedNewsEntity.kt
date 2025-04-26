package com.example.mystocksapp.data.Entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique



@Entity
data class SavedNewsEntity (
    @Id var id: Long = 0,
    @Index @Unique var apiId : String,
    var name : String? = null,
    var homepageUrl: String? = null,
    var logo: String? = null,
    var favicon: String? = null,

    //article info
    var title : String,
    var author : String?=null,
    @Index var publishedUtc : String,
    var articleUrl : String? = null,
    var image : String? = null,
    var description : String? = null,
    var keywords : String? = null,
    var tickers : String? = null
)
