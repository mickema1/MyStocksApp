package com.example.mystocksapp.screens

import androidx.compose.runtime.Composable
import com.example.mystocksapp.viewModels.StocksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockDetailScreen(
    cryptoId: String,
    viewModel: StocksViewModel = koinViewModel(),
){

}