package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerDimensionBinding
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.util.FormatValor

class DimensionAdapter(private val formatValor: FormatValor):
    RecyclerView.Adapter<DimensionAdapter.DespesasViewHolder>() {

    private var binding: RecyclerDimensionBinding? = null
    private var data: ArrayList<SublistDataClass> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_dimension, parent, false)
        return DespesasViewHolder(item)
    }

    override fun onBindViewHolder(holder: DespesasViewHolder, position: Int) {
        val despesa = data[position]
        holder.bind(despesa)
    }

    override fun getItemCount() = data.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(despesa: SublistDataClass){

            binding = RecyclerDimensionBinding.bind(itemView)
            binding?.run {
                textViewDescription.text = despesa.description
                textViewGasto.text = "R$ ${formatValor.formatValor(despesa.value.toDouble())}"
                constraintRecycler.setBackgroundResource(despesa.color)
                Glide.with(itemView)
                    .load(despesa.icon)
                    .circleCrop()
                    .into(icon)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(subList: ArrayList<SublistDataClass>) {
        data = subList
        notifyDataSetChanged()
    }
}