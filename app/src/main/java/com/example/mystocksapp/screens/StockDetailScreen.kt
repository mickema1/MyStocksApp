package com.example.mystocksapp.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.viewModels.StockDetailsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockDetailScreen(
    navController: NavController,
    ticker: String,
    viewModel: StockDetailsViewModel = koinViewModel(),
) {
    val stockDetailResult by viewModel.stockDetails.collectAsState() // Observe the stock details
    val scrollState = rememberScrollState() // To enable scrolling
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
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
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
                                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(color = MaterialTheme.colorScheme.onBackground)) {
                                        append(" See more")
                                    }
                                } else {
                                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(color = MaterialTheme.colorScheme.onBackground)) {
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
                                color = MaterialTheme.colorScheme.onBackground,
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


                    Row {
                        Button(onClick = {
                            val today = LocalDate.now(ZoneOffset.UTC)
                            val end = today.minusDays(7)
                            val fromDate = end.toString()
                            val toDate = today.toString()

                            viewModel.getStockGraph(
                                ticker = ticker,
                                from = fromDate,
                                to = toDate,
                                timespan = "hour"
                            )
                        }) {
                            Text("Week")
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(onClick = {
                            val today = LocalDate.now(ZoneOffset.UTC)
                            val thirtyDays = today.minusDays(30)
                            val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val fromDate = thirtyDays.format(fmt)
                            val toDate = today.format(fmt)

                            viewModel.getStockGraph(
                                ticker = ticker,
                                from   = fromDate,
                                to     = toDate,
                                timespan = "day"
                            )
                        }) {
                            Text("Month")
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Button(onClick = {
                            val today = LocalDate.now(ZoneOffset.UTC)
                            val yesterdayStart = today.minusDays(365)
                            val fromDate = yesterdayStart.toString()
                            val toDate = today.toString()

                            viewModel.getStockGraph(
                                ticker = ticker,
                                from = fromDate,
                                to = toDate,
                                timespan = "day"
                            )
                        }) {
                            Text("Year")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val graphState = viewModel.graphData.collectAsState()

                    if (graphState.value is ApiResult.Success && (graphState.value as ApiResult.Success).data.isNotEmpty()) {
                        StockGraphScreen(viewModel)
                        LatestStockData(viewModel)
                    }

                } else {
                    Text(text = "Stock not found", style = MaterialTheme.typography.bodyLarge)
                }
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to List")
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

@Composable
fun StockGraphScreen(viewModel: StockDetailsViewModel) {
    val graphDataResult by viewModel.graphData.collectAsState()

    when (graphDataResult) {
        is ApiResult.Loading -> {
            CircularProgressIndicator()
        }
        is ApiResult.Success -> {
            val entries = (graphDataResult as ApiResult.Success).data
            LineChartComposable(entries = entries)
        }
        is ApiResult.Error -> {
            Text("Error fetching graph data", color = Color.Red)
        }
    }
}

@Composable
fun LineChartComposable(entries: List<Entry>) {
    val context = LocalContext.current
    val chart = remember { LineChart(context) }

    val lineDataSet = LineDataSet(entries, "Stock Price")
    lineDataSet.color = MaterialTheme.colorScheme.onPrimary.toArgb()
    lineDataSet.valueTextColor = MaterialTheme.colorScheme.onSecondary.toArgb()
    lineDataSet.setDrawCircles(false)
    lineDataSet.setDrawValues(false)

    val lineData = LineData(lineDataSet)
    chart.xAxis.apply {
        textColor = MaterialTheme.colorScheme.onBackground.toArgb() // Numbers color
        axisLineColor = MaterialTheme.colorScheme.onBackground.toArgb() // Axis line color
        gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f).toArgb() // Grid lines color
        setDrawGridLines(true)
        setDrawAxisLine(true)
        setDrawLabels(false)
    }
    chart.axisLeft.apply {
        textColor = MaterialTheme.colorScheme.onBackground.toArgb()
        axisLineColor = MaterialTheme.colorScheme.onBackground.toArgb()
        gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f).toArgb()
        chart.legend.textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    }

    LaunchedEffect(entries) {
        chart.data = lineData
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.invalidate()
    }

    AndroidView(
        factory = { chart },
        modifier = Modifier.fillMaxWidth().height(300.dp)
    )
}
@Composable
fun LatestStockData(viewModel: StockDetailsViewModel) {
    val graphDataResult by viewModel.graphData.collectAsState()

    when (graphDataResult) {
        is ApiResult.Success -> {
            val entries = (graphDataResult as ApiResult.Success).data
            if (entries.isNotEmpty()) {
                val latestEntry = entries.last()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Latest Close Price: ${latestEntry.y}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is ApiResult.Error ->{
        Text("No recent values to show")
        }
        ApiResult.Loading ->{
        LinearProgressIndicator()}
    }
}