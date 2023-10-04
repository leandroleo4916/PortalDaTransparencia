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
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.interfaces.IClickPhoto
import com.example.portaldatransparencia.interfaces.IClickParlamentar
import com.example.portaldatransparencia.interfaces.INotificationSenado
import com.example.portaldatransparencia.views.view_generics.AnimatorViewParlamentar
import kotlinx.coroutines.*

class SenadoAdapter(private val listener: IClickParlamentar,
                    private val notify: INotificationSenado,
                    private val clickPhoto: IClickPhoto,
                    private val animatorView: AnimatorViewParlamentar):
    RecyclerView.Adapter<SenadoAdapter.MainViewHolder>(), Filterable {

    private var binding: RecyclerMainBinding? = null
    private var data = mutableListOf<Parlamentar>()
    private var dataList = mutableListOf<Parlamentar>()
    private var dataListState = mutableListOf<Parlamentar>()
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

                animatorView.animatorView(iconPartidoTransparent)

                constraintDeputado.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    listener.clickParlamentar(
                        dataList[adapterPosition].identificacaoParlamentar.codigoParlamentar,
                        dataList[adapterPosition].identificacaoParlamentar.nomeParlamentar)
                }
                iconDeputado.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.click))
                    clickPhoto.clickPhoto(photo)
                }

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        withContext(Dispatchers.Main) {
                            when (item.siglaPartidoParlamentar){
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
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(senadores: List<Parlamentar>) {
        data = senadores as MutableList<Parlamentar>
        dataList = senadores
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(partido: String, estado: String){
        dataListState = mutableListOf()
        data.forEach {
            if (it.identificacaoParlamentar.siglaPartidoParlamentar.contains(partido)
                && it.identificacaoParlamentar.ufParlamentar.contains(estado)){
                dataListState.add(it)
            }
        }
        dataList = dataListState
        if (dataList.isEmpty()) notify.notification()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter = filter

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