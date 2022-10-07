package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.ListSenador
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.views.view_generics.FormatValor
import java.util.*
import kotlin.collections.ArrayList

class GastoGeralAdapter(private val formatValor: FormatValor) : RecyclerView.Adapter<GastoGeralAdapter.MainViewHolder>() {

    private var data = arrayListOf<ListSenador>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_rancking, parent, false)

        return MainViewHolder(item)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time)
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when (view) { }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: ListSenador) {
            val https = "https:/"
            //val urlFoto = item.urlFoto.split(":/")
            //val photo = https+urlFoto[1]
            itemView.run {
                findViewById<TextView>(R.id.text_name_rancking).text = item.nome
                findViewById<TextView>(R.id.text_valor_item).text = "R$ ${formatValor.formatValor(item.gasto.toDouble())}"
                val medal = findViewById<ImageView>(R.id.image_medal)
                val image = findViewById<ImageView>(R.id.icon_image)
                Glide.with(context)
                    .load("")
                    .circleCrop()
                    .into(image)
                when (adapterPosition){
                    0 -> medal.setImageResource(R.drawable.ic_medal_ouro)
                    1 -> medal.setImageResource(R.drawable.ic_medal_prata)
                    2 -> medal.setImageResource(R.drawable.ic_medal_bronze)
                    else -> medal.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(senadores: List<ListSenador>) {
        data = senadores as ArrayList<ListSenador>
        notifyDataSetChanged()
    }
}