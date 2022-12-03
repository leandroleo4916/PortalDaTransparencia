package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerMainBinding
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.interfaces.INotificationSenado
import kotlinx.coroutines.*
import java.util.*

class SenadoAdapter(private val listener: IClickSenador, private val notify: INotificationSenado,
                    private val context: Context):
    RecyclerView.Adapter<SenadoAdapter.MainViewHolder>(), Filterable {

    private var binding: RecyclerMainBinding? = null
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

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(senador: Parlamentar) {
            val item = senador.identificacaoParlamentar
            val https = "https:/"
            val urlFoto = item.urlFotoParlamentar.split(":/")
            val photo = https+urlFoto[1]

            binding = RecyclerMainBinding.bind(itemView)
            binding?.run {
                Glide.with(itemView)
                    .load(photo)
                    .circleCrop()
                    .into(iconDeputado)
                textName.text = item.nomeParlamentar
                textPartido.text = item.siglaPartidoParlamentar
                textState.text = " - ${item.ufParlamentar}"

                var value = 0
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        while (value <= 20) {
                            withContext(Dispatchers.Main) {
                                progressList.progress = value
                            }
                            delay(5)
                            value++
                        }
                    }
                }
                constraintDeputado.setOnClickListener {
                    val animFade = AnimationUtils.loadAnimation(context, R.anim.click)
                    it.startAnimation(animFade)
                    listener.clickSenador(
                        dataList[adapterPosition].identificacaoParlamentar.codigoParlamentar,
                        dataList[adapterPosition].identificacaoParlamentar.nomeParlamentar)
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
                    if (senadores.identificacaoParlamentar.nomeParlamentar.contains(constraint) ||
                        senadores.identificacaoParlamentar.siglaPartidoParlamentar.contains(constraint) ||
                        senadores.identificacaoParlamentar.ufParlamentar.contains(constraint)) {
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