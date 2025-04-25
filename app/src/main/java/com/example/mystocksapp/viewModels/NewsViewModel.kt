package com.example.mystocksapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.Dao.StockNews
import com.example.mystocksapp.data.Entity.SavedNewsEntity
import com.example.mystocksapp.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val stocksApi: StocksApi,
    private val newsRepo : NewsRepository) : ViewModel() {

    private val _newsList = MutableStateFlow<ApiResult<List<StockNews>>>(ApiResult.Loading)
    val newsList:StateFlow<ApiResult<List<StockNews>>> = _newsList.asStateFlow()

    private val _savedNews = MutableStateFlow<ApiResult<List<SavedNewsEntity>>>(ApiResult.Loading)
    val savedNews: StateFlow<ApiResult<List<SavedNewsEntity>>> = _savedNews.asStateFlow()
    //todo perhaps search?... maybe? idk...

    fun getNews(latestSavedUtc: String? = null){
        viewModelScope.launch {
            _newsList.value = ApiResult.Loading
            try {
                val response = stocksApi.getStockNews(publishedUtcGt = latestSavedUtc)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.data != null) {
                        val stockList = body.data
                        _newsList.value = ApiResult.Success(stockList)
                        Log.d("NewsViewModel", "Found ${stockList.size} results")
                    } else {
                        _newsList.value = ApiResult.Error("No results found.")
                        Log.e("NewsViewModel", "Data is null")
                    }
                } else {
                    _newsList.value = ApiResult.Error("Error fetching news: ${response.message()}")
                    Log.e("NewsViewModel", "Error fetching news: ${response.message()}")

                }
            }catch (e : Exception)
            {
                _newsList.value = ApiResult.Error("Exception: ${e.message}")
                Log.e("NewsViewModel", "Exception fetching news: ${e.message}")
            }
        }
    }


    fun loadSavedNews(){
    viewModelScope.launch {
        try {
            val saved = newsRepo.getAllByDate()
            _savedNews.value = saved
        } catch (e: Exception) {
            _savedNews.value = ApiResult.Error("Exception fetching saved news: ${e.message}")
        }
    }
}

    fun refreshNewsFromApiAndUpdateLocal() {
        viewModelScope.launch {
            //newsRepo.clearAllNews()
            val savedList = (newsRepo.getAllByDate() as? ApiResult.Success)?.data.orEmpty()
            val latestUtc = savedList.maxByOrNull { it.publishedUtc }?.publishedUtc
                val resp = stocksApi.getStockNews(publishedUtcGt = latestUtc)

            if (resp.isSuccessful) {
                val incoming = resp.body()?.data.orEmpty()
                val toSave = incoming.map { dto ->
                    SavedNewsEntity(
                        apiId        = dto.apiId,
                        name         = dto.name,
                        homepageUrl  = dto.homepageUrl,
                        logo         = dto.logo,
                        favicon      = dto.favicon,
                        title        = dto.title,
                        author       = dto.author,
                        publishedUtc = dto.publishedUtc,
                        articleUrl   = dto.articleUrl,
                        image        = dto.image,
                        description  = dto.description
                    )
                }
                newsRepo.saveNews(toSave)
            }
            loadSavedNews()
        }
    }
    }