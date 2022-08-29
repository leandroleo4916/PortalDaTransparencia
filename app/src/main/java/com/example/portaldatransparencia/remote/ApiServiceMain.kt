package com.example.portaldatransparencia.remote

import com.example.portaldatransparencia.dataclass.MainDataClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceMain {
    @GET("/api/v2/deputados?")
    suspend fun getDeputados(
        @Query("ordem") ordem: String,
        @Query("ordenarPor") ordenarPor: String
    ): Response<MainDataClass>
}

