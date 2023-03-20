package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGastosBinding
import com.example.portaldatransparencia.dataclass.GastosSenador
import com.example.portaldatransparencia.interfaces.INotification
import com.example.portaldatransparencia.util.FormaterValueBilhoes

class GastoSenadorAdapter(private val formatValor: FormaterValueBilhoes,
                          private val notify: INotification):
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
                val dateDoc = despesa.data.split("-")
                (dateDoc[2]+"/"+dateDoc[1]+"/"+dateDoc[0]).also { textDate.text = it }
                despesa.tipoDespesa.let { textDestination.text = it }
                textTypeDoc.text =
                    (if (despesa.detalhamento == "Não foi informado" || despesa.detalhamento == "null" )
                        despesa.tipoDespesa
                    else despesa.detalhamento).toString()
                despesa.fornecedor.let { textNomeFornecedor.text = it }
                despesa.cnpjCpf.let {
                    textCnpjFornecedor.text = (if (it.length == 14) "CNPJ: $it" else "CPF: $it").toString()
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
        if (senador.isEmpty()) {
            data = arrayListOf()
            dataSelected = arrayListOf()
        }
        else {
            data = senador as ArrayList<GastosSenador>
            dataSelected = senador
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun openDataSelect(type: String) {
        if (type != "Todos") {
            data = arrayListOf()
            val noteSelect: ArrayList<GastosSenador> = arrayListOf()
            dataSelected.forEach {
                if (it.tipoDespesa.contains(type)) noteSelect.add(it)
            }
            if (noteSelect.isNotEmpty()) data = noteSelect
            else notify.notification()
        }
        else data = dataSelected
        notifyDataSetChanged()
    }
}