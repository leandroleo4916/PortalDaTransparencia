package com.example.portaldatransparencia.views.view_generics

import android.view.View
import kotlinx.coroutines.*

class AddValueViewGraph {

    fun addHeightToView(info: Int, view: View){
        val layoutParams = view.layoutParams
        var value = 1
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= info*1.8) {
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
}