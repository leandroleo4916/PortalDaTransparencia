package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName

data class CargoSenadorDataClass(
    @SerializedName("CargoParlamentar")
    val cargoParlamentar: CargoParlamentar
)

data class CargoParlamentar (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerializedName("Metadados")
    val metadados: MetadadosCargos,

    @SerializedName("Parlamentar")
    val parlamentar: ParlamentarCargos
)

data class MetadadosCargos (
    @SerializedName("Versao")
    val versao: String,

    @SerializedName("VersaoServico")
    val versaoServico: String,

    @SerializedName("DataVersaoServico")
    val dataVersaoServico: String,

    @SerializedName("DescricaoDataSet")
    val descricaoDataSet: String
)

data class ParlamentarCargos (
    @SerializedName("Codigo")
    val codigo: String,

    @SerializedName("Nome")
    val nome: String,

    @SerializedName("Cargos")
    val cargos: Cargos
)

data class Cargos (
    @SerializedName("Cargo")
    val cargo: List<Cargo>
)

data class Cargo (
    @SerializedName("IdentificacaoComissao")
    val identificacaoComissao: IdentificacaoComissao,

    @SerializedName("CodigoCargo")
    val codigoCargo: String,

    @SerializedName("DescricaoCargo")
    val descricaoCargo: String,

    @SerializedName("DataInicio")
    val dataInicio: String,

    @SerializedName("DataFim")
    val dataFim: String? = null
)

