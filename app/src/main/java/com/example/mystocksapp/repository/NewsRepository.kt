package com.example.mystocksapp.repository

import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.data.Entity.SavedNewsEntity
import com.example.mystocksapp.data.Entity.SavedNewsEntity_
import io.objectbox.Box

class NewsRepository(private val savedNewsBox:Box<SavedNewsEntity>) {

    fun saveNews(news: List<SavedNewsEntity>) {
        val existingApiIds = savedNewsBox.query()
            .build()
            .find()
            .map { it.apiId }

        val newsToSave = news.filter { it.apiId !in existingApiIds }

        if (newsToSave.isNotEmpty()) {
            savedNewsBox.put(newsToSave)
        }
    }
    fun clearAllNews() {
        savedNewsBox.removeAll()
    }

    fun getAllByDate(): ApiResult<List<SavedNewsEntity>> {
            return try {
                val results = savedNewsBox.query()
                    .orderDesc(SavedNewsEntity_.publishedUtc)
                    .build()
                    .find()

                ApiResult.Success(results)
    } catch (e: Exception) {
        ApiResult.Error("Error fetching news from DB: ${e.message}")
        }
    }

    fun getById(Id : String) : SavedNewsEntity?{
        return savedNewsBox.get(Id.toLong())
    }
}

