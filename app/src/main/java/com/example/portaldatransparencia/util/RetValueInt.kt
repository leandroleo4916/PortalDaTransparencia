package com.example.portaldatransparencia.util

class RetValueInt {

    fun formatValorToInt(valor: String): Float {
        return when (valor.length) {
            4 -> valor.substring(0,1).toFloat()
            5 -> valor.substring(0,2).toFloat()
            else -> valor.substring(0,3).toFloat()
        }
    }

    fun formatValor(valor: String): Float {
        return when (valor.length) {
            7 -> valor.substring(0,2).toFloat() / 10
            8 -> valor.substring(0,3).toFloat() / 10
            else -> valor.substring(0,4).toFloat() / 10
        }
    }
}