package com.example.portaldatransparencia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.ListDeputado
import com.example.portaldatransparencia.util.FormatValor
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import kotlinx.coroutines.*

class GastoGeralAdapterCamara(private val formatValor: FormaterValueBilhoes) :
    RecyclerView.Adapter<GastoGeralAdapterCamara.MainViewHolder>() {

    private var data = arrayListOf<ListDeputado>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_rancking, parent, false)

        return MainViewHolder(item)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time)
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: ListDeputado) {

            itemView.run {
                val progress = findViewById<ProgressBar>(R.id.progress_circular_rancking)
                findViewById<TextView>(R.id.text_name_rancking).text = item.nome
                findViewById<TextView>(R.id.text_valor_item).text =
                    "${formatValor.formatValor(item.gasto.toDouble())}"
                val image = findViewById<ImageView>(R.id.icon_image)
                Glide.with(context)
                    .load(item.urlFoto)
                    .circleCrop()
                    .into(image)

                var value = 0
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        while (value <= 20) {
                            withContext(Dispatchers.Main) {
                                progress.progress = value
                            }
                            delay(10)
                            value++
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deputado: ArrayList<ListDeputado>) {
        deputado.sortByDescending {
            it.gasto.toInt()
        }
        data = deputado
        notifyDataSetChanged()
    }
}