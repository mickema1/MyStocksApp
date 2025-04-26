package com.example.mystocksapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.data.Entity.SavedNewsEntity
import com.example.mystocksapp.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsDetailsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsDetail = MutableStateFlow<ApiResult<SavedNewsEntity>>(ApiResult.Loading)
    val newsDetail: StateFlow<ApiResult<SavedNewsEntity>> = _newsDetail.asStateFlow()

    fun getNewsDetail(id: String) {
        viewModelScope.launch {
            _newsDetail.value = ApiResult.Loading
            try {
                val article = newsRepository.getById(id)
                if (article != null) {
                    _newsDetail.value = ApiResult.Success(article)
                } else {
                    _newsDetail.value = ApiResult.Error("Article not found")
                }
            } catch (e: Exception) {
                _newsDetail.value = ApiResult.Error("Exception: ${e.message}")
            }
        }
    }
}