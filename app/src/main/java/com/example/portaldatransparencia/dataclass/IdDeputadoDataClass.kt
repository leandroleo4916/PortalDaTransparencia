package com.example.portaldatransparencia.dataclass

data class IdDeputadoDataClass(
    val dados: Dados,
    val links: List<Link>
)

data class Dados (
    val id: Long,
    val uri: String,
    val nomeCivil: String,
    val ultimoStatus: UltimoStatus,
    val cpf: String,
    val sexo: String,
    val urlWebsite: String? = null,
    val redeSocial: List<String>,
    val dataNascimento: String,
    val dataFalecimento: String? = null,
    val ufNascimento: String,
    val municipioNascimento: String,
    val escolaridade: String
)

data class UltimoStatus (
    val id: Long,
    val uri: String,
    val nome: String,
    val siglaPartido: String,
    val uriPartido: String,
    val siglaUf: String,
    val idLegislatura: Long,
    val urlFoto: String,
    val email: String? = null,
    val data: String,
    val nomeEleitoral: String,
    val gabinete: Gabinete,
    val situacao: String,
    val condicaoEleitoral: String,
    val descricaoStatus: String? = null
)

data class Gabinete (
    val nome: String,
    val predio: String,
    val sala: String,
    val andar: String,
    val telefone: String,
    val email: String? = null
)

data class Link (
    val rel: String,
    val href: String
)
