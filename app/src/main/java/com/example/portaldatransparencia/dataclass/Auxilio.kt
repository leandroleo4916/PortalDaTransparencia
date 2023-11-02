package com.example.portaldatransparencia.dataclass

data class Auxilio (
    val auxilio: AuxilioSenado
)

data class AuxilioSenado (
    val nomeParlamentar: String,
    val estadoEleito: String,
    val partidoEleito: String,
    val auxilioMoradia: Boolean,
    val imovelFuncional: Boolean
)
