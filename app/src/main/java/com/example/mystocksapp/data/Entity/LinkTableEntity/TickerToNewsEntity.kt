package com.example.mystocksapp.data.Entity.LinkTableEntity

import com.example.mystocksapp.data.Entity.SavedNewsEntity
import com.example.mystocksapp.data.Entity.TickerIdEntity
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

//@Entity //todo
data class TickerToNewsEntity (
    @Id var id:Long=0,
){
    lateinit var news: ToOne<SavedNewsEntity>
    lateinit var ticker: ToOne<TickerIdEntity>
}