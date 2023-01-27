package com.example.portaldatransparencia.views.senado.senador.votacoes_senador

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.VotacoesAdapter
import com.example.portaldatransparencia.databinding.FragmentVotacoesSenadorBinding
import com.example.portaldatransparencia.dataclass.Votacao
import com.example.portaldatransparencia.interfaces.ISmoothPosition
import com.example.portaldatransparencia.repository.ResultVotacoesItemRequest
import com.example.portaldatransparencia.repository.ResultVotacoesRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentVotacoes: Fragment(R.layout.fragment_votacoes_senador), ISmoothPosition {

    private lateinit var binding: FragmentVotacoesSenadorBinding
    private val viewModel: VotacoesViewModel by viewModel()
    private lateinit var adapter: VotacoesAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private var ano = "2023"
    private lateinit var id: String
    private lateinit var chipEnabled: Chip
    private var numberVotacoes = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVotacoesSenadorBinding.bind(view)
        chipEnabled = binding.chipGroupItem.chip2023
        id = securityPreferences.getString("id")
        recyclerView()
        listenerChip()
        observer()
    }

    private fun recyclerView() {
        val recycler = binding.recyclerProposta
        adapter = VotacoesAdapter(this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer() {

        viewModel.votacoes(id, ano).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultVotacoesRequest.Success -> {
                        result.dado?.let { votacoes ->
                            val votacao = votacoes.votacaoParlamentar?.parlamentar?.votacoes?.votacao
                            if (votacao?.isNotEmpty() == true){
                                numberVotacoes = votacao.size
                                calculateVotacoes()
                                adapter.updateData(votacao)
                            }
                            else disableProgressAndText()
                        }
                    }
                    is ResultVotacoesRequest.Error -> {
                        result.exception.message?.let {
                            observerItem()
                        }
                    }
                    is ResultVotacoesRequest.ErrorConnection -> {
                        result.exception.message?.let {
                            observerItem()
                        }
                    }
                }
            }
        }
    }

    private fun observerItem() {

        viewModel.votacoesItem(id, ano).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultVotacoesItemRequest.Success -> {
                        result.dado?.let { votacoes ->
                            val votacao = votacoes.votacaoParlamentar?.parlamentar?.votacoes?.votacao
                            if (votacao != null){
                                numberVotacoes = 1
                                calculateVotacoes()
                                val voto: ArrayList<Votacao> = arrayListOf()
                                voto.add(votacao)
                                adapter.updateData(voto)
                            }
                            else disableProgressAndText()
                        }
                    }
                    is ResultVotacoesItemRequest.Error -> {
                        result.exception.message?.let {
                            disableProgressAndText()
                        }
                    }
                    is ResultVotacoesItemRequest.ErrorConnection -> {
                        result.exception.message?.let {
                            disableProgressAndText()
                        }
                    }
                }
            }
        }
    }

    private fun listenerChip(){
        binding.run {
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
        ano = viewDisabled.text.toString()
        chipEnabled = viewDisabled
        binding.run {
            statusView.run {
                enableView(progressVotacoes)
                disableView(textTotalVotos)
                disableView(iconVoto)
                disableView(textNotValue)
            }
        }
        adapter.updateData(arrayListOf())
        observer()
    }

    private fun calculateVotacoes(){

        binding.run {
            statusView.run {
                disableView(progressVotacoes)
                enableView(textTotalVotos)
                enableView(iconVoto)
                disableView(textNotValue)
            }
            if (numberVotacoes == 1){
                "$numberVotacoes Votação".also { textTotalVotos.text = it }
            }else {
                "$numberVotacoes Votações".also { textTotalVotos.text = it }
            }
        }
    }

    private fun disableProgressAndText(){
        binding.run {
            statusView.run {
                disableView(progressVotacoes)
                enableView(textNotValue)
            }
            textNotValue.text = "Nenhuma votação para $ano ou não tinha mandato neste ano."
        }
    }

    override fun smoothPosition(position: Int) {
        binding.recyclerProposta.smoothScrollToPosition(position)
    }

}