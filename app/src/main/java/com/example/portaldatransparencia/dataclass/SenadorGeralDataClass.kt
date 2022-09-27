package com.example.portaldatransparencia.dataclass


data class SenadorGastosDataClass(
    val gastosSenador: List<GastosSenador>
)

data class GastosSenador (
    val ano: String,
    val mes: String,
    val senador: String,
    val tipoDespesa: String,
    val cnpjCpf: String,
    val fornecedor: String,
    val documento: String,
    val data: String,
    val detalhamento: String,
    val valorReembolsado: String,
    val codDocumento: String
)


