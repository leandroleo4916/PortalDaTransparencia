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
                if (item.value.toInt() < 1000000){
                    addLayoutParams(1, viewValue1)
                    textItemTop.text = "$ -1 mi"
                }else {
                    addValue.addHeightToView(ret, 2F, viewValue1)
                    if (ret < 10) addValue.addValueToText(ret, 1F, textItemTop)
                    else addValue.addValueToText(ret, 2F, textItemTop)
                    textItem1.text = item.description
                }
            }
        }

        fun bind2(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            val valueInt = item.value.toInt()
            binding?.run {
                if (valueInt < 1000000){
                    if (valueInt < 1000){
                        textItemTop.text = "$ -1 mil"
                        addLayoutParams(1, viewValue1)
                    }
                    else {
                        val ret = retValueInt.formatValorToInt(item.value)
                        if (ret in 1.0..99.9){
                            textItemTop.text = "$ +${ret.toInt()} mil"
                            if (ret <= 10) addLayoutParams(1, viewValue1)
                            else addValue.addHeightToViewMil(ret, 10F, viewValue1)
                        }
                        else {
                            addValue.addHeightToView(ret, 10F, viewValue1)
                            addValue.addValueToText(ret, 10F, textItemTop)
                        }
                    }
                }
                else {
                    val ret = retValueInt.formatValor(item.value)
                    addValue.addHeightMultiToView(ret, 66F, viewValue1)
                    addValue.addValueMultiToTextMi(valueInt, 70, textItemTop)
                }
                textItem1.text = item.description
            }
        }

        fun bind3(item: AddInfoSetor){
            binding = RecyclerGraphGastoBinding.bind(itemView)
            binding?.run {
                val ret = retValueInt.formatValor(item.value)
                if (item.value.toInt() < 1000000){
                    addLayoutParams(1, viewValue1)
                    textItemTop.text = "$ -1 mi"
                }
                else{
                    addValue.addHeightToView(ret, 0.2F, viewValue1)
                    addValue.addValueToText(ret, 0.2F, textItemTop)
                }
                textItem1.text = item.description
            }
        }

        private fun addLayoutParams(info: Int, view: View){
            val layoutParams = view.layoutParams
            layoutParams.height = info
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