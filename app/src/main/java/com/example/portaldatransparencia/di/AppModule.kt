package com.example.portaldatransparencia.di

import com.example.portaldatransparencia.remote.*
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.ProgressBar
import com.example.portaldatransparencia.views.deputado.DeputadoViewModel
import com.example.portaldatransparencia.views.frente.FrenteViewModel
import com.example.portaldatransparencia.views.gastos.DespesasViewModel
import com.example.portaldatransparencia.views.main.MainViewModel
import com.example.portaldatransparencia.views.proposta.PropostaViewModel
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
        single<ApiServiceIdDeputado> {
                get<Retrofit>().create(ApiServiceIdDeputado::class.java)
        }
        single<ApiServiceIdDespesas> {
                get<Retrofit>().create(ApiServiceIdDespesas::class.java)
        }
        single<ApiServiceFrente> {
                get<Retrofit>().create(ApiServiceFrente::class.java)
        }
        single<ApiServiceProposta> {
                get<Retrofit>().create(ApiServiceProposta::class.java)
        }
}

val viewModelModule = module { viewModel { MainViewModel(get()) } }
val viewModelDeputado = module { viewModel { DeputadoViewModel(get()) } }
val viewModelDespesas = module { viewModel { DespesasViewModel(get()) } }
val viewModelFront = module { viewModel { FrenteViewModel(get()) } }
val viewModelProposta = module { viewModel { PropostaViewModel(get()) } }

val repositorySearch = module { single { SearchRepository(get()) } }
val repositoryIdDeputado = module { single { IdDeputadoRepository(get()) } }
val repositoryDespesasDeputado = module { single { IdDespesasRepository(get()) } }
val repositoryFront = module { single { FrenteRepository(get()) } }
val repositoryProposta = module { single { PropostaRepository(get()) } }

val progressModule = module { factory { ProgressBar() } }
val ageModule = module { factory { CalculateAge() } }
val securityPreferences = module { single { SecurityPreferences(get()) } }

val appModules = listOf( retrofitModule, viewModelModule, repositorySearch, progressModule,
        viewModelDeputado, repositoryIdDeputado, viewModelDespesas, repositoryDespesasDeputado,
        securityPreferences, viewModelFront, repositoryFront, viewModelProposta, repositoryProposta,
        ageModule
)