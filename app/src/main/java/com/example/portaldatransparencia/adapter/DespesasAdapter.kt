package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.DadoDespesas

class DespesasAdapter: RecyclerView.Adapter<DespesasAdapter.DespesasViewHolder>() {

    private var data: List<DadoDespesas> = listOf()

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
             //when(view){ itemView -> listener.clickDeputado(data[position].id.toString()) }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(despesa: DadoDespesas){

            itemView.run {
                val date = findViewById<TextView>(R.id.text_date)
                val typeDoc = findViewById<TextView>(R.id.text_type_doc)
                val fornecedor = findViewById<TextView>(R.id.text_nome_fornecedor)
                val tipoDoc = findViewById<TextView>(R.id.text_destination)
                val valor = findViewById<TextView>(R.id.text_valor_nota)
                date.text = despesa.dataDocumento
                typeDoc.text =
                    if (despesa.tipoDespesa == null) "Não informado tipo de documento"
                    else despesa.tipoDespesa.toString()
                fornecedor.text = despesa.nomeFornecedor
                tipoDoc.text =
                    if (despesa.tipoDocumento == null) "Não informado tipo de documento"
                    else despesa.tipoDocumento.toString()
                valor.text = "R$ ${despesa.valorDocumento}"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<DadoDespesas>) {
        data = deputados
        notifyDataSetChanged()
    }
}