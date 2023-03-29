package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName

data class VotoDeputadosDataC(
    val dados: List<Type>,
    val links: List<Link>
)

data class Type (
    val tipoVoto: String,
    val dataRegistroVoto: String,
    @SerializedName("deputado_")
    val deputado: Deputado
)

data class Deputado (
    val id: Long,
    val uri: String,
    val nome: String,
    val siglaPartido: String,
    val uriPartido: String,
    val siglaUf: String,
    val idLegislatura: Long,
    val urlFoto: String,
    val email: String
)

