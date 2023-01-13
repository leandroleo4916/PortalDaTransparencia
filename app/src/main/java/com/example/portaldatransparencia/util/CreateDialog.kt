package com.example.portaldatransparencia.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.createDialog(): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(true)
    return builder
}
