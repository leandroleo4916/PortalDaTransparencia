package com.example.portaldatransparencia.remote

import com.example.portaldatransparencia.dataclass.*
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceMain {
    @GET("/api/v2/deputados?")
    suspend fun getDeputados(
        @Query("ordem") ordem: String,
        @Query("ordenarPor") ordenarPor: String
    ): Response<MainDataClass>
}

interface ApiServiceIdDeputado {
    @GET("/api/v2/deputados/{id}")
    suspend fun getIdDeputado(
         @Path("id") id: String
    ): Response<IdDeputadoDataClass>
}

interface ApiServiceIdDespesas {
    @GET("/api/v2/deputados/{id}/despesas?")
    suspend fun getIdDespesas(
         @Path("id") id: String,
         @Query("ano") ano: String,
         @Query("itens") itens: Int = 100,
         @Query("pagina") pagina: Int,
         @Query("ordem") ordem: String = "ASC",
         @Query("ordenarPor") ordenarPor: String = "ano"
    ): Response<Despesas>
}

interface ApiServiceFrente {
    @GET("/api/v2/deputados/{id}/frentes")
    suspend fun getFrente(
        @Path("id") id: String
    ): Response<Frente>
}

interface ApiServiceFrenteId {
    @GET("/api/v2/deputados/{id}/frentes")
    suspend fun getFrenteId(
        @Path("id") id: String
    ): Response<FrenteId>
}

interface ApiServiceOccupation {
    @GET("/api/v2/deputados/{id}/ocupacoes")
    suspend fun getOccupation(
        @Path("id") id: String
    ): Response<OccupationDataClass>
}

interface ApiServiceProposta {
    @GET("/api/v2/proposicoes?")
    suspend fun getProposta(
        @Query("ano") ano: String,
        @Query("idDeputadoAutor") id: String,
        @Query("itens") itens: Int = 100,
        @Query("pagina") pagina: Int,
        @Query("ordem") ordem: String = "ASC",
        @Query("ordenarPor") ordenarPor: String = "id"
    ): Response<PropostaDataClass>
}

interface ApiServiceSenado {
    @Headers("Accept: application/json")
    @GET("https://legis.senado.leg.br/dadosabertos/senador/lista/atual.json")
    suspend fun getSenado(): Response<SenadoresDataClass>
}

interface ApiServiceSenador {
    @Headers("Accept: application/json")
    @GET("https://legis.senado.leg.br/dadosabertos/senador/{id}.json")
    suspend fun getSenador(
        @Path("id") id: String
    ): Response<SenadorDataClass>
}

