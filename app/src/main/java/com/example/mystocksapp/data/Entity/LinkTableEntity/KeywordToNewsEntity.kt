package com.example.mystocksapp.data.Entity.LinkTableEntity

import com.example.mystocksapp.data.Entity.KeywordEntity
import com.example.mystocksapp.data.Entity.SavedNewsEntity
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

//@Entity todo
data class KeywordToNewsEntity(
    @Id var id: Long = 0
){
    lateinit var news: ToOne<SavedNewsEntity>
    lateinit var keyword: ToOne<KeywordEntity>
}
