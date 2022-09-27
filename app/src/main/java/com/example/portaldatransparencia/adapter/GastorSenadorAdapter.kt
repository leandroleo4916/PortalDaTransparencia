package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.GastosSenador
import java.text.DecimalFormat

class GastorSenadorAdapter: RecyclerView.Adapter<GastorSenadorAdapter.DespesasViewHolder>() {

    private var data: ArrayList<GastosSenador> = arrayListOf()

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

            itemView.run {
                val date = findViewById<TextView>(R.id.text_date)
                val typeDespesa = findViewById<TextView>(R.id.text_type_doc)
                val fornecedor = findViewById<TextView>(R.id.text_nome_fornecedor)
                val typeDoc = findViewById<TextView>(R.id.text_destination)
                val valor = findViewById<TextView>(R.id.text_valor_nota)

                date.text = despesa.data
                despesa.tipoDespesa.let { typeDespesa.text = it }
                despesa.fornecedor.let { fornecedor.text = it }
                despesa.tipoDespesa.let { typeDoc.text = it }
                despesa.valorReembolsado.let{
                    if (it.contains(",")){
                        val value = it.split(",")
                        val format = DecimalFormat("#.00")
                        val formatTotal = format.format(value[0].toFloat())
                        valor.text = "R$ $formatTotal"
                    }
                    else {
                        val format = DecimalFormat("#.00")
                        val formatTotal = format.format(it.toFloat())
                        valor.text = "R$ $formatTotal"
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSenador(senador: List<GastosSenador>) {
        data = senador as ArrayList<GastosSenador>
        notifyDataSetChanged()
    }
}