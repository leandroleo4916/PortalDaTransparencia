package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesBinding
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListBinding
import com.example.portaldatransparencia.dataclass.VotacaoId

class VotacoesCamaraAdapter(private val context: Context):
    RecyclerView.Adapter<VotacoesCamaraAdapter.VotacoesViewHolder>() {

    private var binding: RecyclerVotacoesListBinding? = null
    private var data: List<VotacaoId> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotacoesViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_votacoes_list, parent, false)
        return VotacoesViewHolder(item)
    }

    override fun onBindViewHolder(holder: VotacoesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class VotacoesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(votacao: VotacaoId){
            binding = RecyclerVotacoesListBinding.bind(itemView)
            binding?.run {
                votacao.run {
                    val date = dataHoraRegistro.split("-")
                    (date[1] + "/" + date[0]).also { textDateVotacao.text = it }

                    textComissao.text = siglaOrgao
                    textId.text = id
                    when (descricao.substring(0, 5)) {
                        "Aprov" -> {
                            iconCheck.setImageResource(R.drawable.ic_check_green)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal)
                        }
                        "Rejei" -> {
                            iconCheck.setImageResource(R.drawable.ic_close)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal_red)
                        }
                        else -> {
                            iconCheck.setImageResource(R.drawable.ic_atent)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal_yellow)
                        }
                    }
                    textDescriptionMateria.text = descricao
                }
                viewShowVotos.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                }
                viewShowVideo.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                }
                viewShowDetail.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: List<VotacaoId>) {
        data = data+front
        notifyDataSetChanged()
    }
}