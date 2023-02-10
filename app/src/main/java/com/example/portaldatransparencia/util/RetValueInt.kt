package com.example.portaldatransparencia.util

class RetValueInt {

    fun formatValor(valor: String): Int {
        return when (valor.length) {
            1 -> 2
            2 -> 2
            3 -> 2
            4 -> 2
            5 -> 2
            6 -> 2
            7 -> valor.substring(0,1).toInt()
            8 -> valor.substring(0,2).toInt()
            else -> valor.substring(0,3).toInt()
        }
    }
}