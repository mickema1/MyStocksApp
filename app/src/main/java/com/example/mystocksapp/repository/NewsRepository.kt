package com.example.mystocksapp.repository

import com.example.mystocksapp.data.Dao.StockNews
import io.objectbox.Box

class NewsRepository(private val savedNewsBox:Box<StockNews>) {
}