package com.example.portaldatransparencia.util

class DaysOfMonth {
    fun month (month: String): List<String>{
        val list = arrayListOf<String>()
        when(month){
            "02" -> {
                list.add("01-02-2023")
                list.add("28-02-2023")
            }
            "03" -> {
                list.add("01-03-2023")
                list.add("31-03-2023")
            }
            "04" -> {
                list.add("01-04-2023")
                list.add("30-04-2023")
            }
            "05" -> {
                list.add("01-05-2023")
                list.add("31-05-2023")
            }
            "06" -> {
                list.add("01-06-2023")
                list.add("30-06-2023")
            }
            "07" -> {
                list.add("01-07-2023")
                list.add("31-07-2023")
            }
            "08" -> {
                list.add("01-08-2023")
                list.add("31-08-2023")
            }
            "09" -> {
                list.add("01-09-2023")
                list.add("30-09-2023")
            }
            "10" -> {
                list.add("01-10-2023")
                list.add("31-10-2023")
            }
            "11" -> {
                list.add("01-11-2023")
                list.add("30-11-2023")
            }
            "12" -> {
                list.add("01-12-2023")
                list.add("31-12-2023")
            }
            else -> {
                list.add("01-02-2023")
                list.add("31-12-2023")
            }
        }
        return list
    }
}