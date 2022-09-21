package com.example.portaldatransparencia.interfaces

import android.view.View
import com.google.android.material.snackbar.Snackbar

interface IHideViewController {
    fun hideNavView (value: Boolean)
    fun snackBar(child: View, snackBar: Snackbar.SnackbarLayout)
}