package com.example.portaldatransparencia.views.view_generics

import android.view.View

class EnableDisableView {
    fun enableView(view: View){
        view.visibility = View.VISIBLE
    }
    fun disableView(view: View){
        view.visibility = View.GONE
    }
}