package com.example.mystocksapp._const

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    object StocksList : BottomNavItem("Stocks", Icons.Filled.Home, Routes.StocksList)
    object SavedStocks : BottomNavItem("Saved", Icons.Filled.Favorite, Routes.SavedStocks)
    object News : BottomNavItem("News", Icons.Filled.Notifications, Routes.NewsList)
}