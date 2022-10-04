package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Votacao

class VotacoesAdapter : RecyclerView.Adapter<VotacoesAdapter.PropostaViewHolder>() {

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
            itemView.run {
                val iconCheck = findViewById<ImageView>(R.id.icon_check)
                val textAprovacao = findViewById<TextView>(R.id.text_aprovacao)
                val textSessao = findViewById<TextView>(R.id.text_sessao)
                val textDate = findViewById<TextView>(R.id.text_date_votacao)
                val textMateria = findViewById<TextView>(R.id.text_description_materia)
                val textTramitacao = findViewById<TextView>(R.id.text_description_tramitacao)
                val votoSenador = findViewById<TextView>(R.id.text_voto)
                val iconCheckVoto = findViewById<ImageView>(R.id.icon_check_voto)
                val iconMateria = findViewById<ImageView>(R.id.icon_materia)
                val iconTramite = findViewById<ImageView>(R.id.icon_tramite)
                val verMateria = findViewById<TextView>(R.id.text_ver_mais_materia)
                val verTramite = findViewById<TextView>(R.id.text_ver_mais_tramite)

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
                        (date[2] + "/" + date[1] + "/" + date[0]).also { textDate.text = it }
                    }
                    textMateria.text =
                        if (materia.ementa != "") {
                            if (materia.ementa.length > 100){
                                materia.ementa.substring(0, 99)+"..."
                            }
                            else materia.ementa
                        }
                        else "Descrição não registrada"

                    if (tramitacao != null) {
                        val tramite = tramitacao.identificacaoTramitacao?.textoTramitacao
                        textTramitacao.text = if (tramite!!.isNotEmpty() &&
                            tramite.length > 100){
                            tramite.substring(0, 99)+"..."
                        }
                        else tramite
                    }

                    when (siglaDescricaoVoto) {
                        "Sim" -> {
                            votoSenador.text = "Voto do senador foi - $siglaDescricaoVoto"
                            iconCheckVoto.setImageResource(R.drawable.ic_check_green)
                        }
                        "Não" -> {
                            votoSenador.text = "Voto do senador foi - $siglaDescricaoVoto"
                            iconCheckVoto.setImageResource(R.drawable.ic_close)
                        }
                        "P-NRV" -> {
                            votoSenador.text = "Presente, mas não votou"
                            iconCheckVoto.setImageResource(R.drawable.ic_atent)
                        }
                        "AP" -> {
                            votoSenador.text = "Atividade parlamentar"
                            iconCheckVoto.setImageResource(R.drawable.ic_atent)
                        }
                        "Votou" -> {
                            votoSenador.text = "Votou, mas este voto foi sigiloso"
                            iconCheckVoto.setImageResource(R.drawable.ic_check_green)
                        }
                        else -> {
                            votoSenador.text = "Não registrado ou não compareceu"
                            iconCheckVoto.setImageResource(R.drawable.ic_close)
                        }
                    }
                    verMateria.setOnClickListener {
                        if (textMateria.length() <= 103){
                            textMateria.text = materia.ementa
                            verMateria.text = "ver menos"
                            iconMateria.setImageResource(R.drawable.ic_up)
                        }
                        else {
                            textMateria.text = materia.ementa.substring(0, 100)+"..."
                            verMateria.text = "ver mais"
                            iconMateria.setImageResource(R.drawable.ic_down)
                        }
                    }
                    verTramite.setOnClickListener {
                        if (textTramitacao.length() <= 103){
                            textTramitacao.text = tramitacao?.identificacaoTramitacao?.textoTramitacao
                            verTramite.text = "ver menos"
                            iconTramite.setImageResource(R.drawable.ic_up)
                        }
                        else {
                            textTramitacao.text =
                                tramitacao?.identificacaoTramitacao?.textoTramitacao!!.substring(0, 100)+"..."
                            verTramite.text = "ver mais"
                            iconTramite.setImageResource(R.drawable.ic_down)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(votacoes: List<Votacao>) {
        data = votacoes as ArrayList<Votacao>
        notifyDataSetChanged()
    }
}