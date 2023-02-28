package com.example.portaldatransparencia.dataclass

data class RankingDataClass (
    val ranking: List<Ranking>
)

data class Ranking (
    val id: String,
    val nome: String,
    val foto: String,
    val gasto: String,
    val partido: String,
    val estado: String
)
