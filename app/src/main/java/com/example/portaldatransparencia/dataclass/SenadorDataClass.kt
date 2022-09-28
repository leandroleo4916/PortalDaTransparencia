package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName

data class SenadorDataClass(
    @SerializedName("DetalheParlamentar")
    val detalheParlamentar: DetalheParlamentar
)

data class DetalheParlamentar (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerializedName("Metadados")
    val metadados: MetadadosItem,

    @SerializedName("Parlamentar")
    val parlamentar: ParlamentarItem
)

data class MetadadosItem (
    @SerializedName("Versao")
    val versao: String,

    @SerializedName("VersaoServico")
    val versaoServico: String,

    @SerializedName("DataVersaoServico")
    val dataVersaoServico: String,

    @SerializedName("DescricaoDataSet")
    val descricaoDataSet: String
)

data class ParlamentarItem (
    @SerializedName("IdentificacaoParlamentar")
    val identificacaoParlamentar: IdentificacaoParlamentarItem,

    @SerializedName("DadosBasicosParlamentar")
    val dadosBasicosParlamentar: DadosBasicosParlamentar,

    @SerializedName("Telefones")
    val telefones: Telefones,

    @SerializedName("OutrasInformacoes")
    val outrasInformacoes: OutrasInformacoes
)

data class DadosBasicosParlamentar (
    @SerializedName("DataNascimento")
    val dataNascimento: String,

    @SerializedName("Naturalidade")
    val naturalidade: String,

    @SerializedName("UfNaturalidade")
    val ufNaturalidade: String,

    @SerializedName("EnderecoParlamentar")
    val enderecoParlamentar: String
)

data class IdentificacaoParlamentarItem (
    @SerializedName("CodigoParlamentar")
    val codigoParlamentar: String,

    @SerializedName("CodigoPublicoNaLegAtual")
    val codigoPublicoNaLegAtual: String,

    @SerializedName("NomeParlamentar")
    val nomeParlamentar: String,

    @SerializedName("NomeCompletoParlamentar")
    val nomeCompletoParlamentar: String,

    @SerializedName("SexoParlamentar")
    val sexoParlamentar: String,

    @SerializedName("UrlFotoParlamentar")
    val urlFotoParlamentar: String,

    @SerializedName("UrlPaginaParlamentar")
    val urlPaginaParlamentar: String,

    @SerializedName("UrlPaginaParticular")
    val urlPaginaParticular: String,

    @SerializedName("EmailParlamentar")
    val emailParlamentar: String,

    @SerializedName("SiglaPartidoParlamentar")
    val siglaPartidoParlamentar: String,

    @SerializedName("UfParlamentar")
    val ufParlamentar: String
)

data class OutrasInformacoes (
    @SerializedName("Servico")
    val servico: List<Servico>
)

data class Servico (
    @SerializedName("NomeServico")
    val nomeServico: String,

    @SerializedName("DescricaoServico")
    val descricaoServico: String? = null,

    @SerializedName("UrlServico")
    val urlServico: String
)

data class Telefones (
    @SerializedName("Telefone")
    val telefone: Any
)

sealed class TelefoneUnion {
    class TelefoneElementArrayValue(val value: List<Telefone>) : TelefoneUnion()
    class TelefoneElementValue(val value: Telefone)            : TelefoneUnion()
}

data class Telefone (
    @SerializedName("NumeroTelefone")
    val numeroTelefone: String,

    @SerializedName("OrdemPublicacao")
    val ordemPublicacao: String,

    @SerializedName("IndicadorFax")
    val indicadorFax: String
)


