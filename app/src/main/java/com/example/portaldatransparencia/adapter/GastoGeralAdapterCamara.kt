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
import com.example.portaldatransparencia.dataclass.ListDeputado
import com.example.portaldatransparencia.util.FormatValor

class GastoGeralAdapterCamara(private val formatValor: FormatValor) :
    RecyclerView.Adapter<GastoGeralAdapterCamara.MainViewHolder>() {

    private var data = arrayListOf<ListDeputado>()

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

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: ListDeputado) {

            itemView.run {
                findViewById<TextView>(R.id.text_name_rancking).text = item.nome
                findViewById<TextView>(R.id.text_valor_item).text =
                    "R$ ${formatValor.formatValor(item.gasto.toDouble())}"
                val medal = findViewById<ImageView>(R.id.image_medal)
                val image = findViewById<ImageView>(R.id.icon_image)
                Glide.with(context)
                    .load(item.urlFoto)
                    .circleCrop()
                    .into(image)
                when (adapterPosition) {
                    0 -> medal.setImageResource(R.drawable.ic_medal_ouro)
                    1 -> medal.setImageResource(R.drawable.ic_medal_prata)
                    2 -> medal.setImageResource(R.drawable.ic_medal_bronze)
                    else -> medal.visibility = View.GONE
                }

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(senador: ArrayList<ListDeputado>) {
        data = senador
        notifyDataSetChanged()
    }
}