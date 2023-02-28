package com.example.portaldatransparencia.views.view_generics

import android.view.View

class AnimationView {
    fun crossFade(view: View, visible: Boolean) {
        val shortAnimationDuration = 300
        view.apply {
            alpha = 0F
            visibility =
                if (visible) View.VISIBLE
                else View.GONE

            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }
}