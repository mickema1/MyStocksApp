package com.example.mystocksapp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.viewModels.StockDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockDetailScreen(
    navController: NavController,
    ticker: String,
    viewModel: StockDetailsViewModel = koinViewModel(),
) {
    val stockDetailResult by viewModel.stockDetails.collectAsState() // Observe the stock details
    val scrollState = rememberScrollState() // To enable scrolling
    val context = LocalContext.current

    LaunchedEffect(ticker) {
        viewModel.getStockDetails(ticker)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (stockDetailResult) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                val stock = (stockDetailResult as ApiResult.Success).data
                if (stock != null) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stock.name ?: "Unknown"} (${stock.ticker ?: ticker})",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    var expanded by remember { mutableStateOf(false) }

                    DetailRow(title = "Description") {
                        val descriptionText = stock.description ?: "Not provided"

                        val displayText = if (expanded) {
                            descriptionText
                        } else {
                            "${descriptionText.take(100)}..."
                        }

                        Text(
                            text = buildAnnotatedString {
                                append(displayText)
                                if (!expanded) {
                                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(color = MaterialTheme.colorScheme.primary)) {
                                        append(" See more")
                                    }
                                } else {
                                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(color = MaterialTheme.colorScheme.primary)) {
                                        append(" See less")
                                    }
                                }
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.clickable { expanded = !expanded } // Make the entire text clickable
                        )
                    }

                    stock.marketCap?.let {
                        DetailRow(title = "Market Cap") {
                            Text(
                                text = "${valueFormatter(stock.marketCap)} ${stock.currencyName?.uppercase() ?: ""}",
                            )
                        }
                    }

                    stock.homepageUrl?.let { url -> //clickable -> redirects to the url
                        val context = LocalContext.current
                        DetailRow(title = "Homepage") {
                            Text(
                                text = url,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }

                    DetailRow(title = "Listed Since") {
                        Text(text = stock.listDate ?: "-")
                    }

                    DetailRow(title = "Exchange & Type") {
                        Text(text = "${stock.primaryExchange ?: "-"} • ${stock.type ?: "-"}")
                    }

                    DetailRow(title = "Employees") {
                        Text(text = "${stock.totalEmployees ?: "-"}")
                    }

                    if (!stock.city.isNullOrBlank() || !stock.state.isNullOrBlank()) {
                        DetailRow(title = "Address") {
                            Text(text = "${stock.city ?: ""}, ${stock.state ?: ""}")
                        }
                    }

                } else {
                    Text(text = "Stock not found", style = MaterialTheme.typography.bodyLarge)
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (stockDetailResult as ApiResult.Error).message
                Text(text = "Error: $errorMessage", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
            }
        }
    }
}


@Composable
fun DetailRow(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = title, fontWeight = FontWeight.Bold)
        content()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun valueFormatter(value: Double?):String{
    if (value == null) {
        return "-"
    }

    if (value >= 1_000_000_000_000) {
        val result = value / 1_000_000_000_000
        return String.format("%.2fT", result) // T = Trillion
    } else if (value >= 1_000_000_000) {
        val result = value / 1_000_000_000
        return String.format("%.2fB", result) // B = Billion
    } else if (value >= 1_000_000) {
        val result = value / 1_000_000
        return String.format("%.2fM", result) // M = Million
    } else {
        return String.format("%.2f", value) // Just a regular number
    }
}
