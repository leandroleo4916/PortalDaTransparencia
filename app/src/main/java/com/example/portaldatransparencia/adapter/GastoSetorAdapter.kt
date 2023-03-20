package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGastoSetorBinding
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.interfaces.ISmoothPosition
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.AnimationView

class GastoSetorAdapter(private val formatValor: FormaterValueBilhoes,
                        private val animeView: AnimationView):
    RecyclerView.Adapter<GastoSetorAdapter.DespesasViewHolder>() {

    private var binding: RecyclerGastoSetorBinding? = null
    private var data: ArrayList<AddInfoSetor> = arrayListOf()
    private var value = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_gasto_setor, parent, false)
        return DespesasViewHolder(item)
    }

    override fun onBindViewHolder(holder: DespesasViewHolder, position: Int) {
        val despesa = data[position]
        holder.bind(despesa)
    }

    override fun getItemCount() = data.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(despesa: AddInfoSetor){

            binding = RecyclerGastoSetorBinding.bind(itemView)
            binding?.run {
                textDescriptionSetor.text = despesa.description
                textValueSetor.text = formatValor.formatValor(despesa.value.toDouble())
                viewLateral.setBackgroundResource(despesa.color)

                Glide.with(itemView)
                    .load(despesa.photo)
                    .into(iconGasto)
            }
            animeView.crossFade(itemView, true)
            itemView.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(subList: ArrayList<AddInfoSetor>) {
        data = subList
        notifyDataSetChanged()
    }
}