package com.example.portaldatransparencia.views.view_generics

import android.view.View
import android.widget.TextView
import com.example.portaldatransparencia.util.ConverterValueNotes
import kotlinx.coroutines.*

class AddValueViewGraph(private val format: ConverterValueNotes) {

    fun addHeightToView(info: Float, div: Float, view: View){
        val result = info / div
        val layoutParams = view.layoutParams
        var value = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= result) {
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

    fun addHeightMultiToView(info: Float, multi: Float, view: View){
        val result = info * multi
        val layoutParams = view.layoutParams
        var value = 1
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= result) {
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

    fun addValueToText(info: Float, multi: Float, textView: TextView){
        var value = 1F
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= info){
                    withContext(Dispatchers.Main){
                        textView.text = "$ +${value.toInt()} mi"
                    }
                    delay(1)
                    value += multi
                }
            }
        }
    }

    fun addValueMultiToTextMi(info: Int, div: Int, textView: TextView){
        val res = info / div
        var value = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= info){
                    withContext(Dispatchers.Main){
                        if (value < 1000000) textView.text = "$ +$value mil"
                        else {
                            val valueFormat = format.formatString(value.toString())
                            textView.text = valueFormat
                        }
                    }
                    delay(1)
                    value += res
                }
            }
        }
    }

    fun addHeightToViewMil(info: Float, div: Float, view: View){
        var value = 1
        val result = info / div
        val layoutParams = view.layoutParams
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value <= result) {
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