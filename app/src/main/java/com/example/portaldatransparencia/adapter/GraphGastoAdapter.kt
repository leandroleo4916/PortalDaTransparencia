package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGraphGastoBinding
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.util.RetValueInt
import com.example.portaldatransparencia.views.view_generics.AddValueViewGraph

class GraphGastoAdapter(private val addValue: AddValueViewGraph,
                        private val retValueInt: RetValueInt):
    RecyclerView.Adapter<GraphGastoAdapter.DespesasViewHolder>() {

    private var binding: RecyclerGraphGastoBinding? = null
    private var data: List<AddInfoSetor> = listOf()
    private var anoSelect = "Todos"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_graph_gasto, parent, false)
        return DespesasViewHolder(item)
    }

    override fun onBindViewHolder(holder: DespesasViewHolder, position: Int) {
        if (anoSelect == "Todos") holder.bind(data[position])
        else holder.bind2(data[position])
    }

    override fun getItemCount() = data.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            binding?.run {
                val ret = retValueInt.formatValor(item.value)
                addValue.addHeightToView(ret, 1.5F, viewValue1)
                addValue.addValueToText(ret, 1.5f, textItemTop)
                textItem1.text = item.description
            }
        }

        fun bind2(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            binding?.run {
                val ret = retValueInt.formatValor(item.value)
                addValue.addHeightToView(ret, 1.5F, viewValue1)
                addValue.addValueToText(ret, 1.5f, textItemTop)
                textItem1.text = item.description
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: ArrayList<AddInfoSetor>, ano: String) {
        data = front
        anoSelect = ano
        notifyDataSetChanged()
    }
}