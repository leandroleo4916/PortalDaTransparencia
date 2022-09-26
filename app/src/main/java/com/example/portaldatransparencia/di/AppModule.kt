package com.example.portaldatransparencia.di

import com.example.portaldatransparencia.remote.*
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.EnableDisableView
import com.example.portaldatransparencia.views.ModifyChip
import com.example.portaldatransparencia.views.VisibilityNavViewAndFloating
import com.example.portaldatransparencia.views.deputado.DeputadoViewModel
import com.example.portaldatransparencia.views.frente.FrenteViewModel
import com.example.portaldatransparencia.views.gastos.DespesasViewModel
import com.example.portaldatransparencia.views.geral.OccupationViewModel
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.proposta.PropostaViewModel
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.senador.SenadorViewModel
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
        single<ApiServiceOccupation> {
                get<Retrofit>().create(ApiServiceOccupation::class.java)
        }
        single<ApiServiceSenado> {
                get<Retrofit>().create(ApiServiceSenado::class.java)
        }
        single<ApiServiceSenador> {
                get<Retrofit>().create(ApiServiceSenador::class.java)
        }
}

val viewModelModule = module { viewModel { CamaraViewModel(get()) } }
val viewModelDeputado = module { viewModel { DeputadoViewModel(get()) } }
val viewModelDespesas = module { viewModel { DespesasViewModel(get()) } }
val viewModelFront = module { viewModel { FrenteViewModel(get()) } }
val viewModelProposta = module { viewModel { PropostaViewModel(get()) } }
val viewModelOccupation = module { viewModel { OccupationViewModel(get()) } }
val viewModelSenado = module { viewModel { SenadoViewModel(get()) } }
val viewModelSenador = module { viewModel { SenadorViewModel(get()) } }

val repositorySearch = module { single { SearchRepository(get()) } }
val repositoryIdDeputado = module { single { IdDeputadoRepository(get()) } }
val repositoryDespesasDeputado = module { single { IdDespesasRepository(get()) } }
val repositoryFront = module { single { FrenteRepository(get()) } }
val repositoryProposta = module { single { PropostaRepository(get()) } }
val repositoryOccupation = module { single { OccupationRepository(get()) } }
val repositorySenado = module { single { SenadoRepository(get()) } }
val repositorySenador = module { single { SenadorRepository(get()) } }

val progressModule = module { factory { EnableDisableView() } }
val ageModule = module { factory { CalculateAge() } }
val visibilityNavFloating = module { factory { VisibilityNavViewAndFloating() } }
val modifyChip = module { factory { ModifyChip() } }
val securityPreferences = module { single { SecurityPreferences(get()) } }

val appModules = listOf( retrofitModule, viewModelModule, repositorySearch, progressModule,
        viewModelDeputado, repositoryIdDeputado, viewModelDespesas, repositoryDespesasDeputado,
        securityPreferences, viewModelFront, repositoryFront, viewModelProposta, repositoryProposta,
        ageModule, viewModelOccupation, repositoryOccupation, repositorySenado, viewModelSenado,
        viewModelSenador, visibilityNavFloating, repositorySenador, modifyChip
)