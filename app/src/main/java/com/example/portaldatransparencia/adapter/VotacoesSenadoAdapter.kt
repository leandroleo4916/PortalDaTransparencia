package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListSenadoBinding
import com.example.portaldatransparencia.dataclass.VotacaoSenadoItem
import com.example.portaldatransparencia.interfaces.IAddVotoInRecycler

class VotacoesSenadoAdapter (private val clickSeeVotos: IAddVotoInRecycler):
    RecyclerView.Adapter<VotacoesSenadoAdapter.VotacoesViewHolder>() {

    private var binding: RecyclerVotacoesListSenadoBinding? = null
    private var data: ArrayList<VotacaoSenadoItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotacoesViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_votacoes_list_senado, parent, false)
        return VotacoesViewHolder(item)
    }

    override fun onBindViewHolder(holder: VotacoesViewHolder, position: Int) {
        val item = data.reversed()[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    inner class VotacoesViewHolder(private val itemViewE: View): RecyclerView.ViewHolder(itemViewE){

        fun bind(votacao: VotacaoSenadoItem){

            binding = RecyclerVotacoesListSenadoBinding.bind(itemViewE)
            binding?.run {
                votacao.run {
                    val date = dataSessao.split("-")
                    (date[2] + "/" + date[1] + "/" + date[0]).also { textDateVotacao.text = it }

                    textComissao.text = descricaoIdentificacaoMateria ?: "Não informado descrição"
                    textPropostaDescription.text = descricaoVotacao
                    var sim = 0
                    var nao = 0
                    var abs = 0

                    when (resultado) {
                        "A" -> {
                            textUltimaProposta.text = "Aprovada"
                            iconCheck.setImageResource(R.drawable.ic_check_green)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal)
                        }
                        "R" -> {
                            textUltimaProposta.text = "Rejeitada"
                            iconCheck.setImageResource(R.drawable.ic_close)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal_red)
                        }
                        else -> {
                            textUltimaProposta.text = "Em tramitação"
                            iconCheck.setImageResource(R.drawable.ic_atent)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal_yellow)
                        }
                    }
                    when (secreta){
                        "S" -> {
                            textSim.text = totalVotosSim.run {
                                if (this == "0" || this == "1") "$this voto - Sim"
                                else "$this votos - Sim"
                            }
                            textNao.text = totalVotosNao.run {
                                if (this == "0" || this == "1") "$this voto - Não"
                                else "$this votos - Não"
                            }
                            textAbstencao.text = totalVotosAbstencao.run {
                                if (this == "0" || this == "1") "$this voto - Abstenção"
                                else "$this votos - Abstenção"
                            }

                            textDescriptionVotacao.text = "Votação secreta: Sim"
                            verVotoSenado.text = "Esta votação foi secreta"
                            iconRight.visibility = View.GONE
                        }
                        "N" -> {
                            textDescriptionVotacao.text = "Votação secreta: Não"
                            verVotoSenado.text = "Ver votos dos Senadores"
                            iconRight.visibility = View.VISIBLE

                            votos.votoParlamentar.forEach {
                                when (it.voto){
                                    "Sim" -> sim += 1
                                    "Não" -> nao += 1
                                    else -> abs += 1
                                }
                            }

                            textSim.text =
                                if (sim == 0 || sim == 1) "$sim voto - Sim"
                                else "$sim votos - Sim"
                            textNao.text =
                                if (nao == 0 || nao == 1) "$nao voto - Não"
                                else "$nao votos - Não"
                            textAbstencao.text =
                                if (abs == 0 || abs == 1) "$abs voto - Abstenção"
                                else "$abs votos - Abstenção"

                            linearVerVoto.setOnClickListener {
                                it.startAnimation(AnimationUtils
                                    .loadAnimation(itemViewE.context, R.anim.click))
                                if (verVotoSenado.text == "Ver votos dos Senadores") {
                                    clickSeeVotos.addVoto(votos.votoParlamentar,
                                        votacao.descricaoIdentificacaoMateria)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: ArrayList<VotacaoSenadoItem>) {
        data = front
        notifyDataSetChanged()
    }
}