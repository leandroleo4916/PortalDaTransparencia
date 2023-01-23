package com.example.portaldatransparencia.views.view_generics

class ModifyHttpToHttps {
    fun modifyUrl(url: String): String {
        val urlFoto = url.split(":")
        return "https:${urlFoto[1]}"
    }
}