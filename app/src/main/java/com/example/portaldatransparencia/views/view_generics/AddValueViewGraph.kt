package com.example.portaldatransparencia.views.view_generics

import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*

class AddValueViewGraph {

    fun addHeightToView(info: Int, view: View){
        val multi = info * 2
        val layoutParams = view.layoutParams
        var value = 1
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= multi) {
                    withContext(Dispatchers.Main) {
                        layoutParams.height = value
                        view.layoutParams = layoutParams
                    }
                    delay(1)
                    value += 1
                }
            }
        }
    }

    fun addValueToText(info: Int, textView: TextView){
        var value = 1F
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {

                while (value <= info){
                    withContext(Dispatchers.Main){
                        textView.text = "$ ${value.toInt()} mi"
                    }
                    delay(1)
                    value += 0.5F
                }
            }
        }
    }
}