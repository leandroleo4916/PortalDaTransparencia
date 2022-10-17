package com.example.portaldatransparencia.util

class FormatValueFloat {
    fun formatFloat(valor: String): Float{
        return if (valor.contains(",")){
            val value = valor.split(",")
            if (value[0].isNotEmpty()){
                value[0].toFloat()
            }
            else 0.1F
        }
        else {
            if (valor.isNotEmpty()) valor.toFloat()
            else  0.0F
        }
    }
}