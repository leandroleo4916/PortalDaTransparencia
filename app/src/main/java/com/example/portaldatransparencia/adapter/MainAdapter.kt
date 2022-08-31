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
import com.example.portaldatransparencia.interfaces.IClickDeputado

class MainAdapter(private val listener: IClickDeputado): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var data: List<Dado> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_main, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time)
    }

    override fun getItemCount() = data.size

    inner class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when(view){ itemView -> listener.clickDeputado(data[position].id.toString()) }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(deputado: Dado){
            itemView.run {
                val image = findViewById<ImageView>(R.id.icon_deputado)
                Glide.with(context)
                    .load(deputado.urlFoto)
                    .circleCrop()
                    .into(image)
                findViewById<TextView>(R.id.text_name).text = deputado.nome
                findViewById<TextView>(R.id.text_partido).text = deputado.siglaPartido
                findViewById<TextView>(R.id.text_state).text = deputado.siglaUf
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<Dado>) {
        data = deputados
        notifyDataSetChanged()
    }
}