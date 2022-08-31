package com.example.portaldatransparencia.dataclass

data class IdDeputadoDataClass(
    val dados: Dados,
    val links: List<Link>
)

data class Dados (
    val cpf: String,
    val dataFalecimento: String,
    val dataNascimento: String,
    val escolaridade: String,
    val id: Long,
    val municipioNascimento: String,
    val nomeCivil: String,
    val redeSocial: List<String>,
    val sexo: String,
    val ufNascimento: String,
    val ultimoStatus: UltimoStatus,
    val uri: String,
    val urlWebsite: String
)

data class UltimoStatus (
    val condicaoEleitoral: String,
    val data: String,
    val descricaoStatus: String,
    val email: String,
    val gabinete: Gabinete,
    val id: Long,
    val idLegislatura: Long,
    val nome: String,
    val nomeEleitoral: String,
    val siglaPartido: String,
    val siglaUf: String,
    val situacao: String,
    val uri: String,
    val uriPartido: String,
    val urlFoto: String
)

data class Gabinete (
    val andar: String,
    val email: String,
    val nome: String,
    val predio: String,
    val sala: String,
    val telefone: String
)

