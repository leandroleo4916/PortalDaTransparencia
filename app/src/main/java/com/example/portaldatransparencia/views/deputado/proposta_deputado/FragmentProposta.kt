package com.example.portaldatransparencia.views.deputado.proposta_deputado

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.PropostaAdapter
import com.example.portaldatransparencia.databinding.FragmentPropostaBinding
import com.example.portaldatransparencia.remote.ResultPropostaRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPropostaBinding.bind(view)
        chipEnabled = binding!!.chipGroupItem.chip2022
        id = securityPreferences.getStoredString("id")
        recyclerView()
        listenerChip()
        observer("2022", id, page)
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerProposta
        adapter = PropostaAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer(year: String, id: String, pagina: Int) {

        viewModel.propostaDeputado(year, id, pagina).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultPropostaRequest.Success -> {
                        result.dado?.let { proposta ->
                            if (proposta.dados.isNotEmpty()){
                                val size = proposta.dados.size
                                calculatePropostas(size, page)
                                adapter.updateData(proposta.dados, page)
                                numberProposta += size
                                page += 1
                                if (size >= 100) observer(year, id, page)
                            }else{
                                if (numberProposta == 0) {
                                    binding?.run {
                                        statusView.disableView(progressProposta)
                                        statusView.enableView(textNotValue)
                                        textNotValue.text =
                                            "Nenhum projeto de lei para $year ou não tinha mandato neste ano."
                                    }
                                    adapter.updateData(proposta.dados, page)
                                }
                            }
                        }
                    }
                    is ResultPropostaRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultPropostaRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun listenerChip(){
        binding?.run {
            chipGroupItem.run {
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
        page = 1
        chipEnabled = viewDisabled
        binding?.run {
            statusView.enableView(progressProposta)
            statusView.disableView(textPropostaParlamentar)
            statusView.disableView(iconProposta)
            statusView.disableView(textNotValue)
        }
        adapter.updateData(arrayListOf(), 1)
        observer(viewDisabled.text as String, id, page)
    }

    private fun calculatePropostas(size: Int, pagina: Int){
        if (pagina == 1) numberProposta = 0
        numberProposta += size
        binding?.run {
            statusView.disableView(progressProposta)
            statusView.enableView(textPropostaParlamentar)
            statusView.enableView(iconProposta)
            statusView.disableView(textNotValue)
            "$numberProposta Projetos".also { textPropostaParlamentar.text = it }
        }
    }

}