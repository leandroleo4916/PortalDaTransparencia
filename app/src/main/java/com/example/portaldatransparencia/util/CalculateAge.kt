package com.example.portaldatransparencia.util

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class CalculateAge {
    fun age(birth: String): Int {
        val date = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val dateCurrent = LocalDate.now()
        val periodo = Period.between(date, dateCurrent)
        return periodo.years
    }
}