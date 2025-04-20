package com.example.mystocksapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.data.Entity.SavedTickerEntity
import com.example.mystocksapp.items.SavedTickerItem
import com.example.mystocksapp.viewModels.SavedTickerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SavedTickerScreen(navController: NavController, viewModel: SavedTickerViewModel = koinViewModel()) {
    val savedListResult by viewModel.savedTickers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedTickers()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        when (savedListResult) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }
            is ApiResult.Success -> {
                val savedList = (savedListResult as ApiResult.Success<List<SavedTickerEntity>>).data
                if (savedList.isEmpty()) {
                    Text(text = "No saved stocks yet.")
                } else {
                    LazyColumn {
                        items(savedList) { ticker ->
                            SavedTickerItem(
                                ticker = ticker,
                                onRemoveClick = {
                                    viewModel.removeSavedTicker(ticker.ticker)
                                },
                                navController = navController,
                                savedStock = ticker
                            )
                        }
                    }
                }
            }
            is ApiResult.Error -> {
                Text(text = "Error: ${(savedListResult as ApiResult.Error).message}")
            }
        }
    }
}