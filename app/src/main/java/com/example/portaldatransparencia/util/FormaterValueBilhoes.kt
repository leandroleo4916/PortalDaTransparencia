package com.example.portaldatransparencia.util

class FormaterValueBilhoes {

    fun formatValor(valor: Double): String {
        val s = valor.toInt().toString()
        val ss = valor.toString()

        return when (s.length){
            0 -> {
                val value = ss.split(".")
                if (value[1].length != 1){
                    "R$ ${value[0]},${value[1]}"
                }else {
                    "R$ ${value[0]},${value[1]}0"
                }
            }
            1 -> {
                val value = ss.split(".")
                if (value[1].length != 1){
                    "R$ ${value[0]},${value[1]}"
                }else {
                    "R$ ${value[0]},${value[1]}0"
                }
            }
            2 -> {
                val value = ss.split(".")
                if (value[1].length != 1){
                    "R$ ${value[0]},${value[1]}"
                }else {
                    "R$ ${value[0]},${value[1]}0"
                }
            }
            3 -> {
                val value = ss.split(".")
                if (value[1].length != 1){
                    "R$ ${value[0]},${value[1]}"
                }else {
                    "R$ ${value[0]},${value[1]}0"
                }
            }
            4 -> {
                val value1 = s.substring(0, 1)
                val value2 = s.substring(1, 4)
                "R$ $value1.$value2,00"
            }
            5 -> {
                val value1 = s.substring(0, 2)
                val value2 = s.substring(2, 5)
                "R$ $value1.$value2,00"
            }
            6 -> {
                val value1 = s.substring(0, 3)
                "R$ $value1 mil"
            }
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