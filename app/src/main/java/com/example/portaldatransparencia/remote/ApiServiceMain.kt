package com.example.portaldatransparencia.remote

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
    @Headers("Content-Type: application/json")
    @GET("/api/v2/votacoes?")
    fun getVotacoes(
        @Query("ordem") ordem: String,
        @Query("ordenarPor") ordenarPor: String,
        @Query("pagina") pagina: String,
        @Query("itens") itens: String,
    ): Call<VotacoesList>
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
        @Query("itens") itens: Int = 100,
        @Query("pagina") pagina: Int,
        @Query("ordem") ordem: String = "ASC",
        @Query("ordenarPor") ordenarPor: String = "id"
    ): Call<PropostaDataClass>
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
    @GET("https://raw.githubusercontent.com/leandroleo4916/API_SENADO/master/geralDeputado")
    suspend fun getGastoGeral(): Response<GastoGeralCamara>
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

