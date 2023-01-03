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
import com.example.portaldatransparencia.dataclass.Votacao
import com.example.portaldatransparencia.interfaces.ISmoothPosition

class VotacoesAdapter(private val smooth: ISmoothPosition, private val context: Context):
    RecyclerView.Adapter<VotacoesAdapter.PropostaViewHolder>() {

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
        holder.bind(front, position)
    }

    override fun getItemCount() = data.size

    inner class PropostaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(votacao: Votacao, position: Int) {

            val animFade = AnimationUtils.loadAnimation(context, R.anim.click_votacao)
            binding = RecyclerVotacoesBinding.bind(itemView)
            binding?.run {
                votacao.run {
                    if (descricaoResultado == "Rejeitado") {
                        textAprovacao.text = descricaoResultado
                        iconCheck.setImageResource(R.drawable.ic_close)
                        constraint2.setBackgroundResource(R.drawable.back_teal_red)
                    } else {
                        textAprovacao.text = descricaoResultado
                        iconCheck.setImageResource(R.drawable.ic_check_green)
                        constraint2.setBackgroundResource(R.drawable.back_teal)
                    }
                    """Sessão: ${sessaoPlenaria.codigoSessao} | Votação secreta: $indicadorVotacaoSecreta""".trimMargin()
                        .also { textSessao.text = it }
                    if (sessaoPlenaria.dataSessao != null) {
                        val date = sessaoPlenaria.dataSessao.split("-")
                        (date[2] + "/" + date[1] + "/" + date[0]).also { textDateVotacao.text = it }
                    }

                    if (materia.ementa != null) {
                        if (materia.ementa.length >= 100) {
                            textDescriptionMateria.text = materia.ementa.substring(0, 99) + "..."
                        }
                        else {
                            textDescriptionMateria.text = materia.ementa
                            iconMateria.visibility = View.GONE
                            textVerMaisMateria.visibility = View.GONE
                        }
                    } else {
                        textDescriptionMateria.text = "Descrição não registrada"
                        iconMateria.visibility = View.GONE
                        textVerMaisMateria.visibility = View.GONE
                    }

                    if (tramitacao != null) {
                        val tramite = tramitacao.identificacaoTramitacao?.textoTramitacao
                        if (tramite!!.isNotEmpty() && tramite.length >= 100) {
                            textDescriptionTramitacao.text = tramite.substring(0, 99) + "..."
                        } else {
                            textDescriptionTramitacao.text = tramite
                            iconTramite.visibility = View.GONE
                            textVerMaisTramite.visibility = View.GONE
                        }
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
                        val getText = textDescriptionMateria.text
                        if (getText.contains("...")) {
                            textDescriptionMateria.text = materia.ementa
                            textVerMaisMateria.text = "ver menos"
                            iconMateria.setImageResource(R.drawable.ic_up)
                        }
                        else {
                            ((materia.ementa?.substring(0, 100) ?: "Sem descrição") + "...")
                                .also { textDescriptionMateria.text = it }
                            textVerMaisMateria.text = "ver mais"
                            iconMateria.setImageResource(R.drawable.ic_down)
                        }
                        smooth.smoothPosition(position)
                        itemView.startAnimation(animFade)
                    }
                    textVerMaisTramite.setOnClickListener {
                        val getText = textDescriptionTramitacao.text
                        val text = tramitacao?.identificacaoTramitacao?.textoTramitacao
                        if (getText != null && getText.contains("...")) {
                            if (text != null) textDescriptionTramitacao.text = text
                            textVerMaisTramite.text = "ver menos"
                            iconTramite.setImageResource(R.drawable.ic_up)
                        }
                        else {
                            if (text != null) {
                                textDescriptionTramitacao.text = text.substring(0, 99) + "..."
                            }
                            textVerMaisTramite.text = "ver mais"
                            iconTramite.setImageResource(R.drawable.ic_down)
                        }
                        smooth.smoothPosition(position)
                        itemView.startAnimation(animFade)
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