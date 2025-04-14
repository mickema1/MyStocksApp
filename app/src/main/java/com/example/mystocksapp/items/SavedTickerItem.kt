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
import androidx.compose.ui.unit.dp
import com.example.mystocksapp.data.SavedTickerEntity

@Composable
fun SavedTickerItem(ticker: SavedTickerEntity, onClick: () -> Unit, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = ticker.name, style = MaterialTheme.typography.titleMedium)
            Text(text = ticker.ticker, style = MaterialTheme.typography.bodyMedium)
        }

        IconButton(onClick = { onRemoveClick() }) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove stock")
        }
    }
}
