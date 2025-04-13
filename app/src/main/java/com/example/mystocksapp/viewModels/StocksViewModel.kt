package com.example.mystocksapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.Stocks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StocksViewModel(private val stocksApi: StocksApi): ViewModel() {
    private val _stocksList = MutableStateFlow<ApiResult<List<Stocks>>>(ApiResult.Loading)
    val stocksList: StateFlow<ApiResult<List<Stocks>>> = _stocksList.asStateFlow()
    private val _searchQuery = MutableStateFlow("")

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getStockList(query: String) {
        viewModelScope.launch {
            _stocksList.value = ApiResult.Loading
            try {
                val response = stocksApi.getStocksList(query)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        _stocksList.value = ApiResult.Success(data)
                        Log.d("StocksViewModel", "Found ${data.size} results for \"$query\"")
                    } else {
                        _stocksList.value = ApiResult.Error("No results found.")
                        Log.e("StocksViewModel", "Data is null for \"$query\"")
                    }
                } else {
                    _stocksList.value = ApiResult.Error("Error fetching stocks: ${response.message()}")
                    Log.e("StocksViewModel", "Error fetching stocks: ${response.message()}")
                }
            } catch (e: Exception) {
                _stocksList.value = ApiResult.Error("Exception: ${e.message}")
                Log.e("StocksViewModel", "Exception fetching stocks: ${e.message}")
            }
        }
    }



}