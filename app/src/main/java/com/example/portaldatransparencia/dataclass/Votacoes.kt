package com.example.portaldatransparencia.dataclass

data class VotacoesList(
    val dados: List<VotacaoId>,
    val links: List<Link>
)

data class VotacaoId (
    val id: String,
    val uri: String,
    val data: String,
    val dataHoraRegistro: String,
    val siglaOrgao: String,
    val uriOrgao: String,
    val uriEvento: String? = null,
    val proposicaoObjeto: String? = null,
    val uriProposicaoObjeto: String? = null,
    val descricao: String,
    val aprovacao: Long? = null
)

