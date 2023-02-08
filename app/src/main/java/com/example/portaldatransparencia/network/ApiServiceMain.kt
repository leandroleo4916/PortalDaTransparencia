package com.example.portaldatransparencia.network

import com.example.portaldatransparencia.dataclass.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceMain {
    @GET("/api/v2/deputados?")
    fun getDeputados(
        @Query("ordem") ordem: String,
        @Query("ordenarPor") ordenarPor: String
    ): Call<MainDataClass>
}

interface ApiVotacoes {
    @GET("https://dadosabertos.camara.leg.br/arquivos/votacoes/json/votacoes-{ano}.json")
    fun getVotacoes(
        @Path("ano") ano: String
    ): Call<VotacoesList>
}

interface ApiVotacoesSenado {
    @GET("https://legis.senado.leg.br/dadosabertos/arquivos/ListaVotacoes{ano}.json")
    fun getVotacoes(
        @Path("ano") ano: String
    ): Call<VotacaoSenado>
}

interface ApiServiceIdDeputado {
    @GET("/api/v2/deputados/{id}")
    fun getIdDeputado(
         @Path("id") id: String
    ): Call<IdDeputadoDataClass>
}

interface ApiServiceIdDespesas {
    @GET("/api/v2/deputados/{id}/despesas?")
    fun getIdDespesas(
         @Path("id") id: String,
         @Query("ano") ano: String,
         @Query("itens") itens: Int = 100,
         @Query("pagina") pagina: Int,
         @Query("ordem") ordem: String = "ASC",
         @Query("ordenarPor") ordenarPor: String = "ano"
    ): Call<Despesas>
}

interface ApiServiceFrente {
    @GET("/api/v2/deputados/{id}/frentes")
    fun getFrente(
        @Path("id") id: String
    ): Call<Frente>
}

interface ApiServiceFrenteId {
    @GET("/api/v2/frentes/{id}")
    fun getFrenteId(
        @Path("id") id: String
    ): Call<FrenteId>
}

interface ApiServiceOccupation {
    @GET("/api/v2/deputados/{id}/ocupacoes")
    fun getOccupation(
        @Path("id") id: String
    ): Call<OccupationDataClass>
}

interface ApiServiceProposta {
    @GET("/api/v2/proposicoes?")
    fun getProposta(
        @Query("ano") ano: String,
        @Query("idDeputadoAutor") id: String,
        @Query("itens") itens: Int,
        @Query("pagina") pagina: Int,
        @Query("ordem") ordem: String = "ASC",
        @Query("ordenarPor") ordenarPor: String = "id"
    ): Call<PropostaDataClass>
}

interface ApiServicePropostaItem {
    @GET("/api/v2/proposicoes/{id}")
    fun getPropostaItem(
        @Path("id") id: String
    ): Call<ProposicaoDataClass>
}

interface ApiServiceSenado {
    @GET("https://legis.senado.leg.br/dadosabertos/senador/lista/atual.json")
    suspend fun getSenado(): Response<SenadoresDataClass>
}

interface ApiServiceSenador {
    @GET("https://legis.senado.leg.br/dadosabertos/senador/{id}.json")
    suspend fun getSenador(
        @Path("id") id: String
    ): Response<SenadorDataClass>
}

interface ApiServiceGastos {
    @Headers("Content-Type: application/json")
    @GET("https://raw.githubusercontent.com/leandroleo4916/API_SENADO/master/{ano}/{nome}")
    suspend fun getGastos(
        @Path("ano") ano: String,
        @Path("nome") nome: String
    ): Response<SenadorGastosDataClass>
}

interface ApiServiceGastoGeralSenador {
    @Headers("Content-Type: application/json")
    @GET("https://raw.githubusercontent.com/leandroleo4916/API_SENADO/master/geralSenador")
    suspend fun getGastoGeral(): Response<GastoGeralDataClass>
}

interface ApiServiceGastoGeralDeputado {
    @Headers("Content-Type: application/json")
    @GET("https://raw.githubusercontent.com/leandroleo4916/API_SENADO/master/gastosCamara/geral{ano}")
    suspend fun getGastoGeral(
        @Path("ano") ano: String
    ): Response<GastoGeralCamara>
}

interface ApiServiceRankingDeputado {
    @Headers("Content-Type: application/json")
    @GET("https://raw.githubusercontent.com/leandroleo4916/API_SENADO/master/rankingCamara/ranking{ano}")
    suspend fun rankingGeral(
        @Path("ano") ano: String
    ): Response<RankingCamara>
}

interface ApiServiceVotacoes {
    @Headers("Content-Type: application/json")
    @GET("https://legis.senado.leg.br/dadosabertos/senador/{id}/votacoes.json")
    suspend fun getVotacoes(
        @Path("id") id: String,
        @Query("ano") ano: String
    ): Response<DataClassVotacoesSenador>
}

interface ApiServiceVotacoesItem {
    @Headers("Content-Type: application/json")
    @GET("https://legis.senado.leg.br/dadosabertos/senador/{id}/votacoes.json")
    suspend fun getVotacoesItem(
        @Path("id") id: String,
        @Query("ano") ano: String
    ): Response<DataClassVotacoesItem>
}

interface ApiServiceSenadorCargos {
    @GET("https://legis.senado.leg.br/dadosabertos/senador/{id}/cargos.json")
    suspend fun getCargos(
        @Path("id") id: String
    ): Response<CargoSenadorDataClass>
}

interface ApiServiceEvento {
    @GET("https://dadosabertos.camara.leg.br/api/v2/eventos/{id}")
    fun getEvento(@Path("id") id: String)
    : Call<EventoDataClass>
}

