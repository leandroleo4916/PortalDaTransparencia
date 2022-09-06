package com.example.portaldatransparencia.dataclass

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class Despesas (
    val dados: List<DadoDespesas>,
    val links: List<Link>
)

@Serializable
data class DadoDespesas (
    val ano: Long,
    val mes: Long,
    val tipoDespesa: TipoDespesa,
    val codDocumento: Long,
    val tipoDocumento: TipoDocumento,
    val codTipoDocumento: Long,
    val dataDocumento: String,
    val numDocumento: String,
    val valorDocumento: Double,
    val urlDocumento: String,
    val nomeFornecedor: String,
    val cnpjCpfFornecedor: String,
    val valorLiquido: Double,
    val valorGlosa: Double,
    val numRessarcimento: String,
    val codLote: Long,
    val parcela: Long
)

@Serializable
enum class TipoDespesa(val value: String) {
    ManutençãoDeEscritórioDeApoioÀAtividadeParlamentar(
        "Manutenção de escritório de apoio parlamentar");

    companion object : KSerializer<TipoDespesa> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("quicktype.TipoDespesa", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): TipoDespesa =
            when (val value = decoder.decodeString()) {
                "Manutenção de escritório de apoio parlamentar"
            -> ManutençãoDeEscritórioDeApoioÀAtividadeParlamentar
            else
            -> throw IllegalArgumentException("TipoDespesa could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: TipoDespesa) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
enum class TipoDocumento(val value: String) {
    NotaFiscal("Nota Fiscal"),
    RecibosOutros("Recibos/Outros");

    companion object : KSerializer<TipoDocumento> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("quicktype.TipoDocumento", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): TipoDocumento =
            when (val value = decoder.decodeString()) {
            "Nota Fiscal"    -> NotaFiscal
            "Recibos/Outros" -> RecibosOutros
            else
            -> throw IllegalArgumentException("TipoDocumento could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: TipoDocumento) {
            return encoder.encodeString(value.value)
        }
    }
}
