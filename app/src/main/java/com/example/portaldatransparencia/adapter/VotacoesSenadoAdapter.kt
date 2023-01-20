package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListSenadoBinding
import com.example.portaldatransparencia.dataclass.VotacaoSenadoItem
import com.example.portaldatransparencia.dataclass.VotoParlamentar

class VotacoesSenadoAdapter (private val context: Context,
                             private val adapterVoto: VotacoesSenadoVotoAdapter,
                             private var recyclerSim: RecyclerView,
                             private var recyclerNao: RecyclerView,
                             private var recyclerAbs: RecyclerView):
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
                    textDescriptionVotacao.text = "Votação secreta: $secreta"
                    textPropostaDescription.text = descricaoVotacao

                    when (resultado) {
                        "A" -> {
                            iconCheck.setImageResource(R.drawable.ic_check_green)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal)
                        }
                        "R" -> {
                            iconCheck.setImageResource(R.drawable.ic_close)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal_red)
                        }
                        else -> {
                            iconCheck.setImageResource(R.drawable.ic_atent)
                            constraintLateral.setBackgroundResource(R.drawable.back_teal_yellow)
                        }
                    }
                    when (secreta){
                        "S" -> {
                            textSim.visibility = View.GONE
                            recyclerSim.visibility = View.GONE
                            textNao.visibility = View.GONE
                            recyclerNao.visibility = View.GONE
                            textAbstencao.text =
                                "$totalVotosSim - Sim, $totalVotosNao - Não, $totalVotosAbstencao - Abstenção"

                            recyclerAbs.layoutManager = LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false)
                            recyclerAbs.adapter = adapterVoto
                            adapterVoto.updateData(
                                votacao.votos.votoParlamentar as ArrayList<VotoParlamentar>)
                        }
                        "N" -> {
                            recyclerSim.layoutManager = LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false)
                            recyclerSim.adapter = adapterVoto
                            adapterVoto.updateData(
                                votacao.votos.votoParlamentar as ArrayList<VotoParlamentar>)

                            recyclerNao.layoutManager = LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false)
                            recyclerNao.adapter = adapterVoto
                            adapterVoto.updateData(votacao.votos.votoParlamentar)

                            recyclerAbs.layoutManager = LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false)
                            recyclerAbs.adapter = adapterVoto
                            adapterVoto.updateData(votacao.votos.votoParlamentar)
                        }
                        else -> {}
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