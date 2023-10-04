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
import com.example.portaldatransparencia.interfaces.IClickPhoto
import com.example.portaldatransparencia.interfaces.INotification
import com.example.portaldatransparencia.views.view_generics.AnimatorViewParlamentar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainAdapter(private val listener: IClickDeputado,
                  private val notify: INotification,
                  private val clickPhoto: IClickPhoto,
                  private val animatorView: AnimatorViewParlamentar):
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

                animatorView.animatorView(iconPartidoTransparent)

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        withContext(Dispatchers.Main) {
                            when (deputado.siglaPartido){
                                "PL" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_blue)
                                "PT" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_red)
                                "PP" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_blue_c)
                                "AVANTE" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_orange)
                                "CIDADANIA" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_rose)
                                "MDB" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_green_mdb)
                                "NOVO" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_orange_new)
                                "PATRIOTA" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_green_patri)
                                "PCdoB" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_red_pc)
                                "PDT" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_red)
                                "PODEMOS" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_blue_pode)
                                "REPUBLICANOS" -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_blue_c)
                                else -> iconPartidoTransparent.setBackgroundResource(R.drawable.back_gradient_green)
                            }
                        }
                    }
                }
                constraintDeputado.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    listener.clickDeputado(dataList[position].id.toString())
                }
                iconDeputado.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    clickPhoto.clickPhoto(dataList[position].urlFoto)
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