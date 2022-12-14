package com.example.portaldatransparencia.dataclass

data class GastoGeralDataClass(
    val gastoGeral: GastoGeral
)

data class GastoGeral (
    val totalGeral: String,
    val totalNotas: String,
    val aluguel: String,
    val divulgacao: String,
    val passagens: String,
    val contratacao: String,
    val locomocao: String,
    val aquisicao: String,
    val outros: String,
    val listSenador: List<ListParlamentar>
)

data class ListParlamentar (
    var id: String,
    var partido: String,
    var uf: String,
    val nome: String,
    val gasto: String,
    var urlFoto: String? = null
)


