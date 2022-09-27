package com.example.portaldatransparencia.dataclass

data class Despesas (
    val dados: List<DadoDespesas>,
    val links: List<Link>
)

data class DadoDespesas (
    val ano: Long,
    val mes: Long,
    val tipoDespesa: String,
    val codDocumento: Long,
    val tipoDocumento: String,
    val codTipoDocumento: Long,
    val dataDocumento: String?,
    val numDocumento: String,
    val valorDocumento: Double,
    val urlDocumento: String?,
    val nomeFornecedor: String,
    val cnpjCpfFornecedor: String,
    val valorLiquido: Double,
    val valorGlosa: Double,
    val numRessarcimento: String,
    val codLote: Long,
    val parcela: Long
)
