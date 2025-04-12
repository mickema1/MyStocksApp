package com.example.mystocksapp._const

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {

    object StocksList : BottomNavItem("Stocks", Icons.Filled.Home, Routes.StocksList)
    object FavouriteStocks : BottomNavItem("Favorite", Icons.Filled.FavoriteBorder, Routes.StocksList)


}