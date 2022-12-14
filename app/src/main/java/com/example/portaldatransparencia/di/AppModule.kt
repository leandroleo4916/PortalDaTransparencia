package com.example.portaldatransparencia.di

import com.example.portaldatransparencia.remote.*
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.*
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.deputado.DeputadoViewModel
import com.example.portaldatransparencia.views.deputado.frente_deputado.FrenteViewModel
import com.example.portaldatransparencia.views.deputado.gastos_deputado.DespesasViewModel
import com.example.portaldatransparencia.views.deputado.geral_deputado.OccupationViewModel
import com.example.portaldatransparencia.views.deputado.proposta_deputado.PropostaViewModel
import com.example.portaldatransparencia.views.activity.gastogeral.camara.GastoGeralViewModelCamara
import com.example.portaldatransparencia.views.activity.gastogeral.senado.GastoGeralViewModelSenado
import com.example.portaldatransparencia.views.activity.ranking.camara.RankingViewModelCamara
import com.example.portaldatransparencia.views.activity.ranking.senado.RankingViewModelSenado
import com.example.portaldatransparencia.views.activity.votacoes.VotacoesViewModelCamara
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.senador.SenadorViewModel
import com.example.portaldatransparencia.views.senador.geral_senador.GeralSenadorViewModel
import com.example.portaldatransparencia.views.senador.votacoes_senador.VotacoesViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.ModifyChip
import com.example.portaldatransparencia.views.view_generics.VisibilityNavViewAndFloating
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
        single<ApiServiceGastos> {
                get<Retrofit>().create(ApiServiceGastos::class.java)
        }
        single<ApiServiceSenadorCargos> {
                get<Retrofit>().create(ApiServiceSenadorCargos::class.java)
        }
        single<ApiServiceVotacoes> {
                get<Retrofit>().create(ApiServiceVotacoes::class.java)
        }
        single<ApiServiceVotacoesItem> {
                get<Retrofit>().create(ApiServiceVotacoesItem::class.java)
        }
        single<ApiServiceGastoGeralSenador> {
                get<Retrofit>().create(ApiServiceGastoGeralSenador::class.java)
        }
        single<ApiServiceGastoGeralDeputado> {
                get<Retrofit>().create(ApiServiceGastoGeralDeputado::class.java)
        }
        single<ApiServiceFrenteId> {
                get<Retrofit>().create(ApiServiceFrenteId::class.java)
        }
        single<ApiVotacoes> {
                get<Retrofit>().create(ApiVotacoes::class.java)
        }
        single<ApiServiceEvento> {
                get<Retrofit>().create(ApiServiceEvento::class.java)
        }
        single<ApiServicePropostaItem> {
                get<Retrofit>().create(ApiServicePropostaItem::class.java)
        }
}

val viewModelModule = module { viewModel { CamaraViewModel(get()) } }
val viewModelDeputado = module { viewModel { DeputadoViewModel(get()) } }
val viewModelDespesas = module { viewModel { DespesasViewModel(get(), get()) } }
val viewModelFront = module { viewModel { FrenteViewModel(get()) } }
val viewModelProposta = module { viewModel { PropostaViewModel(get()) } }
val viewModelOccupation = module { viewModel { OccupationViewModel(get()) } }
val viewModelSenado = module { viewModel { SenadoViewModel(get()) } }
val viewModelSenador = module { viewModel { SenadorViewModel(get()) } }
val viewModelSenadorGeral = module { viewModel { GeralSenadorViewModel(get()) } }
val viewModelVotacoes = module { viewModel { VotacoesViewModel(get(), get()) } }
val viewModelGastoGeral = module { viewModel { GastoGeralViewModelCamara(get()) } }
val viewModelRankingCamara = module { viewModel { RankingViewModelCamara(get()) } }
val viewModelRankingSenado = module { viewModel { RankingViewModelSenado(get()) } }
val viewModelGastoGeralSenado = module { viewModel { GastoGeralViewModelSenado(get()) } }
val viewModelVotacoesCamara = module { viewModel { VotacoesViewModelCamara() } }

val repositorySearch = module { single { SearchRepository(get()) } }
val repositoryIdDeputado = module { single { IdDeputadoRepository(get()) } }
val repositoryDespesasDeputado = module { single { IdDespesasRepository(get(), get()) } }
val repositoryFront = module { single { FrenteRepository(get(), get()) } }
val repositoryProposta = module { single { PropostaRepository(get()) } }
val repositoryOccupation = module { single { OccupationRepository(get()) } }
val repositorySenado = module { single { SenadoRepository(get()) } }
val repositorySenador = module { single { SenadorRepository(get()) } }
val repositorySenadorGeral = module { single { GeralSenadorRepository(get()) } }
val repositoryVotacoes = module { single { VotacoesRepository(get()) } }
val repositoryVotacoesItem = module { single { VotacoesRepositoryItem(get()) } }
val repositoryGastoGeral = module { single { GastoGeralRepository(get(), get()) } }
val repositoryVotacoesCamara = module { single { VotacoesCamaraRepository(get()) } }

val progressModule = module { factory { EnableDisableView() } }
val ageModule = module { factory { CalculateAge() } }
val visibilityNavFloating = module { factory { VisibilityNavViewAndFloating() } }
val modifyChip = module { factory { ModifyChip() } }
val retiraAcento = module { factory { RetiraAcento() } }
val securityPreferences = module { single { SecurityPreferences(get()) } }
val formatValor = module { factory { FormatValor() } }
val formatValorBi = module { factory { FormaterValueBilhoes(get()) } }
val formatValorFloat = module { factory { FormatValueFloat() } }
val validationInternet = module { single { ValidationInternet() } }

val appModules = listOf( retrofitModule, viewModelModule, repositorySearch, progressModule,
        viewModelDeputado, repositoryIdDeputado, viewModelDespesas, repositoryDespesasDeputado,
        securityPreferences, viewModelFront, repositoryFront, viewModelProposta, repositoryProposta,
        ageModule, viewModelOccupation, repositoryOccupation, repositorySenado, viewModelSenado,
        viewModelSenador, visibilityNavFloating, repositorySenador, modifyChip, viewModelSenadorGeral,
        repositorySenadorGeral, retiraAcento, repositoryVotacoes, viewModelVotacoes,
        repositoryVotacoesItem, formatValor, repositoryGastoGeral, viewModelGastoGeral,
        formatValorBi, formatValorFloat, validationInternet,
        viewModelRankingCamara, viewModelRankingSenado, viewModelGastoGeralSenado,
        viewModelVotacoesCamara, repositoryVotacoesCamara
)