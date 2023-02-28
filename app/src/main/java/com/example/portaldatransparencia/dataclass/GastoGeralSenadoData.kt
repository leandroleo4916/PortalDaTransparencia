package com.example.portaldatransparencia.dataclass

data class GastoGeralSenadoData (
    val gastoGeral: GastoGeralSenado
)

data class GastoGeralSenado (
    val total: String,
    val notas: String,
    val aluguel: String,
    val divulgacao: String,
    val contratacao: String,
    val locomocao: String,
    val passagens: String,
    val aquisicao: String,
    val outros: String
)
