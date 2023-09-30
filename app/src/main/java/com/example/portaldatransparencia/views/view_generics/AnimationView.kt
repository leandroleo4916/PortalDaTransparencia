package com.example.portaldatransparencia.views.view_generics

import android.view.View

class AnimationView {
    fun crossFade(myView: View) {

        if (myView.visibility == View.VISIBLE) {
            // Se a view está visível, esconde suavemente
            myView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    myView.visibility = View.GONE
                }
        } else {
            // Se a view está invisível, mostra suavemente
            myView.alpha = 0f
            myView.visibility = View.VISIBLE
            myView.animate()
                .alpha(1f).duration = 300
        }
    }

    fun crossVisibleView(myView: View){
        myView.alpha = 0f
        myView.visibility = View.VISIBLE
        myView.animate()
            .alpha(1f).duration = 300
    }

    fun crossInvisibleView(myView: View){
        myView.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                myView.visibility = View.GONE
            }
    }
}