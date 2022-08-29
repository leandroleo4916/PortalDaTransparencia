package com.example.portaldatransparencia.di

import com.example.portaldatransparencia.remote.ApiServiceMain
import com.example.portaldatransparencia.remote.SearchRepository
import com.example.portaldatransparencia.views.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
        single<Retrofit> {
                Retrofit.Builder()
                        .baseUrl("https://dadosabertos.camara.leg.br")
                        .client(get())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
        }
        single {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build()
        }
        single<ApiServiceMain> {
                get<Retrofit>().create(ApiServiceMain::class.java)
        }
}

val viewModelModule = module { viewModel { MainViewModel(get()) } }
val playVoice = module { factory {  } }
val dataBase = module { single { SearchRepository(get()) } }

val appModules = listOf( retrofitModule, viewModelModule, playVoice, dataBase)