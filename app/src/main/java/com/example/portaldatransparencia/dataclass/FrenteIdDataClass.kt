package com.example.portaldatransparencia.dataclass

data class FrenteId(
val dados: FrontId,
val links: List<Link>
)

data class FrontId (
    val id: Long,
    val uri: String,
    val titulo: String,
    val idLegislatura: Long,
    val telefone: String,
    val email: String,
    val keywords: String,
    val idSituacao: String,
    val situacao: String,
    val urlWebsite: String,
    val urlDocumento: String,
    val coordenador: Coordenador
)

data class Coordenador (
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
