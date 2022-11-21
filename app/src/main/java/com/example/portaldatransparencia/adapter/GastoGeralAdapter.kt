package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.RecyclerRankingBinding
import com.example.portaldatransparencia.dataclass.ListParlamentar
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import kotlinx.coroutines.*

class GastoGeralAdapter(private val formatValor: FormaterValueBilhoes) :
    RecyclerView.Adapter<GastoGeralAdapter.MainViewHolder>() {

    private var binding: RecyclerRankingBinding? = null
    private var data = arrayListOf<ListParlamentar>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_ranking, parent, false)

        return MainViewHolder(item)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time)
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(item: ListParlamentar) {

            binding = RecyclerRankingBinding.bind(itemView)
            binding?.run {
                textNameRancking.text = item.nome
                textPartidoUf.text = "${item.partido}"+" - "+"${item.uf}"
                textValorItem.text = formatValor.formatValor(item.gasto.toDouble())

                var value = 0
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        while (value <= 20) {
                            withContext(Dispatchers.Main) {
                                progressCircularRancking.progress = value
                            }
                            delay(5)
                            value++
                        }
                    }
                }
                Glide.with(itemView)
                    .load(item.urlFoto)
                    .circleCrop()
                    .into(iconImage)

                itemView.setOnClickListener {

                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(parlamentar: ArrayList<ListParlamentar>) {
        parlamentar.sortByDescending {
            it.gasto.toInt()
        }
        data = parlamentar
        notifyDataSetChanged()
    }
}