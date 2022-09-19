package com.example.portaldatransparencia.dataclass

data class OccupationDataClass(
    val dados: List<Occupation>,
    val links: List<Link>
)

data class Occupation (
    val titulo: String,
    val entidade: String,
    val entidadeUF: String,
    val entidadePais: String,
    val anoInicio: Long,
    val anoFim: Long
)
