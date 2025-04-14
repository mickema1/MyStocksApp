package com.example.mystocksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mystocksapp.ui.theme.MyStocksAppTheme
import org.koin.core.context.startKoin
import androidx.navigation.compose.rememberNavController
import com.example.mystocksapp._const.BottomNavItem
import com.example.mystocksapp._const.Routes
import com.example.mystocksapp.screens.SavedTickerScreen
import com.example.mystocksapp.screens.StockDetailScreen
import com.example.mystocksapp.screens.StockListScreen

import org.koin.android.ext.koin.androidContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(repositoryModule,
                viewModelModule,
                repositoryModule,
                viewModelModule,
                networkModule,
                objectBoxModule,
                imageModule,
                helperModule)
        }
        setContent {
            MyStocksAppTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    //proměnné jejichž hodnotu potřebujeme zachovat i po rekompozici (překreslení) musíme ukládat pomocí remember a mutableStateOf, případně funkcí podobných
    var selectedItem by remember { mutableStateOf(0) }

    //náš list itemů použitých v bottom navigation bar
    val items = listOf(
        BottomNavItem.StocksList,
        BottomNavItem.SavedStocks
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
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
                            navController.navigate(item.screenRoute) {
                                navController.graph.startDestinationRoute?.let { screenRoute ->
                                    popUpTo(screenRoute) {  //odebrání ostatních obrazovek z back stacku
                                        saveState =
                                            true//uložení stavu obrazovek odstraněných z back stacku
                                    }
                                }
                                launchSingleTop =
                                    true //zabraňuje vytváření nových instancí stejné obrazovky
                                restoreState =
                                    true //obnovení stavu, pokud byl uložen pomocí saveState
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.LightGray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.StocksList,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.StocksList) { StockListScreen(navController) }
        composable(Routes.StocksDetail) { navBackStackEntry ->
            val cryptoId = navBackStackEntry.arguments?.getString("cryptoId")
            if (cryptoId != null) {
                StockDetailScreen(cryptoId)
            }
        }
        composable(Routes.SavedStocks) { SavedTickerScreen(navController) }
        //composable(Routes.Settings) { SettingsScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyStocksAppTheme {
        MainScreen(rememberNavController())
    }
}