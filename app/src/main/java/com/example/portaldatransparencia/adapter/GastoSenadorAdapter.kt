package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGastosBinding
import com.example.portaldatransparencia.dataclass.GastosSenador
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.util.FormaterValueBilhoes

class GastoSenadorAdapter(private val formatValor: FormaterValueBilhoes):
    RecyclerView.Adapter<GastoSenadorAdapter.DespesasViewHolder>() {

    private var data: ArrayList<GastosSenador> = arrayListOf()
    private var dataSelected: ArrayList<GastosSenador> = arrayListOf()
    private var binding: RecyclerGastosBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_gastos, parent, false)
        return DespesasViewHolder(item)
    }

    override fun onBindViewHolder(holder: DespesasViewHolder, position: Int) {
        val despesa = data[position]
        holder.bind(despesa)
    }

    override fun getItemCount() = data.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(despesa: GastosSenador){

            binding = RecyclerGastosBinding.bind(itemView)
            binding?.run {
                textDate.text = despesa.data
                despesa.tipoDespesa.let { textDestination.text = it }
                textTypeDoc.text =
                    (if (despesa.detalhamento == "Não foi informado") despesa.tipoDespesa
                    else despesa.detalhamento).toString()
                despesa.fornecedor.let { textNomeFornecedor.text = it }
                despesa.cnpjCpf.let {
                    textCnpjFornecedor.text = (if (it.length == 14) "CPF: $it" else "CNPJ: $it").toString()
                }
                despesa.valorReembolsado.let{
                    if (it.contains(",")){
                        val value = it.split(",")
                        if (value[0].isNotEmpty()){
                            textValorNota.text = formatValor.formatValor(value[0].toDouble())
                        }
                        else textValorNota.text = "R$ 0,01"
                    }
                    else {
                        if (it.isNotEmpty()) {
                            textValorNota.text = formatValor.formatValor(it.toDouble())
                        }
                        else textValorNota.text = "Valor não informado"
                    }
                }
                iconShare.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSenador(senador: List<GastosSenador>) {
        data = if (senador.isEmpty()) arrayListOf()
        else senador as ArrayList<GastosSenador>

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun openDataSelect(type: String) {
        if (type != "") {
            data = arrayListOf()
            val noteSelect: ArrayList<GastosSenador> = arrayListOf()
            dataSelected.forEach {
                if (it.tipoDespesa.contains(type)) noteSelect.add(it)
            }
            if (noteSelect.isNotEmpty()) data = noteSelect
        }
        else data = dataSelected
        notifyDataSetChanged()
    }
}