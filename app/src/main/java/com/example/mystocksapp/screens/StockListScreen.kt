package com.example.mystocksapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.mystocksapp.items.StockItem
import com.example.mystocksapp.viewModels.StocksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockListScreen(
    navController: NavController,
    viewModel: StocksViewModel = koinViewModel(),
) {
    val listState = rememberLazyListState()
    val stockList by viewModel.stocksList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getStockList() // Pass your actual API key here
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        when (stockList) {
            is ApiResult.Loading -> {
                // Show loading indicator
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                // Display stock data
                val stockList = (stockList as ApiResult.Success).data
                LazyColumn(state = listState) {
                    items(stockList) { stock ->
                        StockItem(
                            stocks = stock,
                            navController = navController
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                // Display error message
                val errorMessage = (stockList as ApiResult.Error).message
                Text(text = "Error: $errorMessage")
            }
        }
    }
}
