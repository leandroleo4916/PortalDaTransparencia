package com.example.portaldatransparencia.util

import java.text.Normalizer
import java.util.regex.Pattern

class RetiraAcento {

    fun deleteAccent(str: String): String{
        var ret = ""
        val lower = str.lowercase()
        val nfdNormalizedString: String = Normalizer.normalize(lower, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        val text = pattern.matcher(nfdNormalizedString).replaceAll("")
        val nome = text.split(" ")
        nome.forEach{
            ret += it
        }
        return ret
    }
}