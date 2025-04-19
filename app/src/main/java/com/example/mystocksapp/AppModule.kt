package com.example.mystocksapp




import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.mystocksapp.Api.StocksApi
import com.example.mystocksapp.data.MyObjectBox
import com.example.mystocksapp.data.SavedTickerEntity
import com.example.mystocksapp.repository.SavedTickersRepository
import com.example.mystocksapp.viewModels.SavedTickerViewModel
import com.example.mystocksapp.viewModels.StockDetailsViewModel
import com.example.mystocksapp.viewModels.StocksViewModel
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val repositoryModule = module {
    single { SavedTickersRepository(get()) }
}

val viewModelModule = module {
    viewModel { StocksViewModel(get()) }
    viewModel { SavedTickerViewModel(get(),get()) }
    viewModel { StockDetailsViewModel(get())}
}

val imageModule = module {
    single { provideImageLoader(androidContext()) }
}

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideStocksApi(get()) }
}

val objectBoxModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .build()
    }
    single { get<BoxStore>().boxFor(SavedTickerEntity::class.java) }
}

val helperModule = module {
    //single { NotificationHelper(androidContext()) }
    //single { NotificationSchedulerHelper(androidContext()) } //úkol č. 3
}

fun provideImageLoader(androidContext: Context): ImageLoader {
    return ImageLoader.Builder(androidContext)
        .memoryCache {
            MemoryCache.Builder(androidContext)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(androidContext.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    //Get ApiKey from properties
    //val polygonApiKey: String = System.getProperty("polygonApiKey") ?: throw IllegalStateException("API key is missing")

    val polygonApiKey: String = "Dqp4SuuiUB_5ynx3TB29v4tnpzrUrThp" //todo read from properties

    val apiKeyInterceptor = Interceptor { chain -> val originalRequest = chain.request()

        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("apiKey", polygonApiKey)
            .build()


        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }


    val clientWithInterceptor = okHttpClient.newBuilder()
        .addInterceptor(apiKeyInterceptor) // Adding the interceptor
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.polygon.io/")
        .client(clientWithInterceptor)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideStocksApi(retrofit: Retrofit): StocksApi {
    return retrofit.create(StocksApi::class.java)
}