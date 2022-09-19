package com.example.portaldatransparencia.dataclass

data class PropostaDataClass(
    val dados: List<Proposta>,
    val links: List<Link>
)

data class Proposta (
    val id: Long,
    val uri: String,
    val siglaTipo: String,
    val codTipo: Long,
    val numero: Long,
    val ano: Long,
    val ementa: String?
)

