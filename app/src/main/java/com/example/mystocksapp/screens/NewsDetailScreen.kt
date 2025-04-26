package com.example.mystocksapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.viewModels.NewsDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsDetailScreen(
    navController: NavController,
    newsId:String,
    viewModel: NewsDetailsViewModel = koinViewModel(),
) {

    val newsDetailResult by viewModel.newsDetail.collectAsState() // Observe the stock details
    val scrollState = rememberScrollState() // To enable scrolling
    LaunchedEffect(newsId) {
        viewModel.getNewsDetail(newsId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        when (newsDetailResult) {
            is ApiResult.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
            is ApiResult.Success -> {
                val article = (newsDetailResult as ApiResult.Success).data
                Text(
                    text = article.title ?: "—",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // … you can add description, author, etc. below …
            }
            is ApiResult.Error -> {
                Text(
                    text = (newsDetailResult as ApiResult.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}