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
    fun transformIntToString(valor: Int): String{
        val value = valor.toString()
        when (value.length) {
            5 -> {
                val v1 = value.substring(0, 2)
                val v2 = value.substring(2, 5)
                return "R$ $v1.$v2,00"
            }
            6 -> {
                val v1 = value.substring(0, 3)
                val v2 = value.substring(3, 6)
                return "R$ $v1.$v2,00"
            }
            else -> {
                val v1 = value.substring(0, 1)
                val v2 = value.substring(1, 4)
                val v3 = value.substring(4, 7)
                return "R$ $v1.$v2.$v3,00"
            }
        }
    }
}