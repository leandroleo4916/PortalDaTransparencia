package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListSenadoBinding
import com.example.portaldatransparencia.dataclass.VotacaoSenadoItem
import com.example.portaldatransparencia.dataclass.VotoParlamentar
import com.example.portaldatransparencia.interfaces.IAddVotoInRecycler


class VotacoesSenadoAdapter (private val context: Context):
    RecyclerView.Adapter<VotacoesSenadoAdapter.VotacoesViewHolder>() {

    private var binding: RecyclerVotacoesListSenadoBinding? = null
    private var data: List<VotacaoSenadoItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotacoesViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_votacoes_list_senado, parent, false)
        return VotacoesViewHolder(item)
    }

    override fun onBindViewHolder(holder: VotacoesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class VotacoesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(votacao: VotacaoSenadoItem){
            binding = RecyclerVotacoesListSenadoBinding.bind(itemView)
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
                            val listSim: ArrayList<VotoParlamentar> = arrayListOf()
                            val listNao: ArrayList<VotoParlamentar> = arrayListOf()
                            val listAbs: ArrayList<VotoParlamentar> = arrayListOf()

                            votacao.votos.votoParlamentar.forEach {
                                when (it.voto){
                                    "Sim" -> {
                                        sim += 1
                                        listSim.add(it)
                                    }
                                    "Não" -> {
                                        nao += 1
                                        listNao.add(it)
                                    }
                                    else -> {
                                        abs += 1
                                        listAbs.add(it)
                                    }
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
                                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
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