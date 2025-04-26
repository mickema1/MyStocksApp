package com.example.mystocksapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.data.Entity.SavedNewsEntity
import com.example.mystocksapp.items.NewsItem
import com.example.mystocksapp.viewModels.NewsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsViewModel = koinViewModel(),
) {
    val savedNews by viewModel.savedNews.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedNews()
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {

        Button(onClick = {
            viewModel.refreshNewsFromApiAndUpdateLocal()

        },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(56.dp)
                .width(300.dp)) {
            Text("Refresh")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (savedNews) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                val news = (savedNews as ApiResult.Success).data
                if (news.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No results found." + (savedNews as ApiResult.Success<List<SavedNewsEntity>>).data.size,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn {
                        items(news) { article ->
                            NewsItem(
                                news = article,
                                navController = navController,
                            )
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (savedNews as ApiResult.Error).message
                Text("Error: $errorMessage")
            }
        }
    }
}
