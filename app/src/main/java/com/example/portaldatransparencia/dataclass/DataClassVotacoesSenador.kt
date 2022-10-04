package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName

data class DataClassVotacoesSenador(
    @SerializedName("VotacaoParlamentar")
    val votacaoParlamentar: VotacaoParlamentar?
)

data class VotacaoParlamentar (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerializedName("Metadados")
    val metadados: Metadados,

    @SerializedName("Parlamentar")
    val parlamentar: ParlamentarVoto?
)

data class ParlamentarVoto (
    @SerializedName("Codigo")
    val codigo: String,

    @SerializedName("Nome")
    val nome: String,

    @SerializedName("Votacoes")
    val votacoes: Votacoes?
)

data class Votacoes (
    @SerializedName("Votacao")
    val votacao: List<Votacao>?
)

data class Votacao (
    @SerializedName("SessaoPlenaria")
    val sessaoPlenaria: SessaoPlenaria,

    @SerializedName("Materia")
    val materia: Materia,

    @SerializedName("Tramitacao")
    val tramitacao: Tramitacao?,

    @SerializedName("CodigoSessaoVotacao")
    val codigoSessaoVotacao: String,

    @SerializedName("Sequencial")
    val sequencial: String?,

    @SerializedName("IndicadorVotacaoSecreta")
    val indicadorVotacaoSecreta: String?,

    @SerializedName("DescricaoVotacao")
    val descricaoVotacao: String?,

    @SerializedName("DescricaoResultado")
    val descricaoResultado: String?,

    @SerializedName("SiglaDescricaoVoto")
    val siglaDescricaoVoto: String?,

    @SerializedName("descricaoVoto")
    val descricaoVoto: String?
)

data class Materia (
    @SerializedName("Codigo")
    val codigo: String?,

    @SerializedName("IdentificacaoProcesso")
    val identificacaoProcesso: String?,

    @SerializedName("DescricaoIdentificacao")
    val descricaoIdentificacao: String?,

    @SerializedName("Sigla")
    val sigla: String?,

    @SerializedName("Numero")
    val numero: String?,

    @SerializedName("Ano")
    val ano: String?,

    @SerializedName("Ementa")
    val ementa: String?,

    @SerializedName("Data")
    val data: String?
)

data class SessaoPlenaria (
    @SerializedName("CodigoSessao")
    val codigoSessao: String,

    @SerializedName("SiglaCasaSessao")
    val siglaCasaSessao: String,

    @SerializedName("CodigoSessaoLegislativa")
    val codigoSessaoLegislativa: String,

    @SerializedName("SiglaTipoSessao")
    val siglaTipoSessao: String,

    @SerializedName("NumeroSessao")
    val numeroSessao: String,

    @SerializedName("DataSessao")
    val dataSessao: String,

    @SerializedName("HoraInicioSessao")
    val horaInicioSessao: String
)

data class Tramitacao (
    @SerializedName("IdentificacaoTramitacao")
    val identificacaoTramitacao: IdentificacaoTramitacao?
)

data class IdentificacaoTramitacao (
    @SerializedName("CodigoTramitacao")
    val codigoTramitacao: String?,

    @SerializedName("NumeroAutuacao")
    val numeroAutuacao: String?,

    @SerializedName("DataTramitacao")
    val dataTramitacao: String?,

    @SerializedName("TextoTramitacao")
    val textoTramitacao: String?,

    @SerializedName("OrigemTramitacao")
    val origemTramitacao: DestinoTramitacaoClass?,

    @SerializedName("DestinoTramitacao")
    val destinoTramitacao: DestinoTramitacaoClass?
)

data class DestinoTramitacaoClass (
    @SerializedName("Local")
    val local: Local
)

data class Local (
    @SerializedName("CodigoLocal")
    val codigoLocal: String,

    @SerializedName("TipoLocal")
    val tipoLocal: String,

    @SerializedName("SiglaCasaLocal")
    val siglaCasaLocal: String,

    @SerializedName("SiglaLocal")
    val siglaLocal: String,

    @SerializedName("NomeLocal")
    val nomeLocal: String
)

