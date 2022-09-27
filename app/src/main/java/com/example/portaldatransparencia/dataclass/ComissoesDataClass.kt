package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName

data class ComissoesDataClass(
    @SerializedName("MembroComissaoParlamentar")
    val membroComissaoParlamentar: MembroComissaoParlamentar
)

data class MembroComissaoParlamentar (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerializedName("Metadados")
    val metadados: MetadadosItem,

    @SerializedName("Parlamentar")
    val parlamentar: ParlamentarElement
)

data class ParlamentarElement (
    @SerializedName("Codigo")
    val codigo: String,

    @SerializedName("Nome")
    val nome: String,

    @SerializedName("MembroComissoes")
    val membroComissoes: MembroComissoes
)

data class MembroComissoes (
    @SerializedName("Comissao")
    val comissao: List<Comissao>
)

data class Comissao (
    @SerializedName("IdentificacaoComissao")
    val identificacaoComissao: IdentificacaoComissao,

    @SerializedName("DescricaoParticipacao")
    val descricaoParticipacao: String,

    @SerializedName("DataInicio")
    val dataInicio: String,

    @SerializedName("DataFim")
    val dataFim: String? = null
)

data class IdentificacaoComissao (
    @SerializedName("CodigoComissao")
    val codigoComissao: String,

    @SerializedName("SiglaComissao")
    val siglaComissao: String,

    @SerializedName("NomeComissao")
    val nomeComissao: String,

    @SerializedName("SiglaCasaComissao")
    val siglaCasaComissao: String
)
