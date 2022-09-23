package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.interfaces.IClickDeputado
import com.example.portaldatransparencia.interfaces.INotification
import java.util.*

class MainAdapter(private val listener: IClickDeputado, private val notify: INotification):
    RecyclerView.Adapter<MainAdapter.MainViewHolder>(), Filterable {

    private var data = mutableListOf<Dado>()
    private var dataList = mutableListOf<Dado>()
    private var filter: ListItemFilter = ListItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_main, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = dataList[position]
        holder.bind(time)
    }

    override fun getItemCount() = dataList.size

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when (view) {
                itemView -> listener.clickDeputado(dataList[position].id.toString())
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(deputado: Dado) {
            itemView.run {
                val image = findViewById<ImageView>(R.id.icon_deputado)
                Glide.with(context)
                    .load(deputado.urlFoto)
                    .circleCrop()
                    .into(image)
                findViewById<TextView>(R.id.text_name).text = deputado.nome
                findViewById<TextView>(R.id.text_partido).text = deputado.siglaPartido
                findViewById<TextView>(R.id.text_state).text = " - ${deputado.siglaUf}"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<Dado>) {
        data = deputados as MutableList<Dado>
        dataList = deputados
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter { return filter }

    private inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterResults = FilterResults()
            if (constraint != null) {
                val list = ArrayList<Dado>()

                for (deputado in data) {
                    if (deputado.nome.lowercase(Locale.ROOT).contains(constraint.toString()
                            .lowercase(Locale.ROOT)) || deputado.siglaPartido.contains(constraint)) {
                        list.add(deputado)
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
                dataList = results.values as MutableList<Dado>
                if (dataList.isEmpty()){ notify.notification() }
            }
            notifyDataSetChanged()
        }
    }
}