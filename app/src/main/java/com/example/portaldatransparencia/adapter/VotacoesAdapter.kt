package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerVotacoesBinding
import com.example.portaldatransparencia.dataclass.Votacao

class VotacoesAdapter : RecyclerView.Adapter<VotacoesAdapter.PropostaViewHolder>() {

    private var binding: RecyclerVotacoesBinding? = null
    private var data: ArrayList<Votacao> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropostaViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_votacoes, parent, false)
        return PropostaViewHolder(item)
    }

    override fun onBindViewHolder(holder: PropostaViewHolder, position: Int) {
        val front = data[position]
        holder.bind(front)
    }

    override fun getItemCount() = data.size

    inner class PropostaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(votacao: Votacao) {

            binding = RecyclerVotacoesBinding.bind(itemView)
            binding?.run {
                votacao.run {
                    if (descricaoResultado == "Rejeitado") {
                        textAprovacao.text = descricaoResultado
                        iconCheck.setImageResource(R.drawable.ic_close)
                    } else {
                        textAprovacao.text = descricaoResultado
                        iconCheck.setImageResource(R.drawable.ic_check_green)
                    }
                    """Sessão: ${sessaoPlenaria.codigoSessao} | Votação secreta: $indicadorVotacaoSecreta""".trimMargin()
                        .also { textSessao.text = it }
                    if (sessaoPlenaria.dataSessao != null) {
                        val date = sessaoPlenaria.dataSessao.split("-")
                        (date[2] + "/" + date[1] + "/" + date[0]).also { textDateVotacao.text = it }
                    }
                    textDescriptionMateria.text =
                        if (materia.ementa != null) {
                            if (materia.ementa.length > 100) {
                                materia.ementa.substring(0, 99) + "..."
                            } else materia.ementa
                        } else "Descrição não registrada"

                    if (tramitacao != null) {
                        val tramite = tramitacao.identificacaoTramitacao?.textoTramitacao
                        textDescriptionTramitacao.text =
                            if (tramite!!.isNotEmpty() && tramite.length > 100) {
                                tramite.substring(0, 99) + "..."
                            }
                            else tramite
                    }

                    when (siglaDescricaoVoto) {
                        "Sim" -> {
                            textVoto.text = "Voto do senador foi - $siglaDescricaoVoto"
                            iconCheckVoto.setImageResource(R.drawable.ic_check_green)
                        }
                        "Não" -> {
                            textVoto.text = "Voto do senador foi - $siglaDescricaoVoto"
                            iconCheckVoto.setImageResource(R.drawable.ic_close)
                        }
                        "P-NRV" -> {
                            textVoto.text = "Presente, mas não votou"
                            iconCheckVoto.setImageResource(R.drawable.ic_atent)
                        }
                        "AP" -> {
                            textVoto.text = "Atividade parlamentar"
                            iconCheckVoto.setImageResource(R.drawable.ic_atent)
                        }
                        "Votou" -> {
                            textVoto.text = "Votou, mas este voto foi sigiloso"
                            iconCheckVoto.setImageResource(R.drawable.ic_check_green)
                        }
                        else -> {
                            textVoto.text = "Não registrado ou não compareceu"
                            iconCheckVoto.setImageResource(R.drawable.ic_close)
                        }
                    }
                    textVerMaisMateria.setOnClickListener {
                        if (textDescriptionMateria.length() <= 103) {
                            textDescriptionMateria.text = materia.ementa
                            textVerMaisMateria.text = "ver menos"
                            iconMateria.setImageResource(R.drawable.ic_up)
                        } else {
                            ((materia.ementa?.substring(0, 100) ?: "Sem descrição") + "...")
                                .also { textDescriptionMateria.text = it }
                            textVerMaisMateria.text = "ver mais"
                            iconMateria.setImageResource(R.drawable.ic_down)
                        }
                    }
                    textVerMaisTramite.setOnClickListener {
                        if (textDescriptionTramitacao.length() <= 103) {
                            textDescriptionTramitacao.text =
                                tramitacao?.identificacaoTramitacao?.textoTramitacao
                            textVerMaisTramite.text = "ver menos"
                            iconTramite.setImageResource(R.drawable.ic_up)
                        } else {
                            (tramitacao?.identificacaoTramitacao?.textoTramitacao!!
                                .substring(0, 100) + "...").also { textDescriptionTramitacao.text = it }
                            textVerMaisTramite.text = "ver mais"
                            iconTramite.setImageResource(R.drawable.ic_down)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(votacoes: List<Votacao>) {
        data = if (votacoes.isEmpty()) arrayListOf()
        else votacoes as ArrayList<Votacao>
        notifyDataSetChanged()
    }
}