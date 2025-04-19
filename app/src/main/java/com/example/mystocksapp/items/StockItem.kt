package com.example.mystocksapp.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp.R
import com.example.mystocksapp._const.Routes
import com.example.mystocksapp.data.Stocks
import com.example.mystocksapp.viewModels.SavedTickerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockItem(
    stocks: Stocks,
    navController: NavController,
    isSaved: Boolean = false,
    viewModel: SavedTickerViewModel = koinViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate(Routes.stockDetail(ticker = stocks.ticker))
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = stocks.name, fontWeight = FontWeight.Bold)
            Text(text = "Locale: ${stocks.locale}")
            Text(text = "Currency: ${stocks.currencyName}")
            Text(text = "Ticker: ${stocks.ticker}")
        }

        IconButton(onClick = {
            if (!isSaved) {
                viewModel.saveTicker(stocks)
            } else {
                viewModel.removeSavedTicker(stocks.ticker)
            }
        }) {
            if (!isSaved) {
                Icon(Icons.Filled.Add, contentDescription = "Add to favourite")
            }else{
                Icon(Icons.Filled.Clear, contentDescription = "Remove from favourites")
            }
        }
    }
}

