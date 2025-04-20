package com.example.mystocksapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.Dao.StockDetails
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockDetailsViewModel(private val stocksApi: StocksApi) : ViewModel() {

    private val _stockDetails = MutableStateFlow<ApiResult<StockDetails>>(ApiResult.Loading)
    val stockDetails: StateFlow<ApiResult<StockDetails>> = _stockDetails.asStateFlow()

    private val _graphData = MutableStateFlow<ApiResult<List<Entry>>>(ApiResult.Loading)
    val graphData: StateFlow<ApiResult<List<Entry>>> = _graphData.asStateFlow()

    fun getStockDetails(ticker: String) {
        viewModelScope.launch {
            _stockDetails.value = ApiResult.Loading
            try {
                val response = stocksApi.getStockDetails(ticker)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Log.d("StockDetailsViewModel", "Response body: $data")

                        val stockDetails = data.data
                        if (stockDetails != null) {
                            _stockDetails.value = ApiResult.Success(stockDetails)
                            Log.d("StockDetailsViewModel", "Stock details fetched successfully for ticker: $ticker")
                        } else {
                            _stockDetails.value = ApiResult.Error("Results are null for ticker: $ticker")
                            Log.e("StockDetailsViewModel", "Results are null for ticker: $ticker")
                        }
                    } else {
                        _stockDetails.value = ApiResult.Error("No data found for ticker: $ticker")
                        Log.e("StockDetailsViewModel", "No data found for ticker: $ticker")
                    }
                } else {
                    _stockDetails.value = ApiResult.Error("Error fetching stock details: ${response.message()}")
                    Log.e("StockDetailsViewModel", "Error fetching stock details: ${response.message()}")
                }
            } catch (e: Exception) {
                _stockDetails.value = ApiResult.Error("Exception: ${e.message}")
                Log.e("StockDetailsViewModel", "Exception fetching stock details: ${e.message}")
            }
        }
    }

    fun getStockGraph(ticker: String, from: String, to: String, timespan: String) {
        viewModelScope.launch {
            _graphData.value = ApiResult.Loading
            try {
                val response = stocksApi.getAggregateGraph(ticker, multiplier = 1, timespan = timespan, from = from, to = to)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        val entries = data.results?.map { bar ->
                            Entry(bar.timestamp.toFloat(), bar.close.toFloat())
                        }
                        _graphData.value = ApiResult.Success(entries ?: emptyList())
                        Log.d("StockDetailsViewModel", "Graph data fetched successfully for ticker: $ticker")
                    } else {
                        _graphData.value = ApiResult.Error("No graph data found for ticker: $ticker")
                        Log.e("StockDetailsViewModel", "No graph data found for ticker: $ticker")
                    }
                } else {
                    _graphData.value = ApiResult.Error("Error fetching graph data: ${response.message()}")
                    Log.e("StockDetailsViewModel", "Error fetching graph data: ${response.message()}")
                }
            } catch (e: Exception) {
                _graphData.value = ApiResult.Error("Exception: ${e.message}")
                Log.e("StockDetailsViewModel", "Exception fetching graph data: ${e.message}")
            }
        }
    }

}