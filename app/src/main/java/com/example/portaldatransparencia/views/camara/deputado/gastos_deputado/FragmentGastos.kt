package com.example.portaldatransparencia.views.camara.deputado.gastos_deputado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.Despesas
import com.example.portaldatransparencia.interfaces.IClickTipoDespesa
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.network.ApiServiceIdDespesas
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentGastos: Fragment(R.layout.fragment_gastos), INoteDespesas, IClickTipoDespesa {

    private var binding: FragmentGastosBinding? = null
    private val viewModel: DespesasViewModel by inject()
    private lateinit var adapter: DespesasAdapter
    private lateinit var adapterDimension: DimensionAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private lateinit var chipEnabled: Chip
    private lateinit var id: String
    private var ano = "2023"
    private var page = 1
    private var numberNote = 0
    private var listDadosDimension: ArrayList<DadoDespesas> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGastosBinding.bind(view)

        chipEnabled = binding!!.chipGroupItem.chip2023
        id = securityPreferences.getString("id")
        recyclerView()
        observer()
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = DespesasAdapter(this, FormaterValueBilhoes())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val recyclerDimension = binding!!.frameRecyclerDimension.recyclerDimension
        adapterDimension = DimensionAdapter(FormaterValueBilhoes(), this)
        recyclerDimension.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        recyclerDimension.adapter = adapterDimension
    }

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServiceIdDespesas::class.java)
        val call: Call<Despesas> = retrofit.getIdDespesas(id, ano, 100, page)
        call.enqueue(object: Callback<Despesas> {
            override fun onResponse(call: Call<Despesas>, despesas: Response<Despesas>){
                when (despesas.code()){
                    200 -> {
                        if (despesas.body()!!.dados.isNotEmpty()){
                            if (page == 1) listDadosDimension = arrayListOf()
                            listDadosDimension += despesas.body()!!.dados

                            statusView.disableView(binding!!.layoutProgress.textNotValue)
                            val size = despesas.body()!!.dados.size
                            numberNote += size
                            adapter.updateData(despesas.body()!!.dados, page)
                            page += 1

                            if (size >= 100) observer()
                            else {
                                statusView.disableView(binding!!.layoutProgress.progressActive)
                                viewModel.captureDataNotes(listDadosDimension, adapterDimension)
                            }
                        }
                        else noValue("Não há dados no ano $ano")
                    }
                    429 -> observer()
                    else -> noValue("API não respondeu!")
                }
            }
            override fun onFailure(call: Call<Despesas>, t: Throwable) {
                noValue("API não respondeu!")
            }
        })
    }

    private fun listenerChip(){
        binding?.run {
            chipGroupItem.run {
                chip2023.setOnClickListener { modify(chipEnabled, chip2023) }
                chip2022.setOnClickListener { modify(chipEnabled, chip2022) }
                chip2021.setOnClickListener { modify(chipEnabled, chip2021) }
                chip2020.setOnClickListener { modify(chipEnabled, chip2020) }
                chip2019.setOnClickListener { modify(chipEnabled, chip2019) }
                chip2018.setOnClickListener { modify(chipEnabled, chip2018) }
                chip2017.setOnClickListener { modify(chipEnabled, chip2017) }
                chip2016.setOnClickListener { modify(chipEnabled, chip2016) }
                chip2015.setOnClickListener { modify(chipEnabled, chip2015) }
                chip2014.setOnClickListener { modify(chipEnabled, chip2014) }
                chip2013.setOnClickListener { modify(chipEnabled, chip2013) }
                chip2012.setOnClickListener { modify(chipEnabled, chip2012) }
                chip2011.setOnClickListener { modify(chipEnabled, chip2011) }
            }
        }
    }

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {
        ano = viewDisabled.text.toString()
        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        numberNote = 0
        page = 1
        chipEnabled = viewDisabled
        binding!!.layoutProgress.run {
            statusView.disableView(textNotValue)
            statusView.enableView(progressActive)
        }
        adapter.updateData(arrayListOf(), 1)
        adapterDimension.updateData(arrayListOf())
        observer()
    }

    private fun noValue(value: String){
        binding!!.layoutProgress.run {
            statusView.disableView(progressActive)
            statusView.enableView(textNotValue)
            textNotValue.text = value
        }
    }

    override fun listenerDespesas(note: String?) {
        if (note != null && note.isNotEmpty()){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(note))
            startActivity(browserIntent)
        } else {
            Toast.makeText(requireContext(),
                "Comprovante não enviado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun clickTipoDespesa(type: String) = adapter.filter.filter(type)

}