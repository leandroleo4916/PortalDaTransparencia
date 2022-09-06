package com.example.portaldatransparencia.remote

import com.example.portaldatransparencia.dataclass.Despesas
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.MainDataClass
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
         @Query("ordem") ordem: String = "ASC",
         @Query("ordenarPor") ordenarPor: String = "ano"
    ): Response<Despesas>
}

