package com.example.portaldatransparencia.views.deputado.proposta_deputado

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.PropostaAdapter
import com.example.portaldatransparencia.databinding.FragmentPropostaBinding
import com.example.portaldatransparencia.dataclass.PropostaDataClass
import com.example.portaldatransparencia.remote.ApiServiceProposta
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentProposta: Fragment(R.layout.fragment_proposta) {

    private var binding: FragmentPropostaBinding? = null
    private val viewModel: PropostaViewModel by viewModel()
    private lateinit var adapter: PropostaAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private lateinit var id: String
    private lateinit var chipEnabled: Chip
    private var numberProposta = 0
    private var page = 1
    private var year = "2023"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPropostaBinding.bind(view)
        chipEnabled = binding!!.chipGroupItem.chip2023
        id = securityPreferences.getString("id")
        recyclerView()
        listenerChip()
        observer()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerProposta
        adapter = PropostaAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServiceProposta::class.java)
        val call: Call<PropostaDataClass> = retrofit.getProposta(year, id, page, 100)
        call.enqueue(object: Callback<PropostaDataClass> {
            override fun onResponse(call: Call<PropostaDataClass>, res: Response<PropostaDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null && res.body()!!.dados.isNotEmpty()){
                            val size = res.body()!!.dados.size
                            calculatePropostas(size, page)
                            adapter.updateData(res.body()!!.dados, page)
                            numberProposta += size
                            page += 1
                            if (size >= 100) observer()
                        }
                        else {
                            if (numberProposta == 0) {
                                binding?.run {
                                    statusView.disableView(progressProposta)
                                    statusView.enableView(textNotValue)
                                    textNotValue.text =
                                        "Nenhum projeto de lei para $year"
                                }
                                adapter.updateData(res.body()!!.dados, page)
                            }
                        }
                    }
                    429 -> observer()
                    else -> {

                    }
                }
            }

            override fun onFailure(call: Call<PropostaDataClass>, t: Throwable) {

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
            }
        }
    }

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {
        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        numberProposta = 0
        year = viewDisabled.text.toString()
        page = 1
        chipEnabled = viewDisabled
        binding?.run {
            statusView.run {
                enableView(progressProposta)
                disableView(textPropostaParlamentar)
                disableView(iconProposta)
                disableView(textNotValue)
            }
        }
        adapter.updateData(arrayListOf(), 1)
        observer()
    }

    private fun calculatePropostas(size: Int, pagina: Int){
        if (pagina == 1) numberProposta = 0
        numberProposta += size
        binding?.run {
            statusView.run {
                disableView(progressProposta)
                enableView(textPropostaParlamentar)
                enableView(iconProposta)
                disableView(textNotValue)
            }
            "$numberProposta Projetos".also { textPropostaParlamentar.text = it }
        }
    }

}