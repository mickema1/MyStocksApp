package com.example.mystocksapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mystocksapp.ui.theme.MyStocksAppTheme
import org.koin.core.context.startKoin
import androidx.navigation.compose.rememberNavController
import com.example.mystocksapp._const.BottomNavItem
import com.example.mystocksapp._const.Routes
import com.example.mystocksapp.screens.NewsDetailScreen
import com.example.mystocksapp.screens.NewsListScreen
import com.example.mystocksapp.screens.SavedTickerScreen
import com.example.mystocksapp.screens.StockDetailScreen
import com.example.mystocksapp.screens.StockListScreen

import org.koin.android.ext.koin.androidContext


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*startKoin {
            androidContext(this@MainActivity)
            modules(
                repositoryModule,
                viewModelModule,
                networkModule,
                objectBoxModule,
                imageModule)
        }*/
        setContent {
            MyStocksAppTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    //proměnné jejichž hodnotu potřebujeme zachovat i po rekompozici (překreslení) musíme ukládat pomocí remember a mutableStateOf, případně funkcí podobných
    var selectedItem by remember { mutableStateOf(0) }

    //náš list itemů použitých v bottom navigation bar
    val items = listOf(
        BottomNavItem.StocksList,
        BottomNavItem.SavedStocks,
        BottomNavItem.News
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stocks App",
                    fontWeight = FontWeight.Bold
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),


            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            if (navController.currentDestination?.route != item.screenRoute) {
                                navController.navigate(item.screenRoute) {
                                    popUpTo(Routes.StocksList) {
                                    saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.onTertiary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.StocksList,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.StocksList) { StockListScreen(navController) }

        composable(Routes.SavedStocks) { SavedTickerScreen(navController) }

        composable(Routes.StocksDetail) { navBackStackEntry ->
            val ticker = navBackStackEntry.arguments?.getString("ticker")
            if (ticker!= null) {
                StockDetailScreen(
                    ticker = ticker,
                    navController = navController,
                )
            }
        }

        composable(Routes.NewsList) { NewsListScreen(navController)}

        composable(Routes.NewsDetail) { navBackStackEntry ->
            val _newsId = navBackStackEntry.arguments?.getString("newsId")
            if (_newsId!= null) {
                NewsDetailScreen(
                    newsId = _newsId,
                    navController = navController,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyStocksAppTheme {
        MainScreen(rememberNavController())
    }
}