package com.example.portaldatransparencia.views.view_generics

import android.content.Context
import androidx.appcompat.app.AlertDialog

class CreateDialogClass{
    fun createDialog(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        return builder
    }
}

