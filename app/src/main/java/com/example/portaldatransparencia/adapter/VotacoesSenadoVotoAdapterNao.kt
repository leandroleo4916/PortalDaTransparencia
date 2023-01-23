package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListBinding
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListSenadoBinding
import com.example.portaldatransparencia.databinding.RecyclerVotoSenadoBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.di.modifyHttp
import com.example.portaldatransparencia.interfaces.IClickSeeDetails
import com.example.portaldatransparencia.interfaces.IClickSeeVideo
import com.example.portaldatransparencia.interfaces.IClickSeeVote
import com.example.portaldatransparencia.views.view_generics.ModifyHttpToHttps

class VotacoesSenadoVotoAdapterNao(private val context: Context, private val modifyHttp: ModifyHttpToHttps):
    RecyclerView.Adapter<VotacoesSenadoVotoAdapterNao.VotacoesViewHolder>() {

    private var binding: RecyclerVotoSenadoBinding? = null
    private var data: List<VotoParlamentar> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotacoesViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_voto_senado, parent, false)
        return VotacoesViewHolder(item)
    }

    override fun onBindViewHolder(holder: VotacoesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class VotacoesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(votacao: VotoParlamentar){
            binding = RecyclerVotoSenadoBinding.bind(itemView)
            binding?.run {
                votacao.run {
                    binding.run {
                        val urlFoto = modifyHttp.modifyUrl(votacao.foto)
                        Glide.with(context)
                            .load(urlFoto)
                            .circleCrop()
                            .into(iconImage)
                        textNameVoto.text = nomeParlamentar
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: ArrayList<VotoParlamentar>, adapterPosition: Int) {
        data = front
        notifyDataSetChanged()
    }
}