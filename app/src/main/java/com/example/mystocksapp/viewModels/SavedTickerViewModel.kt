package com.example.mystocksapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.Entity.SavedTickerEntity
import com.example.mystocksapp.data.Dao.Stocks
import com.example.mystocksapp.repository.SavedTickersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedTickerViewModel(
    private val savedTickersRepository: SavedTickersRepository,
    private val stocksApi: StocksApi
)   :ViewModel() {

    private val _savedTickers = MutableStateFlow<ApiResult<List<SavedTickerEntity>>>(ApiResult.Loading)
    val savedTickers: StateFlow<ApiResult<List<SavedTickerEntity>>> = _savedTickers.asStateFlow()
    init {
        loadSavedTickers()
    }

    fun saveTicker(stock : Stocks) {
        viewModelScope.launch {
            savedTickersRepository.saveTicker(stock)
            loadSavedTickers()
        }
    }

    fun removeSavedTicker(ticker: String) {
        viewModelScope.launch {
            savedTickersRepository.removeSavedTicker(ticker)
            loadSavedTickers()
        }
    }

    fun loadSavedTickers() {
        viewModelScope.launch {
            try {
                val favourites = savedTickersRepository.getAllSavedTickers()
                _savedTickers.value = ApiResult.Success(favourites)
            } catch (e: Exception) {
                _savedTickers.value = ApiResult.Error("Exception fetching saved tickers: ${e.message}")
            }
        }
    }
}