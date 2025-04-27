package com.example.mystocksapp.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp._const.Routes
import com.example.mystocksapp.data.Entity.SavedNewsEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun NewsItem(news: SavedNewsEntity, navController: NavController, ) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            navController.navigate(Routes.newsDetail(newsId=news.id))
        }
    ) {
        Text(text = news.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            text = formatPublishedUtc(news.publishedUtc),
            style = MaterialTheme.typography.bodySmall
        )
}
}

fun formatPublishedUtc(publishedUtc: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(publishedUtc)

        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        formatter.format(date ?: Date())
    } catch (e: Exception) {
        "Invalid date"
    }
}