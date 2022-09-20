package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.DadoFrente
import com.example.portaldatransparencia.interfaces.IFront

class FrenteAdapter(private val listener: IFront): RecyclerView.Adapter<FrenteAdapter.DespesasViewHolder>() {

    private var data: List<DadoFrente> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_frente, parent, false)
        return DespesasViewHolder(item)
    }

    override fun onBindViewHolder(holder: DespesasViewHolder, position: Int) {
        val front = data[position]
        holder.bind(front)
    }

    override fun getItemCount() = data.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when(view){ itemView -> listener.listenerFront(data[position]) }
        }

        fun bind(front: DadoFrente){
            itemView.run {
                val description = findViewById<TextView>(R.id.text_description_title)
                description.text = front.titulo
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: List<DadoFrente>) {
        data = front
        notifyDataSetChanged()
    }
}