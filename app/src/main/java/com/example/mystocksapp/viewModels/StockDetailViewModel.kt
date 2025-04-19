package com.example.mystocksapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.StockDetails
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockDetailsViewModel(private val stocksApi: StocksApi) : ViewModel() {

    private val _stockDetails = MutableStateFlow<ApiResult<StockDetails>>(ApiResult.Loading)
    val stockDetails: StateFlow<ApiResult<StockDetails>> = _stockDetails.asStateFlow()

    fun getStockDetails(ticker: String) {
        viewModelScope.launch {
            _stockDetails.value = ApiResult.Loading
            try {
                val response = stocksApi.getStockDetails(ticker)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        // Log the entire response body to inspect its structure
                        Log.d("StockDetailsViewModel", "Response body: $data")

                        // Check if 'data' contains the expected 'results' or other relevant field
                        val stockDetails = data.data  // Adjust this to match the actual field
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

}