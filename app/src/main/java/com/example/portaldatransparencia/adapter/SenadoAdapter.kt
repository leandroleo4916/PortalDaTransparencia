package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.interfaces.INotificationSenado
import kotlinx.coroutines.*
import java.util.*

class SenadoAdapter(private val listener: IClickSenador, private val notify: INotificationSenado):
    RecyclerView.Adapter<SenadoAdapter.MainViewHolder>(), Filterable {

    private var data = mutableListOf<Parlamentar>()
    private var dataList = mutableListOf<Parlamentar>()
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
                itemView -> listener.clickSenador(
                    dataList[position].identificacaoParlamentar.codigoParlamentar,
                    dataList[position].identificacaoParlamentar.nomeParlamentar)
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(senador: Parlamentar) {
            val item = senador.identificacaoParlamentar
            val https = "https:/"
            val urlFoto = item.urlFotoParlamentar.split(":/")
            val photo = https+urlFoto[1]
            itemView.run {
                val progress= findViewById<ProgressBar>(R.id.progress_list)
                val image = findViewById<ImageView>(R.id.icon_deputado)
                Glide.with(context)
                    .load(photo)
                    .circleCrop()
                    .into(image)
                findViewById<TextView>(R.id.text_name).text = item.nomeParlamentar
                findViewById<TextView>(R.id.text_partido).text = item.siglaPartidoParlamentar
                findViewById<TextView>(R.id.text_state).text = " - ${item.ufParlamentar}"

                var value = 0
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        while (value <= 20) {
                            withContext(Dispatchers.Main) {
                                progress.progress = value
                            }
                            delay(5)
                            value++
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(senadores: List<Parlamentar>) {
        data = senadores as MutableList<Parlamentar>
        dataList = senadores
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter { return filter }

    private inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterResults = FilterResults()
            if (constraint != null) {
                val list = ArrayList<Parlamentar>()

                for (senadores in data) {
                    if (senadores.identificacaoParlamentar.nomeParlamentar.lowercase(Locale.ROOT)
                            .contains(constraint.toString().lowercase(Locale.ROOT)) ||
                        senadores.identificacaoParlamentar.siglaPartidoParlamentar.contains(constraint)) {
                        list.add(senadores)
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
                dataList = results.values as MutableList<Parlamentar>
                if (dataList.isEmpty()){ notify.notification() }
            }
            notifyDataSetChanged()
        }
    }
}