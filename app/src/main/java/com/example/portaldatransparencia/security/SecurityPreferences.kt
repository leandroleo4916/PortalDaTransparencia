package com.example.portaldatransparencia.security

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "id", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String{
        return sharedPreferences.getString(key, "").toString()
    }
}