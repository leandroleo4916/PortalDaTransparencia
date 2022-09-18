package com.example.portaldatransparencia.dataclass

data class Occupation(
    val dados: List<OccupationDado>,
    val links: List<Link>
)

data class OccupationDado (
    val titulo: String,
    val entidade: String,
    val entidadeUF: String,
    val entidadePais: String,
    val anoInicio: Long,
    val anoFim: Long
)
