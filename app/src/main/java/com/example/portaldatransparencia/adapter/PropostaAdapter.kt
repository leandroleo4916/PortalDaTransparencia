package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerPropostaBinding
import com.example.portaldatransparencia.dataclass.Proposta
import com.example.portaldatransparencia.interfaces.IClickItemProposta
import com.example.portaldatransparencia.views.deputado.proposta_deputado.FragmentProposta

class PropostaAdapter(private val context: Context?, private val clickProposta: IClickItemProposta):
    RecyclerView.Adapter<PropostaAdapter.PropostaViewHolder>() {

    private var binding: RecyclerPropostaBinding? = null
    private var data: ArrayList<Proposta> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropostaViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_proposta, parent, false)
        return PropostaViewHolder(item)
    }

    override fun onBindViewHolder(holder: PropostaViewHolder, position: Int) {
        val front = data[position]
        holder.bind(front)
    }

    override fun getItemCount() = data.size

    inner class PropostaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(proposta: Proposta){
            binding = RecyclerPropostaBinding.bind(itemView)
            binding?.run {
                textDescriptionProposta.text =
                    (proposta.ementa ?: R.string.nao_informado_descricao) as CharSequence?
                constraintDeputado.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                    clickProposta.clickProposta(proposta.id.toString())
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(proposta: List<Proposta>, page: Int) {
        if (page == 1) { data = arrayListOf() }
        proposta.forEach { data.add(it) }
        notifyDataSetChanged()
    }
}