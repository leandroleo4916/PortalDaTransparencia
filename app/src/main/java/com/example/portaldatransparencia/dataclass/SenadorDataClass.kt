package com.example.portaldatransparencia.dataclass

import kotlinx.serialization.*

data class SenadorDataClass(
    @SerialName("DetalheParlamentar")
    val detalheParlamentar: DetalheParlamentar
)

@Serializable
data class DetalheParlamentar (
    @SerialName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerialName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerialName("Metadados")
    val metadados: Metadados,

    @SerialName("Parlamentar")
    val parlamentar: Parlamentar
)

@Serializable
data class DadosBasicosParlamentar (
    @SerialName("DataNascimento")
    val dataNascimento: String,

    @SerialName("Naturalidade")
    val naturalidade: String,

    @SerialName("UfNaturalidade")
    val ufNaturalidade: String,

    @SerialName("EnderecoParlamentar")
    val enderecoParlamentar: String
)

@Serializable
data class OutrasInformacoes (
    @SerialName("Servico")
    val servico: List<Servico>
)

@Serializable
data class Servico (
    @SerialName("NomeServico")
    val nomeServico: String,

    @SerialName("DescricaoServico")
    val descricaoServico: String? = null,

    @SerialName("UrlServico")
    val urlServico: String
)

@Serializable
data class Telefone (
    @SerialName("NumeroTelefone")
    val numeroTelefone: String,

    @SerialName("OrdemPublicacao")
    val ordemPublicacao: String,

    @SerialName("IndicadorFax")
    val indicadorFax: String
)

