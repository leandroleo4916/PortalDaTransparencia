package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerFrenteBinding
import com.example.portaldatransparencia.dataclass.DadoFrente
import com.example.portaldatransparencia.interfaces.IFront

class FrenteAdapter(private val listener: IFront):
    RecyclerView.Adapter<FrenteAdapter.DespesasViewHolder>() {

    private var binding: RecyclerFrenteBinding? = null
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

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(front: DadoFrente){
            binding = RecyclerFrenteBinding.bind(itemView)
            binding?.run {
                textDescriptionTitle.text = front.titulo
                constraintDeputado.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    listener.listenerFront(data[adapterPosition])
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: List<DadoFrente>) {
        data = front
        notifyDataSetChanged()
    }
}