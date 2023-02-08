package com.example.portaldatransparencia.dataclass

data class GastoGeralCamara(
    val total: Total
)

data class Total (
    val totalGeral: String,
    val totalNotas: String,
    val manutencao: String,
    val combustivel: String,
    val passagens: String,
    val assinatura: String,
    val divulgacao: String,
    val telefonia: String,
    val postais: String,
    val hospedagem: String,
    val taxi: String,
    val locacao: String,
    val consultoria: String,
    val seguranca: String,
    val curso: String,
    val alimentacao: String,
    val outros: String
)
