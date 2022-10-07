package com.example.portaldatransparencia.views.view_generics

class FormatValor {

    fun formatValor(valor: Double): String {
        val s = valor.toInt().toString()
        val ss = s.length
        return if (ss <= 3){
            "$s,00"
        }
        else if (ss == 4 || ss == 5 || ss == 6){
            val tres = s.substring(ss-3, ss)
            val dois = s.substring(0, ss-3)
            "$dois.$tres,00"
        }
        else {
            val quat = s.substring(ss-3, ss)
            val tres = s.substring(ss-6, ss-3)
            val dois = s.substring(0, ss-6)
            "$dois.$tres.$quat,00"
        }
    }
}