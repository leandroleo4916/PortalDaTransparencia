package com.example.portaldatransparencia.dataclass

data class NameDataClass (
    val dados: DadosOrgao,
    val links: List<Link>
)

data class DadosOrgao (
    val id: Long,
    val uri: String,
    val sigla: String,
    val nome: String,
    val apelido: String,
    val codTipoOrgao: Long,
    val tipoOrgao: String,
    val nomePublicacao: String,
    val nomeResumido: String?,
    val dataInicio: String?,
    val dataInstalacao: String?,
    val dataFim: String?,
    val dataFimOriginal: String?,
    val casa: String?,
    val sala: String?,
    val urlWebsite: String?,
)
