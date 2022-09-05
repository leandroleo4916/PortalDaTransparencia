package com.example.portaldatransparencia.dataclass

data class MainDataClass (
    val dados: List<Dado>,
    val links: List<Link>
)

data class Dado (
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

