package com.example.portaldatransparencia.views.view_generics

import android.view.View

class AnimationView {
    fun crossFade(view: View) {
        val shortAnimationDuration = 300
        view.apply {
            alpha = 0F
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }
}