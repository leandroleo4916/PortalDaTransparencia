package com.example.portaldatransparencia.dataclass

data class GastoGeralCamara(
    val gastoGeral: GeralCamara
)

data class GeralCamara (
    val totalGeral: String,
    val totalNotas: String,
    val manutencao: String,
    val combustivel: String,
    val passagens: String,
    val divulgacao: String,
    val telefonia: String,
    val servicos: String,
    val alimentacao: String,
    val outros: String,
    val listDeputado: List<ListParlamentar>
)
