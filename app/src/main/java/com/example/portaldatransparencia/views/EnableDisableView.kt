package com.example.portaldatransparencia.views

import android.view.View
import android.widget.TextView
import com.wang.avi.AVLoadingIndicatorView

class EnableDisableView {
    fun enableProgress(view: AVLoadingIndicatorView){
       view.visibility = View.VISIBLE
    }
    fun disableProgress(view: AVLoadingIndicatorView){
        view.visibility = View.GONE
    }
    fun enableView(view: View){
        view.visibility = View.VISIBLE
    }
    fun disableView(view: View){
        view.visibility = View.GONE
    }
}