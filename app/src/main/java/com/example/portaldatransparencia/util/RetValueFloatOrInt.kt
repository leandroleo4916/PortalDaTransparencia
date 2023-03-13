package com.example.portaldatransparencia.util

class RetValueFloatOrInt {

    fun formatValorToFloat(valor: String): Float {
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

    fun formatValorToInt(valor: String): Int {
        return when (valor.length) {
            3 -> 1
            4 -> valor.substring(0,1).toInt()
            5 -> valor.substring(0,2).toInt()
            6 -> valor.substring(0,3).toInt()
            7 -> valor.substring(0,4).toInt()
            8 -> valor.substring(0,5).toInt()
            9 -> valor.substring(0,6).toInt()
            else -> valor.substring(0,7).toInt()
        }
    }
}