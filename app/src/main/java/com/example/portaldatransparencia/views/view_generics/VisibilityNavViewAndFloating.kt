package com.example.portaldatransparencia.views.view_generics

import android.content.Context
import android.view.View
import com.example.portaldatransparencia.interfaces.IHideViewController
import com.google.android.material.appbar.AppBarLayout

class VisibilityNavViewAndFloating {

    private lateinit var viewController: IHideViewController

    fun showTabView(appBar: AppBarLayout, context: Context, view: View, anime: AnimationView) {
        var isShow = true
        var scrollRange = -1
        appBar.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) { scrollRange = barLayout?.totalScrollRange!! }
            if (scrollRange + verticalOffset == 0) {
                visibilityNavViewAndFloating(context,false, view, anime)
                isShow = true
            }
            else if (isShow) {
                visibilityNavViewAndFloating(context,true, view, anime)
                isShow = false
            }
        }
    }

    fun visibilityNavViewAndFloating(context: Context, value: Boolean, view: View, anime: AnimationView){
        if (context is IHideViewController) { viewController = context }
        viewController.hideNavView(value)
        anime.crossFade(view)
    }

}