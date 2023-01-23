package com.example.portaldatransparencia.dataclass

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder

data class AddVoto(
    val id: String,
    val nome: String,
    val foto: RequestBuilder<Drawable>
)
