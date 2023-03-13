package com.example.portaldatransparencia.views.view_generics

import android.view.View
import android.widget.TextView
import com.example.portaldatransparencia.util.ConverterValueNotes
import kotlinx.coroutines.*

class AddValueViewGraph(private val format: ConverterValueNotes) {

    fun addHeightToView(info: Float, div: Float, view: View){
        val result = info / div
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

    fun addValueToTextSenado1(info: Float, textView: TextView){
        var value = 0F
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value < info + 0.1){
                    withContext(Dispatchers.Main){
                        textView.text = "$ +${value.toInt()} mi"
                    }
                    delay(0)
                    value += 0.2F
                }
            }
        }
    }

    fun addValueToTextSenado2(info: Int, textView: TextView){
        var value = 0F
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value < info + 1){
                    withContext(Dispatchers.Main){
                        textView.text = "$ +${value.toInt()} mil"
                    }
                    delay(0)
                    value += 1.6F
                }
            }
        }
    }

    fun addHeightToViewSenado(info: Int, div: Float, view: View){
        val resultDiv = info / div
        val layoutParams = view.layoutParams
        var value = 1
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (value < resultDiv) {
                    withContext(Dispatchers.Main) {
                        layoutParams.height = value
                        view.layoutParams = layoutParams
                    }
                    delay(0)
                    value += 1
                }
            }
        }
    }

    fun addValueMultiToTextMi(info: Int, div: Int, textView: TextView){
        val res = info / div
        var value = 1
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