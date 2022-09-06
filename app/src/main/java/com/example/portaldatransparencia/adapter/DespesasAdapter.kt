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
        val time = data[position]
        holder.bind(time)
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
                findViewById<TextView>(R.id.text_date).text = despesa.dataDocumento
                findViewById<TextView>(R.id.text_type_doc).text = despesa.tipoDocumento.toString()
                findViewById<TextView>(R.id.text_nome_fornecedor).text = despesa.nomeFornecedor
                findViewById<TextView>(R.id.text_destination).text = despesa.tipoDocumento.toString()
                findViewById<TextView>(R.id.text_valor_nota).text = "R$ ${despesa.valorDocumento}"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<DadoDespesas>) {
        data = deputados
        notifyDataSetChanged()
    }
}