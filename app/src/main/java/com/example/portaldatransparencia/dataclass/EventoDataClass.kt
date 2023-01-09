package com.example.portaldatransparencia.dataclass

import com.google.gson.JsonArray

data class EventoDataClass(
    val dados: DadosEvento,
    val links: List<LinkType>
)

data class DadosEvento (
    val uriDeputados: String? = null,
    val uriConvidados: String? = null,
    val fases: String? = null,
    val id: Long,
    val uri: String,
    val dataHoraInicio: String,
    val dataHoraFim: String,
    val situacao: String,
    val descricaoTipo: String,
    val descricao: String,
    val localExterno: String? = null,
    val orgaos: List<Orgao>,
    val requerimentos: JsonArray,
    val localCamara: LocalCamara,
    val urlDocumentoPauta: String,
    val urlRegistro: String
)

data class LocalCamara (
    val nome: String,
    val predio: String? = null,
    val sala: String? = null,
    val andar: String? = null
)

data class Orgao (
    val id: Long,
    val uri: String,
    val sigla: String,
    val nome: String,
    val apelido: String,
    val codTipoOrgao: Long,
    val tipoOrgao: String,
    val nomePublicacao: String,
    val nomeResumido: String
)

data class LinkType (
    val rel: String,
    val href: String,
    val type: String? = null
)

