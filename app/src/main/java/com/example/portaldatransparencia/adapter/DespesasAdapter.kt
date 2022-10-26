package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.util.FormatValor

class DespesasAdapter(private val listener: INoteDespesas, private val formatValor: FormatValor):
    RecyclerView.Adapter<DespesasAdapter.DespesasViewHolder>() {

    private var binding: RecyclerGastosBinding? = null
    private var data: ArrayList<DadoDespesas> = arrayListOf()

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

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(despesa: DadoDespesas){

            binding = RecyclerGastosBinding.bind(itemView)
            binding?.run {
                if (despesa.dataDocumento != null){
                    val dateDoc = despesa.dataDocumento.split("-")
                    (dateDoc[2]+"/"+dateDoc[1]+"/"+dateDoc[0]).also { textDate.text = it }
                }
                despesa.tipoDespesa.let { textTypeDoc.text = it }
                despesa.nomeFornecedor.let { textNomeFornecedor.text = it }
                despesa.tipoDocumento.let { textDestination.text = it }
                despesa.cnpjCpfFornecedor.let { textCnpjFornecedor.text = it }
                despesa.valorDocumento.let{
                    val formatTotal = formatValor.formatValor(it)
                    textValorNota.text = "R$ $formatTotal"
                }
                itemView.setOnClickListener {
                    listener.listenerDespesas(data[adapterPosition])
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<DadoDespesas>, page: Int) {
        if (page == 1) { data = arrayListOf() }
        deputados.forEach { data.add(it) }
        notifyDataSetChanged()
    }
}