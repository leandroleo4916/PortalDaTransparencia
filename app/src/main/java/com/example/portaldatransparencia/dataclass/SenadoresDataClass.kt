package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class SenadoresDataClass(
    @SerializedName("ListaParlamentarEmExercicio")
    val listaParlamentarEmExercicio: ListaParlamentarEmExercicio
)

data class ListaParlamentarEmExercicio (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerializedName("Metadados")
    val metadados: Metadados,

    @SerializedName("Parlamentares")
    val parlamentares: Parlamentares
)

data class Metadados (
    @SerializedName("Versao")
    val versao: String,

    @SerializedName("VersaoServico")
    val versaoServico: String,

    @SerialName("DataVersaoServico")
    val dataVersaoServico: String,

    @SerialName("DescricaoDataSet")
    val descricaoDataSet: String
)

data class Parlamentares (
    @SerializedName("Parlamentar")
    val parlamentar: List<Parlamentar>
)

data class Parlamentar (
    @SerializedName("IdentificacaoParlamentar")
    val identificacaoParlamentar: IdentificacaoParlamentar,

    @SerializedName("Mandato")
    val mandato: Mandato
)

data class IdentificacaoParlamentar (
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

    @SerializedName("FormaTratamento")
    val formaTratamento: String,

    @SerializedName("UrlFotoParlamentar")
    val urlFotoParlamentar: String,

    @SerializedName("UrlPaginaParlamentar")
    val urlPaginaParlamentar: String,

    @SerializedName("UrlPaginaParticular")
    val urlPaginaParticular: String? = null,

    @SerializedName("EmailParlamentar")
    val emailParlamentar: String? = null,

    @SerializedName("Telefones")
    val telefones: TelefoneElement? = null,

    @SerializedName("SiglaPartidoParlamentar")
    val siglaPartidoParlamentar: String,

    @SerializedName("UfParlamentar")
    val ufParlamentar: String,

    @SerializedName("MembroMesa")
    val membroMesa: String,

    @SerializedName("MembroLideranca")
    val membroLideranca: String
)

data class TelefoneElement (
    @SerializedName("NumeroTelefone")
    val numeroTelefone: String,

    @SerializedName("OrdemPublicacao")
    val ordemPublicacao: String,

    @SerializedName("IndicadorFax")
    val indicadorFax: String
)

data class Mandato (
    @SerializedName("CodigoMandato")
    val codigoMandato: String,

    @SerializedName("UfParlamentar")
    val ufParlamentar: String,

    @SerializedName("PrimeiraLegislaturaDoMandato")
    val primeiraLegislaturaDoMandato: ALegislaturaDoMandato,

    @SerializedName("SegundaLegislaturaDoMandato")
    val segundaLegislaturaDoMandato: ALegislaturaDoMandato,

    @SerializedName("DescricaoParticipacao")
    val descricaoParticipacao: DescricaoParticipacao,

    @SerializedName("Suplentes")
    val suplentes: Suplentes,

    @SerializedName("Exercicios")
    val exercicios: Exercicios,

    @SerializedName("Titular")
    val titular: Titular? = null
)

@Serializable
enum class DescricaoParticipacao(val value: String) {
    The1ºSuplente("1º Suplente"),
    The2ºSuplente("2º Suplente"),
    Titular("Titular");

    companion object : KSerializer<DescricaoParticipacao> {
        override val descriptor: SerialDescriptor
            get() {
            return PrimitiveSerialDescriptor("quicktype.DescricaoParticipacao", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): DescricaoParticipacao = when (val value = decoder.decodeString()) {
            "1º Suplente" -> The1ºSuplente
            "2º Suplente" -> The2ºSuplente
            "Titular"     -> Titular
            else          -> throw IllegalArgumentException("DescricaoParticipacao could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: DescricaoParticipacao) {
            return encoder.encodeString(value.value)
        }
    }
}

data class Exercicios (
    @SerializedName("Exercicio")
    val exercicio: List<Exercicio>
)

data class Exercicio (
    @SerializedName("CodigoExercicio")
    val codigoExercicio: String,

    @SerializedName("DataInicio")
    val dataInicio: String,

    @SerializedName("DataFim")
    val dataFim: String? = null,

    @SerializedName("SiglaCausaAfastamento")
    val siglaCausaAfastamento: String? = null,

    @SerializedName("DescricaoCausaAfastamento")
    val descricaoCausaAfastamento: String? = null,

    @SerializedName("DataLeitura")
    val dataLeitura: String? = null
)

data class ALegislaturaDoMandato (
    @SerializedName("NumeroLegislatura")
    val numeroLegislatura: String,

    @SerializedName("DataInicio")
    val dataInicio: String,

    @SerializedName("DataFim")
    val dataFim: String
)

data class Suplentes (
    @SerializedName("Suplente")
    val suplente: List<Titular>
)

data class Titular (
    @SerializedName("DescricaoParticipacao")
    val descricaoParticipacao: DescricaoParticipacao,

    @SerializedName("CodigoParlamentar")
    val codigoParlamentar: String,

    @SerializedName("NomeParlamentar")
    val nomeParlamentar: String
)


