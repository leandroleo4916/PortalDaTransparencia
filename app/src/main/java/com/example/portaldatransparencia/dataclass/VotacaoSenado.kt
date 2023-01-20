package com.example.portaldatransparencia.dataclass

import com.google.gson.annotations.SerializedName

data class VotacaoSenado(
    @SerializedName("ListaVotacoes")
    val listaVotacoes: ListaVotacoes
)

data class ListaVotacoes (
    @SerializedName("@xmlns:xsi")
    val xmlnsXsi: String,
    @SerializedName("@xsi:noNamespaceSchemaLocation")
    val xsiNoNamespaceSchemaLocation: String,
    @SerializedName("Metadados")
    val metadados: Metadados,
    @SerializedName("Votacoes")
    val votacoes: VotacoesSenado
)

data class VotacoesSenado (
    @SerializedName("Votacao")
    val votacao: List<VotacaoSenadoItem>
)

data class VotacaoSenadoItem (
    @SerializedName("CodigoSessao")
    val codigoSessao: String,
    @SerializedName("SiglaCasa")
    val siglaCasa: String?,
    @SerializedName("CodigoSessaoLegislativa")
    val codigoSessaoLegislativa: String,
    @SerializedName("TipoSessao")
    val tipoSessao: String,
    @SerializedName("NumeroSessao")
    val numeroSessao: String,
    @SerializedName("DataSessao")
    val dataSessao: String,
    @SerializedName("HoraInicio")
    val horaInicio: String?,
    @SerializedName("CodigoTramitacao")
    val codigoTramitacao: String? = null,
    @SerializedName("CodigoSessaoVotacao")
    val codigoSessaoVotacao: String,
    @SerializedName("SequencialSessao")
    val sequencialSessao: String,
    @SerializedName("Secreta")
    val secreta: String?,
    @SerializedName("DescricaoVotacao")
    val descricaoVotacao: String,
    @SerializedName("CodigoMateria")
    val codigoMateria: String? = null,
    @SerializedName("SiglaMateria")
    val siglaMateria: SiglaMateria? = null,
    @SerializedName("NumeroMateria")
    val numeroMateria: String? = null,
    @SerializedName("AnoMateria")
    val anoMateria: String? = null,
    @SerializedName("DescricaoIdentificacaoMateria")
    val descricaoIdentificacaoMateria: String? = null,
    @SerializedName("SiglaCasaMateria")
    val siglaCasaMateria: String?,
    @SerializedName("Votos")
    val votos: Votos,
    @SerializedName("DescricaoObjetivoProcesso")
    val descricaoObjetivoProcesso: String?,
    @SerializedName("Resultado")
    val resultado: String?,
    @SerializedName("TotalVotosSim")
    val totalVotosSim: String? = null,
    @SerializedName("TotalVotosNao")
    val totalVotosNao: String? = null,
    @SerializedName("TotalVotosAbstencao")
    val totalVotosAbstencao: String? = null
)

enum class SiglaMateria(val value: String) {
    @SerializedName("MPV") Mpv("MPV - Medida Provisória"),
    @SerializedName("MSF") Msf("MSF - Mensagem Senado Federal"),
    @SerializedName("OFS") Ofs("OFS - Oficio Senado Federal"),
    @SerializedName("PLC") PLC("PLC - Projeto de Lei da Câmara"),
    @SerializedName("PDS") Pds("PDS - Projeto de Decreto Legislativo"),
    @SerializedName("PEC") Pec("PEC - Proposta de Emenda à Constituição"),
    @SerializedName("PLS") Pls("PLS - Projeto de Lei Complementar"),
    @SerializedName("SCD") Scd("SCD");
}

data class Votos (
    @SerializedName("VotoParlamentar")
    val votoParlamentar: List<VotoParlamentar>
)

data class VotoParlamentar (
    @SerializedName("CodigoParlamentar")
    val codigoParlamentar: String,
    @SerializedName("NomeParlamentar")
    val nomeParlamentar: String,
    @SerializedName("SexoParlamentar")
    val sexoParlamentar: String,
    @SerializedName("SiglaPartido")
    val siglaPartido: String,
    @SerializedName("SiglaUF")
    val siglaUF: String,
    @SerializedName("Url")
    val url: String,
    @SerializedName("Foto")
    val foto: String,
    @SerializedName("Tratamento")
    val tratamento: String,
    @SerializedName("Voto")
    val voto: String
)

enum class Voto(val value: String) {
    @SerializedName("Abstenção") Abstenção("Abstenção"),
    @SerializedName("AP ") Ap("AP "),
    @SerializedName("LL ") Ll("LL "),
    @SerializedName("LP ") Lp("LP "),
    @SerializedName("LS ") Ls("LS "),
    @SerializedName("MIS") MIS("MIS"),
    @SerializedName("NCom") NCOM("NCom"),
    @SerializedName("Não") Não("Não"),
    @SerializedName("P-NRV") PNrv("P-NRV"),
    @SerializedName("Presidente (art. 51 RISF)") PresidenteArt51RISF("Presidente (art. 51 RISF)"),
    @SerializedName("Sim") Sim("Sim"),
    @SerializedName("Votou") Votou("Votou");
}