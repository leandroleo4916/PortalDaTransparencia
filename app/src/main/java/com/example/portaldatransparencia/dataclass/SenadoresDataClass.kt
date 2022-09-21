package com.example.portaldatransparencia.dataclass
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

data class SenadoresDataClass(
    @SerialName("ListaParlamentarEmExercicio")
    val listaParlamentarEmExercicio: ListaParlamentarEmExercicio
)

@Serializable
data class ListaParlamentarEmExercicio (
    @SerialName("@xmlns:xsi")
    val xmlnsXsi: String,

    @SerialName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,

    @SerialName("Metadados")
    val metadados: Metadados,

    @SerialName("Parlamentares")
    val parlamentares: Parlamentares
)

@Serializable
data class Metadados (
    @SerialName("Versao")
    val versao: String,

    @SerialName("VersaoServico")
    val versaoServico: String,

    @SerialName("DataVersaoServico")
    val dataVersaoServico: String,

    @SerialName("DescricaoDataSet")
    val descricaoDataSet: String
)

@Serializable
data class Parlamentares (
    @SerialName("Parlamentar")
    val parlamentar: List<Parlamentar>
)

@Serializable
data class Parlamentar (
    @SerialName("IdentificacaoParlamentar")
    val identificacaoParlamentar: IdentificacaoParlamentar,

    @SerialName("Mandato")
    val mandato: Mandato
)

@Serializable
data class IdentificacaoParlamentar (
    @SerialName("CodigoParlamentar")
    val codigoParlamentar: String,

    @SerialName("CodigoPublicoNaLegAtual")
    val codigoPublicoNaLegAtual: String,

    @SerialName("NomeParlamentar")
    val nomeParlamentar: String,

    @SerialName("NomeCompletoParlamentar")
    val nomeCompletoParlamentar: String,

    @SerialName("SexoParlamentar")
    val sexoParlamentar: SexoParlamentar,

    @SerialName("FormaTratamento")
    val formaTratamento: FormaTratamento,

    @SerialName("UrlFotoParlamentar")
    val urlFotoParlamentar: String,

    @SerialName("UrlPaginaParlamentar")
    val urlPaginaParlamentar: String,

    @SerialName("UrlPaginaParticular")
    val urlPaginaParticular: String? = null,

    @SerialName("EmailParlamentar")
    val emailParlamentar: String? = null,

    @SerialName("Telefones")
    val telefones: Telefones,

    @SerialName("SiglaPartidoParlamentar")
    val siglaPartidoParlamentar: String,

    @SerialName("UfParlamentar")
    val ufParlamentar: String,

    @SerialName("MembroMesa")
    val membroMesa: MembroLideranca,

    @SerialName("MembroLideranca")
    val membroLideranca: MembroLideranca
)

@Serializable
enum class FormaTratamento(val value: String) {
    Senador("Senador "),
    Senadora("Senadora ");

    companion object : KSerializer<FormaTratamento> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("quicktype.FormaTratamento", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): FormaTratamento = when (val value = decoder.decodeString()) {
            "Senador "  -> Senador
            "Senadora " -> Senadora
            else        -> throw IllegalArgumentException("FormaTratamento could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: FormaTratamento) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
enum class MembroLideranca(val value: String) {
    Não("Não"),
    Sim("Sim");

    companion object : KSerializer<MembroLideranca> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("quicktype.MembroLideranca", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): MembroLideranca = when (val value = decoder.decodeString()) {
            "Não" -> Não
            "Sim" -> Sim
            else  -> throw IllegalArgumentException("MembroLideranca could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: MembroLideranca) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
enum class SexoParlamentar(val value: String) {
    Feminino("Feminino"),
    Masculino("Masculino");

    companion object : KSerializer<SexoParlamentar> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("quicktype.SexoParlamentar", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): SexoParlamentar = when (val value = decoder.decodeString()) {
            "Feminino"  -> Feminino
            "Masculino" -> Masculino
            else        -> throw IllegalArgumentException("SexoParlamentar could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: SexoParlamentar) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class Telefones (
    @SerialName("Telefone")
    val telefone: TelefoneUnion
)

@Serializable
sealed class TelefoneUnion {
    class TelefoneElementArrayValue(val value: List<TelefoneElement>) : TelefoneUnion()
    class TelefoneElementValue(val value: TelefoneElement)            : TelefoneUnion()
}

@Serializable
data class TelefoneElement (
    @SerialName("NumeroTelefone")
    val numeroTelefone: String,

    @SerialName("OrdemPublicacao")
    val ordemPublicacao: String,

    @SerialName("IndicadorFax")
    val indicadorFax: MembroLideranca
)

@Serializable
data class Mandato (
    @SerialName("CodigoMandato")
    val codigoMandato: String,

    @SerialName("UfParlamentar")
    val ufParlamentar: String,

    @SerialName("PrimeiraLegislaturaDoMandato")
    val primeiraLegislaturaDoMandato: ALegislaturaDoMandato,

    @SerialName("SegundaLegislaturaDoMandato")
    val segundaLegislaturaDoMandato: ALegislaturaDoMandato,

    @SerialName("DescricaoParticipacao")
    val descricaoParticipacao: DescricaoParticipacao,

    @SerialName("Suplentes")
    val suplentes: Suplentes,

    @SerialName("Exercicios")
    val exercicios: Exercicios,

    @SerialName("Titular")
    val titular: Titular? = null
)

@Serializable
enum class DescricaoParticipacao(val value: String) {
    The1ºSuplente("1º Suplente"),
    The2ºSuplente("2º Suplente"),
    Titular("Titular");

    companion object : KSerializer<DescricaoParticipacao> {
        override val descriptor: SerialDescriptor get() {
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

@Serializable
data class Exercicios (
    @SerialName("Exercicio")
    val exercicio: List<Exercicio>
)

@Serializable
data class Exercicio (
    @SerialName("CodigoExercicio")
    val codigoExercicio: String,

    @SerialName("DataInicio")
    val dataInicio: String,

    @SerialName("DataFim")
    val dataFim: String? = null,

    @SerialName("SiglaCausaAfastamento")
    val siglaCausaAfastamento: String? = null,

    @SerialName("DescricaoCausaAfastamento")
    val descricaoCausaAfastamento: String? = null,

    @SerialName("DataLeitura")
    val dataLeitura: String? = null
)

@Serializable
data class ALegislaturaDoMandato (
    @SerialName("NumeroLegislatura")
    val numeroLegislatura: String,

    @SerialName("DataInicio")
    val dataInicio: String,

    @SerialName("DataFim")
    val dataFim: String
)

@Serializable
data class Suplentes (
    @SerialName("Suplente")
    val suplente: List<Titular>
)

@Serializable
data class Titular (
    @SerialName("DescricaoParticipacao")
    val descricaoParticipacao: DescricaoParticipacao,

    @SerialName("CodigoParlamentar")
    val codigoParlamentar: String,

    @SerialName("NomeParlamentar")
    val nomeParlamentar: String
)

