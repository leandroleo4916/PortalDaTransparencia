package com.example.portaldatransparencia.views.activity.gastogeral.senado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.adapter.GraphGastoAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralSenadoData
import com.example.portaldatransparencia.interfaces.ISmoothPosition
import com.example.portaldatransparencia.repository.ResultGastoGeralSenado
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.RetValueInt
import com.example.portaldatransparencia.views.view_generics.AddValueViewGraph
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralSenado: AppCompatActivity(), ISmoothPosition {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelSenado by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val crossFade: AnimationView by inject()
    private lateinit var gastoSenado: GastoGeralSenadoData
    private lateinit var adapterGraph: GraphGastoAdapter
    private lateinit var adapter: GastoSetorAdapter
    private lateinit var chipSelected: Chip
    private var anoSelect = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chipGroupItem.chipAll.apply {
            chipSelected = this
            hideView.enableView(this)
        }
        recycler()
        listener()
        listenerChip()
        modifyItemTop()
        observerGastoSenado()
    }

    private fun recycler(){
        val recycler = binding.recyclerGastoSetor
        adapter = GastoSetorAdapter(FormaterValueBilhoes(), crossFade, this)
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter

        val recyclerGraph = binding.layoutGraph.recyclerGraph
        adapterGraph = GraphGastoAdapter(AddValueViewGraph(), RetValueInt())
        recyclerGraph.layoutManager =
            LinearLayoutManager(
                this.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerGraph.adapter = adapterGraph
    }

    private fun modifyItemTop(){
        binding.run {
            layoutTop.run {
                textViewTitleTop.text = getString(R.string.senado_federal)
                modifyTextTop("últimos 12 anos")
                hideView.enableView(textViewDescriptionTop)
                hideView.disableView(imageViewFilter)
            }
            chipGroupItem.chip2023.isChecked = false
        }
    }

    private fun modifyTextTop(text: String){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text =
                    if (text != "Todos") "Gasto geral - $text"
                    else getString(R.string.gastoGeral12Anos)
                textViewDescriptionTop
                    .startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.click_votacao))
            }
        }
    }

    private fun observerGastoSenado() {

        viewModel.gastoGeralSenado(anoSelect).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralSenado.Success -> {
                        result.dado?.let { gasto ->
                            gastoSenado = gasto
                            modifyTextTop(anoSelect)
                            addElementSenado()
                            viewModel.buildGraphCamara(
                                gasto, adapter, adapterGraph, anoSelect, applicationContext)
                        }
                    }
                    is ResultGastoGeralSenado.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultGastoGeralSenado.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun listenerChip(){
        binding.run {
            chipGroupItem.run {
                chipAll.setOnClickListener { modify(chipSelected, chipAll) }
                chip2023.setOnClickListener { modify(chipSelected, chip2023) }
                chip2022.setOnClickListener { modify(chipSelected, chip2022) }
                chip2021.setOnClickListener { modify(chipSelected, chip2021) }
                chip2020.setOnClickListener { modify(chipSelected, chip2020) }
                chip2019.setOnClickListener { modify(chipSelected, chip2019) }
                chip2018.setOnClickListener { modify(chipSelected, chip2018) }
                chip2017.setOnClickListener { modify(chipSelected, chip2017) }
                chip2016.setOnClickListener { modify(chipSelected, chip2016) }
                chip2015.setOnClickListener { modify(chipSelected, chip2015) }
                chip2014.setOnClickListener { modify(chipSelected, chip2014) }
                chip2013.setOnClickListener { modify(chipSelected, chip2013) }
                chip2012.setOnClickListener { modify(chipSelected, chip2012) }
                chip2011.setOnClickListener { modify(chipSelected, chip2011) }
            }
        }
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {

        if (anoSelect != viewClicked.text){
            hideView.run {
                binding.layoutProgressAndText.run {
                    enableView(progressActive)
                    disableView(textNotValue)
                }
                disableView(binding.linearLayout)
            }
            anoSelect = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
        }
    }

    private fun addElementSenado() {
        binding.run {
            gastoSenado.gastoGeral.run {
                val total = formatValor.formatValor(total.toDouble())
                textViewParlamentar.text = getString(R.string.senadores)
                textViewTotalParlamentar.text = "83"
                textViewGastoParlamentar.text = total
                textViewTotalNotas.text = notas
                hideView.run {
                    disableView(layoutProgressAndText.progressActive)
                    crossFade.crossFade(constraintNumberParlamentar, true)
                    crossFade.crossFade(frameGraph, true)
                    crossFade.crossFade(constraintNumberTotal, true)
                    crossFade.crossFade(constraintNumberNotas, true)
                }
            }
        }
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    override fun smoothPosition(position: Int) {}

}