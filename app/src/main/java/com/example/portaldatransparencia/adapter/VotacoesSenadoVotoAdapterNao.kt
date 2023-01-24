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
import com.example.portaldatransparencia.databinding.RecyclerVotoSenadoBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.views.view_generics.ModifyHttpToHttps

class VotacoesSenadoVotoAdapterNao(private val click: IClickSenador):
    RecyclerView.Adapter<VotacoesSenadoVotoAdapterNao.VotacoesViewHolder>() {

    private var binding: RecyclerVotoSenadoBinding? = null
    private var data: List<AddVoto> = listOf()

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

    inner class VotacoesViewHolder(private val itemViewE: View): RecyclerView.ViewHolder(itemViewE){

        fun bind(votacao: AddVoto){
            binding = RecyclerVotoSenadoBinding.bind(itemViewE)
            binding?.run {
                votacao.run {
                    binding.run {
                        votacao.foto.circleCrop().into(iconImage)
                        textNameVoto.text = nome
                    }
                }
                constraintVotoSenador.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemViewE.context, R.anim.click))
                    click.clickSenador(votacao.id, votacao.nome)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: ArrayList<AddVoto>) {
        data = front
        notifyDataSetChanged()
    }
}