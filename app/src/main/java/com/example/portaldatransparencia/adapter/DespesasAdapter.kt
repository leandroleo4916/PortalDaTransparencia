package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.views.view_generics.FormatValor

class DespesasAdapter(private val listener: INoteDespesas, private val formatValor: FormatValor):
    RecyclerView.Adapter<DespesasAdapter.DespesasViewHolder>() {

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

    inner class DespesasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when(view){ itemView -> listener.listenerDespesas(data[position]) }
        }

        fun bind(despesa: DadoDespesas){

            itemView.run {
                val date = findViewById<TextView>(R.id.text_date)
                val typeDespesa = findViewById<TextView>(R.id.text_type_doc)
                val fornecedor = findViewById<TextView>(R.id.text_nome_fornecedor)
                val typeDoc = findViewById<TextView>(R.id.text_destination)
                val valor = findViewById<TextView>(R.id.text_valor_nota)
                val cnpj = findViewById<TextView>(R.id.text_cnpj_fornecedor)

                if (despesa.dataDocumento != null){
                    val dateDoc = despesa.dataDocumento.split("-")
                    (dateDoc[2]+"/"+dateDoc[1]+"/"+dateDoc[0]).also { date.text = it }
                }
                despesa.tipoDespesa.let { typeDespesa.text = it }
                despesa.nomeFornecedor.let { fornecedor.text = it }
                despesa.tipoDocumento.let { typeDoc.text = it }
                despesa.cnpjCpfFornecedor.let { cnpj.text = it }
                despesa.valorDocumento.let{
                    val formatTotal = formatValor.formatValor(it)
                    valor.text = "R$ $formatTotal"
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