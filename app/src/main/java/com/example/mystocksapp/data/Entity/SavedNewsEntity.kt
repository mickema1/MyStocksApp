package com.example.mystocksapp.data.Entity

import com.example.mystocksapp.data.Entity.LinkTableEntity.KeywordToNewsEntity
import com.example.mystocksapp.data.Entity.LinkTableEntity.TickerToNewsEntity
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany


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
){
    //todo
        //@Backlink(to = "news")
        //lateinit var tickers: ToMany<TickerToNewsEntity>

        //@Backlink(to = "news")
        //lateinit var keywords: ToMany<KeywordToNewsEntity>
}
