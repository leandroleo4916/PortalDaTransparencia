package com.example.portaldatransparencia.util

class ConverterValueNotes {
    fun formatString(valor: String): String{
        return when(valor.length) {
            5 -> "+${valor.substring(0,2)} mil"
            6 -> "+${valor.substring(0,3)} mil"
            7 -> "+${valor.substring(0,1)}.${valor.substring(2,3)} mi"
            else -> "+${valor.substring(0,2)}.${valor.substring(3,4)} mi"
        }
    }
}