package com.example.mystocksapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.mystocksapp.Api.ApiResult
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.SavedTickerEntity
import com.example.mystocksapp.data.Stocks
import com.example.mystocksapp.repository.SavedTickersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SavedTickerViewModel(
    private val savedTickersRepository: SavedTickersRepository,
    private val stocksApi: StocksApi
)   :ViewModel() {

    /*val favouriteCryptosUpdated = flow { //flow se postará o update UI
        while (true) {
            emit(Unit) //emitnutí nové hodnoty do Flow
            delay(TimeUnit.SECONDS.toMillis(15)) //počkat 15 sekund
        }
    }.map { //zde se postaráme o získání hodnot (logika z loadFavouriteCryptos())
        try{
            val favouriteEntities: List<SavedTickerEntity> = savedTickersRepository.getAllSavedTickers()
            if (favouriteEntities.isEmpty()) {
                ApiResult.Success<List<Stocks>>(emptyList())
            } else {
                val cryptoIds = favouriteEntities.map { it.ticker }.joinToString(",")

                val result = stocksApi.getStocksListByIds(cryptoIds)

                if(result.isSuccessful){
                    ApiResult.Success(result.body()?.data ?: emptyList())
                }else{
                    ApiResult.Error(result.message())
                }
            }
        }catch (e: Exception){
            ApiResult.Error("Exception fetching favourite cryptos: ${e.message}")
        }
    }.stateIn(
        scope = viewModelScope, //nastavení couroutine scope ve které se tento kod spouští
        started = SharingStarted.WhileSubscribed(5_000), //nastavení parametrů flowu, začne okamžitě po přihlášení a prvního subscriberu a přestene 5 vteřin po odhlášení posledního, tzn. nebude se vykonávat pokud nikdo nekolektuje stav
        initialValue = ApiResult.Loading
    )*/


    private val _savedTickers = MutableStateFlow<ApiResult<List<SavedTickerEntity>>>(ApiResult.Loading)
    val savedTickers: StateFlow<ApiResult<List<SavedTickerEntity>>> = _savedTickers.asStateFlow()
    init {
        loadSavedTickers()
    }

    fun saveTicker(stock : Stocks) {
        viewModelScope.launch {
            savedTickersRepository.saveTicker(stock)
            loadSavedTickers()
        }
    }

    fun removeSavedTicker(ticker: String) {
        viewModelScope.launch {
            savedTickersRepository.removeSavedTicker(ticker)
            loadSavedTickers()
        }
    }

    fun loadSavedTickers() {
        viewModelScope.launch {
            try {
                val favourites = savedTickersRepository.getAllSavedTickers()
                _savedTickers.value = ApiResult.Success(favourites)
            } catch (e: Exception) {
                _savedTickers.value = ApiResult.Error("Exception fetching saved tickers: ${e.message}")
            }
        }
    }
}