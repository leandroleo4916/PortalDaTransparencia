package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerMainBinding
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.interfaces.IClickDeputado
import com.example.portaldatransparencia.interfaces.INotification
import kotlinx.coroutines.*

class MainAdapter(private val listener: IClickDeputado, private val notify: INotification):
    RecyclerView.Adapter<MainAdapter.MainViewHolder>(), Filterable {

    private var binding: RecyclerMainBinding? = null
    private var data = mutableListOf<Dado>()
    private var dataList = mutableListOf<Dado>()
    private var dataListState = mutableListOf<Dado>()
    private var filter: ListItemFilter = ListItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_main, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = dataList[position]
        holder.bind(time, position)
    }

    override fun getItemCount() = dataList.size

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(deputado: Dado, position: Int) {
            binding = RecyclerMainBinding.bind(itemView)
            binding?.run {
                Glide.with(itemView)
                    .load(deputado.urlFoto)
                    .circleCrop()
                    .into(iconDeputado)
                textName.text = deputado.nome
                textPartido.text = deputado.siglaPartido
                textState.text = " - ${deputado.siglaUf}"

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
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    listener.clickDeputado(dataList[position].id.toString())
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputados: List<Dado>) {
        data = deputados as MutableList<Dado>
        dataList = deputados
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(partido: String, estado: String){
        dataListState = mutableListOf()
        data.forEach {
            if (it.siglaPartido.contains(partido) && it.siglaUf.contains(estado)){
                dataListState.add(it)
            }
        }
        dataList = dataListState
        if (dataList.isEmpty()) notify.notification()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter { return filter }

    private inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterResults = FilterResults()
            if (constraint != null) {
                val list = ArrayList<Dado>()

                for (deputado in data) {
                    if (deputado.nome.contains(constraint)
                        || deputado.siglaPartido.contains(constraint)
                        || deputado.siglaUf.contains(constraint)){
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