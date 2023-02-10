package com.example.portaldatransparencia.util

class RetValueInt {

    fun formatValor(valor: String): Int {
        return when (valor.length) {
            1 -> 1
            2 -> 1
            3 -> 1
            4 -> 1
            5 -> 1
            6 -> 1
            7 -> valor.substring(0,1).toInt()
            8 -> valor.substring(0,2).toInt()
            else -> valor.substring(0,3).toInt()
        }
    }
}