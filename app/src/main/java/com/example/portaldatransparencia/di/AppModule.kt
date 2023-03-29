package com.example.portaldatransparencia.di

import com.example.portaldatransparencia.adapter.GastoSenadorAdapter
import com.example.portaldatransparencia.network.*
import com.example.portaldatransparencia.repository.*
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.*
import com.example.portaldatransparencia.views.activity.gastogeral.camara.GastoGeralViewModelCamara
import com.example.portaldatransparencia.views.activity.gastogeral.senado.GastoGeralViewModelSenado
import com.example.portaldatransparencia.views.activity.ranking.camara.RankingViewModelCamara
import com.example.portaldatransparencia.views.activity.ranking.senado.RankingViewModelSenado
import com.example.portaldatransparencia.views.activity.votacoes.camara.VotacoesViewModelCamara
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.camara.deputado.DeputadoViewModel
import com.example.portaldatransparencia.views.camara.deputado.frente_deputado.FrontIdViewModel
import com.example.portaldatransparencia.views.camara.deputado.frente_deputado.FrontViewModel
import com.example.portaldatransparencia.views.camara.deputado.gastos_deputado.DespesasViewModel
import com.example.portaldatransparencia.views.camara.deputado.geral_deputado.OccupationViewModel
import com.example.portaldatransparencia.views.camara.deputado.proposta_deputado.PropostaViewModel
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.senado.senador.SenadorViewModel
import com.example.portaldatransparencia.views.senado.senador.gastos_senador.CotasSenadorViewModel
import com.example.portaldatransparencia.views.senado.senador.geral_senador.GeralSenadorViewModel
import com.example.portaldatransparencia.views.senado.senador.votacoes_senador.VotacoesViewModel
import com.example.portaldatransparencia.views.view_generics.*
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
        single<ApiServiceMain> { get<Retrofit>().create(ApiServiceMain::class.java) }
        single<ApiServiceIdDeputado> { get<Retrofit>().create(ApiServiceIdDeputado::class.java) }
        single<ApiServiceIdDespesas> { get<Retrofit>().create(ApiServiceIdDespesas::class.java) }
        single<ApiServiceFrente> { get<Retrofit>().create(ApiServiceFrente::class.java) }
        single<ApiServiceProposta> { get<Retrofit>().create(ApiServiceProposta::class.java) }
        single<ApiServiceOccupation> { get<Retrofit>().create(ApiServiceOccupation::class.java) }
        single<ApiServiceSenado> { get<Retrofit>().create(ApiServiceSenado::class.java) }
        single<ApiServiceSenador> { get<Retrofit>().create(ApiServiceSenador::class.java) }
        single<ApiServiceGastos> { get<Retrofit>().create(ApiServiceGastos::class.java) }
        single<ApiServiceSenadorCargos> { get<Retrofit>().create(ApiServiceSenadorCargos::class.java) }
        single<ApiServiceVotacoes> { get<Retrofit>().create(ApiServiceVotacoes::class.java) }
        single<ApiServiceVotacoesItem> { get<Retrofit>().create(ApiServiceVotacoesItem::class.java) }
        single<ApiServiceRankingSenador> { get<Retrofit>().create(ApiServiceRankingSenador::class.java) }
        single<ApiServiceGastoGeralDeputado> { get<Retrofit>().create(ApiServiceGastoGeralDeputado::class.java) }
        single<ApiServiceFrenteId> { get<Retrofit>().create(ApiServiceFrenteId::class.java) }
        single<ApiVotacoes> { get<Retrofit>().create(ApiVotacoes::class.java) }
        single<ApiServiceEvento> { get<Retrofit>().create(ApiServiceEvento::class.java) }
        single<ApiServicePropostaItem> { get<Retrofit>().create(ApiServicePropostaItem::class.java) }
        single<ApiVotacoesSenado> { get<Retrofit>().create(ApiVotacoesSenado::class.java) }
        single<ApiServiceRankingDeputado> { get<Retrofit>().create(ApiServiceRankingDeputado::class.java) }
        single<ApiServiceGastoGeralSenado> { get<Retrofit>().create(ApiServiceGastoGeralSenado::class.java) }
        single<ApiServiceVotos> { get<Retrofit>().create(ApiServiceVotos::class.java) }
}

val progressModule = module { factory { EnableDisableView() } }
val ageModule = module { factory { CalculateAge() } }
val visibilityNavFloating = module { factory { VisibilityNavViewAndFloating() } }
val modifyChip = module { factory { ModifyChip() } }
val retiraAcento = module { factory { RetiraAcento() } }
val securityPreferences = module { single { SecurityPreferences(get()) } }
val formatValorBi = module { factory { FormaterValueBilhoes() } }
val formatValorFloat = module { factory { FormatValueFloat() } }
val converterValueNotes = module { factory { ConverterValueNotes() } }
val validationInternet = module { single { ValidationInternet() } }
val modifyHttp = module { single { ModifyHttpToHttps() } }
val retValueInt = module { single { RetValueFloatOrInt() } }

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
val repositoryGastoGeral = module { single { GastoGeralRepository(get(), get(), get(), get()) } }
val repositoryVotacoesCamara = module { single { VotacoesCamaraRepository(get()) } }

val animationView = module { single { AnimationView() }}
val dialogFragment = module { single { BottomDialogFragment() }}
val dialogPhoto = module { single { CreateDialogClass() }}

val viewModelModule = module { viewModel { CamaraViewModel(get()) } }
val viewModelDeputado = module { viewModel { DeputadoViewModel(get()) } }
val viewModelDespesas = module { viewModel { DespesasViewModel(get(), get()) } }
val viewModelFrontId = module { viewModel { FrontIdViewModel(get()) } }
val viewModelFront = module { viewModel { FrontViewModel(get()) } }
val viewModelProposta = module { viewModel { PropostaViewModel(get(), get()) } }
val viewModelOccupation = module { viewModel { OccupationViewModel(get()) } }
val viewModelCotasSenador = module { viewModel { CotasSenadorViewModel(get(), get()) } }
val viewModelSenado = module { viewModel { SenadoViewModel(get()) } }
val viewModelSenador = module { viewModel { SenadorViewModel(get()) } }
val viewModelSenadorGeral = module { viewModel { GeralSenadorViewModel(get()) } }
val viewModelVotacoes = module { viewModel { VotacoesViewModel(get(), get()) } }
val viewModelGastoGeral = module { viewModel { GastoGeralViewModelCamara(get()) } }
val viewModelRankingCamara = module { viewModel { RankingViewModelCamara(get()) } }
val viewModelRankingSenado = module { viewModel { RankingViewModelSenado(get()) } }
val viewModelGastoGeralSenado = module { viewModel { GastoGeralViewModelSenado(get()) } }
val viewModelVotacoesCamara = module { viewModel { VotacoesViewModelCamara() } }

val appModules = listOf( retrofitModule, viewModelModule, repositorySearch, progressModule,
        viewModelDeputado, repositoryIdDeputado, viewModelDespesas, repositoryDespesasDeputado,
        securityPreferences, viewModelFront, repositoryFront, viewModelProposta, repositoryProposta,
        ageModule, viewModelOccupation, repositoryOccupation, repositorySenado, viewModelSenado,
        viewModelSenador, visibilityNavFloating, repositorySenador, modifyChip, viewModelSenadorGeral,
        repositorySenadorGeral, retiraAcento, repositoryVotacoes, viewModelVotacoes, retValueInt,
        repositoryVotacoesItem, repositoryGastoGeral, viewModelGastoGeral, viewModelCotasSenador,
        formatValorBi, formatValorFloat, validationInternet, modifyHttp, dialogPhoto,
        viewModelRankingCamara, viewModelRankingSenado, viewModelGastoGeralSenado,
        viewModelVotacoesCamara, repositoryVotacoesCamara, viewModelFrontId, animationView,
        dialogFragment, converterValueNotes
)