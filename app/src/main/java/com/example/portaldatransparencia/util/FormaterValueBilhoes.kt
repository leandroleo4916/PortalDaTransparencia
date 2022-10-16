package com.example.portaldatransparencia.util

class FormaterValueBilhoes {

    fun formatValor(valor: Double): String {
        val s = valor.toInt().toString()

        return when (s.length){
            7 -> {
                val sub1 = s.substring(0, 1)
                val sub2 = s.substring(1, 3)
                "R$ $sub1,$sub2 mi"
            }
            8 -> {
                val sub1 = s.substring(0, 2)
                val sub2 = s.substring(2, 4)
                "R$ $sub1,$sub2 mi"
            }
            9 -> {
                val sub1 = s.substring(0, 3)
                val sub2 = s.substring(3, 5)
                "R$ $sub1,$sub2 mi"
            }
            10 -> {
                val sub1 = s.substring(0, 1)
                val sub2 = s.substring(1, 3)
                "R$ $sub1,$sub2 bi"
            }
            else -> {
                val sub1 = s.substring(0, 3)
                val sub2 = s.substring(3, 5)
                "R$ $sub1,$sub2 bi"
            }
        }
    }
}