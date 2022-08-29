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
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.dataclass.MainDataClass

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var data = ArrayList<MainDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_main, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = data[0].dados[position]
        holder.bind(time)
    }

    override fun getItemCount() = data[0].dados.size

    inner class MainViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when(view){ } //itemView -> listener.clickRecycler(id, cidade) }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(deputado: Dado){
            itemView.run {
                val image = findViewById<ImageView>(R.id.icon_deputado)
                Glide.with(context).load(deputado.urlFoto).into(image)
                findViewById<TextView>(R.id.text_name).text = deputado.nome
                findViewById<TextView>(R.id.text_partido).text = deputado.siglaPartido
                val imagePartido = findViewById<ImageView>(R.id.icon_partido)
                Glide.with(context).load(deputado.uriPartido).into(imagePartido)
                findViewById<TextView>(R.id.text_email).text = deputado.email.toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputado: MainDataClass) {
        data.add(deputado)
        notifyDataSetChanged()
    }
}