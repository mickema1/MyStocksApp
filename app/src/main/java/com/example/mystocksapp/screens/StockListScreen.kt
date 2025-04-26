package com.example.mystocksapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.items.StockItem
import com.example.mystocksapp.viewModels.SavedTickerViewModel
import com.example.mystocksapp.viewModels.StocksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockListScreen(
    navController: NavController,
    viewModel: StocksViewModel = koinViewModel(),
    savedViewModel: SavedTickerViewModel = koinViewModel()
) {
    val stockList by viewModel.stocksList.collectAsState()
    var currentQuery by remember { mutableStateOf("") }
    val saved by savedViewModel.savedTickers.collectAsState()

    LaunchedEffect(Unit) {
        savedViewModel.loadSavedTickers()
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {
        TextField(
            value = currentQuery,
            onValueChange = { currentQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Search for stocks by name...") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary
            )
        )

        Button(
            onClick = {
                viewModel.updateSearchQuery(currentQuery)
                viewModel.getStockList(currentQuery)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (stockList) {
            is ApiResult.Loading -> {
                if (currentQuery.isBlank()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Nothing to show yet, try searching for tickers!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else{
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            is ApiResult.Success -> {
                val stocks = (stockList as ApiResult.Success).data
                if (stocks.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No results found for '$currentQuery'.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn {
                        items(stocks) { stock ->
                            val isSaved = if (saved is ApiResult.Success) {
                                (saved as ApiResult.Success).data.any { it.ticker == stock.ticker }
                            } else {
                                false
                            }
                            StockItem(
                                stocks = stock,
                                navController = navController,
                                isSaved = isSaved
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (stockList as ApiResult.Error).message
                Text("Error: $errorMessage")
            }
        }
    }
}
