package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListBinding
import com.example.portaldatransparencia.dataclass.VotacaoId
import com.example.portaldatransparencia.interfaces.IClickSeeDetails
import com.example.portaldatransparencia.interfaces.IClickSeeVideo
import com.example.portaldatransparencia.interfaces.IClickSeeVote

class VotacoesCamaraAdapter(private val clickVote: IClickSeeVote,
                            private val clickVideo: IClickSeeVideo,
                            private val clickDetail: IClickSeeDetails ):
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
                    val date = data.split("-")
                    (date[2] + "/" + date[1] + "/" + date[0]).also { textDateVotacao.text = it }

                    textComissao.text = siglaOrgao.ifEmpty { "Votação sem descrição" }
                    val text = ultimaAberturaVotacao.descricao
                    textDescriptionVotacao.text =
                        if (text.isEmpty()) "Não foi adicionado descrição"
                        else (if (text.length >= 120) text.substring(0..119)+"..."
                        else text ).toString()

                    when (descricao.substring(0, 5)) {
                        "Aprov" -> {
                            iconCheck.setImageResource(R.drawable.ic_check_green)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal)
                        }
                        "Aporv" -> {
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
                    val txt = ultimaApresentacaoProposicao.descricao
                    textUltimaPropostaDescription.text =
                        if (txt.isEmpty()) "Não foi adicionado última proposta"
                        else (if (txt.length >= 120) txt.substring(0..119)+"..."
                        else txt).toString()
                }
                viewShowVotos.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    clickVote.clickSeeVote(votacao)
                }
                viewShowVideo.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    clickVideo.clickSeeVideo(votacao)
                }
                viewShowDetail.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    clickDetail.clickSeeDetails(votacao)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: ArrayList<VotacaoId>) {
        data = front
        notifyDataSetChanged()
    }
}