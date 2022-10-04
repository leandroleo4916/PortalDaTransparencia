package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class DataClassVotacoesItem(
    @SerializedName("VotacaoParlamentar")
    val votacaoParlamentar: VotacaoParlamentarItem?
)

data class VotacaoParlamentarItem (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerializedName("Metadados")
    val metadados: Metadados,

    @SerializedName("Parlamentar")
    val parlamentar: ParlamentarVotoItem?
)

data class ParlamentarVotoItem (
    @SerializedName("Codigo")
    val codigo: String,

    @SerializedName("Nome")
    val nome: String,

    @SerializedName("Votacoes")
    val votacoes: VotacoesItem?
)

data class VotacoesItem (
    @SerializedName("Votacao")
    val votacao: Votacao?
)

