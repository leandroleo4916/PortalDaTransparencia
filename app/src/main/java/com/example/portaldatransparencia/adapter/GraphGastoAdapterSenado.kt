package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGraphGastoBinding
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.util.RetValueFloatOrInt
import com.example.portaldatransparencia.views.view_generics.AddValueViewGraph

class GraphGastoAdapterSenado(private val addValue: AddValueViewGraph,
                              private val retValueInt: RetValueFloatOrInt):
    RecyclerView.Adapter<GraphGastoAdapterSenado.DespesasViewHolder>() {

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
        when (anoSelect) {
            "Todos" -> holder.bind(data[position])
            "2023" -> holder.bind2(data[position])
            else -> holder.bind3(data[position])
        }
    }

    override fun getItemCount() = data.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            binding?.run {
                val ret = retValueInt.formatValor(item.value)
                addValue.addHeightMultiToView(ret, 4.8F, viewValue1)
                addValue.addValueToTextSenado1(ret, textItemTop)
                textItem1.text = item.description
            }
        }

        fun bind2(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            val valueInt = item.value.toInt()
            binding?.run {
                if (valueInt < 2000){
                    if (valueInt < 1000) {
                        textItemTop.text = "$ -1 mil"
                        addLayoutParams(viewValue1)
                    }
                    else {
                        textItemTop.text = "$ +1 mil"
                        addLayoutParams(viewValue1)
                    }
                }
                else {
                    val value = retValueInt.formatValorToInt(item.value)
                    addValue.addHeightToViewSenado(value, 1.5F, viewValue1)
                    addValue.addValueToTextSenado2(value, textItemTop)
                }
                textItem1.text = item.description
            }
        }

        fun bind3(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            binding?.run {
                val ret = retValueInt.formatValor(item.value)
                if (item.value.toInt() < 1000000){
                    addLayoutParams(viewValue1)
                    textItemTop.text = "$ -1 mi"
                }
                else {
                    addValue.addHeightToView(ret, 0.2F, viewValue1)
                    addValue.addValueToText(ret, 0.2F, textItemTop)
                }
                textItem1.text = item.description
            }
        }

        private fun addLayoutParams(view: View){
            val layoutParams = view.layoutParams
            layoutParams.height = 1
            view.layoutParams = layoutParams
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(front: ArrayList<AddInfoSetor>, ano: String) {
        data = front
        anoSelect = ano
        notifyDataSetChanged()
    }
}