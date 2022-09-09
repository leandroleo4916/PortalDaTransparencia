package com.example.portaldatransparencia.views

import android.view.View
import com.wang.avi.AVLoadingIndicatorView

class ProgressBar {
    fun enableProgress(view: AVLoadingIndicatorView){
       view.visibility = View.VISIBLE
    }
    fun disableProgress(view: AVLoadingIndicatorView){
        view.visibility = View.GONE
    }
}