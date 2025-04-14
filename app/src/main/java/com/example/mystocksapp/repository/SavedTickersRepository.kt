package com.example.mystocksapp.repository

import com.example.mystocksapp.data.SavedTickerEntity
import com.example.mystocksapp.data.SavedTickerEntity_
import com.example.mystocksapp.data.Stocks
import io.objectbox.Box
import io.objectbox.query.QueryBuilder

class SavedTickersRepository (private val savedTickerBox: Box<SavedTickerEntity>)
{

   fun saveTicker(ticker:Stocks){
        val existingEntity = savedTickerBox.query()
            .equal(SavedTickerEntity_.ticker, ticker.ticker, QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().findFirst()

        if (existingEntity != null) {
            // Entita již existuje, aktualizujeme ji
            existingEntity.name = ticker.name
            existingEntity.ticker = ticker.ticker
            savedTickerBox.put(existingEntity)
        } else {
            // Entita neexistuje, vytvoříme novou
            val savedTickerEntity = SavedTickerEntity(name = ticker.name, ticker = ticker.ticker)
            savedTickerBox.put(savedTickerEntity)
        }
    }


    fun removeSavedTicker(ticker: String) {
        val query = savedTickerBox.query()
            .equal(SavedTickerEntity_.ticker, ticker, QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build()
        val result = query.findFirst()
        if (result != null) {
            savedTickerBox.remove(result)
        }
        query.close()
    }

    fun getAllSavedTickers(): List<SavedTickerEntity> {
        return savedTickerBox.all
    }

}