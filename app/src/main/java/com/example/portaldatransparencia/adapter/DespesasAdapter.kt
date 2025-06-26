package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.util.FormaterValueBilhoes

class DespesasAdapter(private val listener: INoteDespesas,
                      private val formatValor: FormaterValueBilhoes):
    RecyclerView.Adapter<DespesasAdapter.DespesasViewHolder>(), Filterable {

    private var binding: RecyclerGastosBinding? = null
    private var data = mutableListOf<DadoDespesas>()
    private var dataFilter = mutableListOf<DadoDespesas>()
    private var filter: ListItemFilter = ListItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_gastos, parent, false)
        return DespesasViewHolder(item)
    }

    override fun onBindViewHolder(holder: DespesasViewHolder, position: Int) {
        val despesa = dataFilter[position]
        holder.bind(despesa)
    }

    override fun getItemCount() = dataFilter.size

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(despesa: DadoDespesas){

            binding = RecyclerGastosBinding.bind(itemView)
            binding?.run {
                despesa.run {
                    if (dataDocumento != null){
                        val dateDoc = dataDocumento.split("-")
                        val day = dateDoc[2].split("T")
                        (day[0]+"/"+dateDoc[1]+"/"+dateDoc[0]).also { textDate.text = it }
                    }
                    tipoDespesa.let { textTypeDoc.text = it }
                    nomeFornecedor.let { textNomeFornecedor.text = it }
                    tipoDocumento.let { textDestination.text = it }

                    textCnpjFornecedor.text =
                        if (cnpjCpfFornecedor.isNotEmpty()) {
                            if (cnpjCpfFornecedor.length == 11)
                                "CPF: $cnpjCpfFornecedor"
                            else "CNPJ: $cnpjCpfFornecedor"
                        }
                        else "CNPJ ou CPF não informado"

                    valorDocumento.let{
                        textValorNota.text = formatValor.formatValor(it)
                    }
                }
                itemView.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    listener.listenerDespesas(despesa.urlDocumento)
                }
            }
        }
    }

    override fun getFilter(): Filter = filter

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<DadoDespesas>, page: Int) {
        if (page == 1) {
            data = arrayListOf()
            dataFilter = arrayListOf()
        }
        data += deputados
        dataFilter += deputados
        notifyDataSetChanged()
    }

    private inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterResults = FilterResults()
            if (constraint != null) {
                var list = ArrayList<DadoDespesas>()
                when (constraint){
                    "TOTAL GERAL" -> list = data as ArrayList<DadoDespesas>
                    else ->
                        for (type in data) {
                            if (type.tipoDespesa.contains(constraint)) {
                            list.add(type)
                        }
                    }
                }
                filterResults.count = list.size
                filterResults.values = list

            } else {
                filterResults.count = data.size
                filterResults.values = data
            }
            return filterResults
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(p0: CharSequence?, results: FilterResults) {
            if (results.values is ArrayList<*>) {
                dataFilter = results.values as ArrayList<DadoDespesas>
            }
            notifyDataSetChanged()
        }
    }
}