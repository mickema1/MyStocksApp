package com.example.mystocksapp.data.Entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

//@Entity todo
data class KeywordEntity(
    @Id var id: Long = 0,
    var value: String
)
