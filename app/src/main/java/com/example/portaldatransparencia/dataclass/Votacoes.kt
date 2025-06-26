package com.example.portaldatransparencia.dataclass

data class VotacoesList(
    val dados: List<VotacaoId>
)

data class VotacaoId (
    val id: String,
    val uri: String,
    val data: String,
    val dataHoraRegistro: String,
    val idOrgao: Long,
    val uriOrgao: String,
    val siglaOrgao: String,
    val idEvento: Long,
    val uriEvento: String,
    var aprovacao: Long? = null,
    val votosSim: Long,
    val votosNao: Long,
    val votosOutros: Long,
    val descricao: String,
    val ultimaAberturaVotacao: UltimaAberturaVotacao,
    val ultimaApresentacaoProposicao: UltimaApresentacaoProposicao
)

data class UltimaAberturaVotacao (
    val dataHoraRegistro: String,
    val descricao: String
)

data class UltimaApresentacaoProposicao (
    val dataHoraRegistro: String,
    val descricao: String,
    val idProposicao: Long,
    val uriProposicao: String
)


