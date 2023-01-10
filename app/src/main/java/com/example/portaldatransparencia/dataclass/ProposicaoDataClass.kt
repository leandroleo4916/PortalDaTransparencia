package com.example.portaldatransparencia.dataclass

data class ProposicaoDataClass (
    val dados: DadosProposicao,
    val links: List<Link>
)

data class DadosProposicao (
    val id: Long,
    val uri: String,
    val siglaTipo: String,
    val codTipo: Long,
    val numero: Long,
    val ano: Long,
    val ementa: String,
    val dataApresentacao: String,
    val uriOrgaoNumerador: String,
    val statusProposicao: StatusProposicao,
    val uriAutores: String,
    val descricaoTipo: String,
    val ementaDetalhada: String,
    val keywords: String,
    var uriPropPrincipal: String? = null,
    var uriPropAnterior: String? = null,
    var uriPropPosterior: String? = null,
    var urlInteiroTeor: String,
    var urnFinal: String? = null,
    var texto: String? = null,
    var justificativa: String? = null
)

data class StatusProposicao (
    val dataHora: String,
    val sequencia: Long,
    val siglaOrgao: String,
    val uriOrgao: String,
    val uriUltimoRelator: String,
    val regime: String,
    val descricaoTramitacao: String,
    val codTipoTramitacao: String,
    val descricaoSituacao: String,
    val codSituacao: Long,
    val despacho: String,
    var url: String? = null,
    val ambito: String,
    val apreciacao: String
)
