package com.example.mystocksapp.data.Entity

import com.example.mystocksapp.data.Entity.LinkTableEntity.KeywordToNewsEntity
import com.example.mystocksapp.data.Entity.LinkTableEntity.TickerToNewsEntity
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany


@Entity
data class SavedNewsEntity (
    @Id var id: Long = 0,
    var name : String,
    var homepageUrl: String,
    var logo: String,
    var favicon: String,

    //article info
    var title : String,
    var author : String,
    var publishedUtc : String,
    var articleUrl : String,
    var image : String,
    var description : String,
){
        @Backlink(to = "news")
        lateinit var tickers: ToMany<TickerToNewsEntity>

        @Backlink(to = "news")
        lateinit var keywords: ToMany<KeywordToNewsEntity>
}
