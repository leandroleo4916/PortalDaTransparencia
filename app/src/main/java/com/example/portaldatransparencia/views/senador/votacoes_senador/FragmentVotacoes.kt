package com.example.portaldatransparencia.views.senador.votacoes_senador

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.PropostaAdapter
import com.example.portaldatransparencia.adapter.VotacoesAdapter
import com.example.portaldatransparencia.databinding.FragmentPropostaBinding
import com.example.portaldatransparencia.databinding.FragmentVotacoesSenadorBinding
import com.example.portaldatransparencia.remote.ResultPropostaRequest
import com.example.portaldatransparencia.remote.ResultVotacoesRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentVotacoes: Fragment(R.layout.fragment_votacoes_senador) {

    private var binding: FragmentVotacoesSenadorBinding? = null
    private val viewModel: VotacoesViewModel by viewModel()
    private lateinit var adapter: VotacoesAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private var ano = "2022"
    private lateinit var id: String
    private lateinit var chipEnabled: Chip
    private var numberVotacoes = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVotacoesSenadorBinding.bind(view)
        chipEnabled = binding!!.chipGroupItem.chip2022
        id = securityPreferences.getString("id")
        recyclerView()
        listenerChip()
        observer()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerProposta
        adapter = VotacoesAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer() {

        viewModel.votacoes(id, ano).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultVotacoesRequest.Success -> {
                        result.dado?.let { votacoes ->
                            val votacao = votacoes.votacaoParlamentar.parlamentar.votacoes.votacao
                            if (votacao.isNotEmpty()){
                                numberVotacoes = votacao.size
                                calculateVotacoes()
                                adapter.updateData(votacao)
                            }
                            else{
                                binding?.run {
                                    statusView.disableView(progressVotacoes)
                                    statusView.enableView(textNotValue)
                                    textNotValue.text =
                                        "Nenhuma votação para $ano ou não tinha mandato neste ano."
                                }
                            }
                        }
                    }
                    is ResultVotacoesRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultVotacoesRequest.ErrorConnection -> {
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
        ano = viewDisabled.text.toString()
        chipEnabled = viewDisabled
        binding?.run {
            statusView.enableView(progressVotacoes)
            statusView.disableView(textTotalVotos)
            statusView.disableView(iconVoto)
            statusView.disableView(textNotValue)
        }
        adapter.updateData(arrayListOf())
        observer()
    }

    private fun calculateVotacoes(){

        binding?.run {
            statusView.disableView(progressVotacoes)
            statusView.enableView(textTotalVotos)
            statusView.enableView(iconVoto)
            statusView.disableView(textNotValue)
            "$numberVotacoes Votações".also { textTotalVotos.text = it }
        }
    }

}