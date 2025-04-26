package com.example.mystocksapp.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp._const.Routes
import com.example.mystocksapp.data.Entity.SavedTickerEntity

@Composable
fun SavedTickerItem(ticker: SavedTickerEntity,
                    onRemoveClick: () -> Unit,
                    navController: NavController,
                    savedStock : SavedTickerEntity
                    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Routes.stockDetail(ticker = savedStock.ticker)) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = ticker.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
            Text(text = ticker.ticker, style = MaterialTheme.typography.bodyMedium)
        }

        IconButton(onClick = { onRemoveClick() }) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove stock")
        }
    }
}
