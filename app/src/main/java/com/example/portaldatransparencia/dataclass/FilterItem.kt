package com.example.portaldatransparencia.dataclass

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import com.example.portaldatransparencia.R
import com.google.android.material.R.*
import com.google.android.material.chip.Chip

data class FilterItem(
    val id: Int? = null,
    val text: String,
    val select: Int? = null
)

fun FilterItem.toChip(context: Context): Chip{

    val chip = if (select == null){
        LayoutInflater.from(context).inflate(R.layout.chip_select, null, false) as Chip
    } else {
        Chip(ContextThemeWrapper(context, style.Widget_MaterialComponents_Chip_Choice))
    }
    chip.text = text
    chip.textSize = 16F
    return chip
}
