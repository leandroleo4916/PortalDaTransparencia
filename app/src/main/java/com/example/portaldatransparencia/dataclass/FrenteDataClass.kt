package com.example.portaldatransparencia.dataclass

data class Frente(
    val dados: List<DadoFrente>,
    val links: List<Link>
)

data class DadoFrente (
    val id: Long,
    val uri: String,
    val titulo: String,
    val idLegislatura: Long
)
