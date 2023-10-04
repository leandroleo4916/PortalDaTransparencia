package com.example.portaldatransparencia.views.view_generics

import android.animation.ValueAnimator
import androidx.constraintlayout.widget.ConstraintLayout

class AnimatorViewParlamentar {

    fun animatorView(view: ConstraintLayout){
        val animator =
            ValueAnimator.ofInt(0, (110 * view.resources.displayMetrics.density).toInt())
        animator.duration = 1000

        animator.addUpdateListener { animation ->
            val larguraAtual = animation.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = larguraAtual
            view.layoutParams = layoutParams
        }

        animator.start()
    }
}