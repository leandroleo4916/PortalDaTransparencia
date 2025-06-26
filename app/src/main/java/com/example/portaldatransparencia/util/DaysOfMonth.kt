package com.example.portaldatransparencia.util

class DaysOfMonth {
    fun month (month: String): List<String>{
        val list = arrayListOf<String>()
        when(month){
            "02" -> {
                list.add("01-02-2025")
                list.add("28-02-2025")
            }
            "03" -> {
                list.add("01-03-2025")
                list.add("31-03-2025")
            }
            "04" -> {
                list.add("01-04-2025")
                list.add("30-04-2025")
            }
            "05" -> {
                list.add("01-05-2025")
                list.add("31-05-2025")
            }
            "06" -> {
                list.add("01-06-2025")
                list.add("30-06-2025")
            }
            "07" -> {
                list.add("01-07-2025")
                list.add("31-07-2025")
            }
            "08" -> {
                list.add("01-08-2025")
                list.add("31-08-2025")
            }
            "09" -> {
                list.add("01-09-2025")
                list.add("30-09-2025")
            }
            "10" -> {
                list.add("01-10-2025")
                list.add("31-10-2025")
            }
            "11" -> {
                list.add("01-11-2025")
                list.add("30-11-2025")
            }
            "12" -> {
                list.add("01-12-2025")
                list.add("31-12-2025")
            }
            else -> {
                list.add("01-02-2025")
                list.add("31-12-2025")
            }
        }
        return list
    }
}