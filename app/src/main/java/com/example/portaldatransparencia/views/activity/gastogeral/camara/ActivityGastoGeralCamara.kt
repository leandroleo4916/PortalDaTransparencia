package com.example.portaldatransparencia.views.activity.gastogeral.camara

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.adapter.GraphGastoAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.interfaces.ISmoothPosition
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara
import com.example.portaldatransparencia.util.ConverterValueNotes
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.RetValueFloatOrInt
import com.example.portaldatransparencia.views.view_generics.AddValueViewGraph
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralCamara: AppCompatActivity(), ISmoothPosition {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelCamara by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val addValue: AddValueViewGraph by inject()
    private val crossFade: AnimationView by inject()
    private val converterValue: ConverterValueNotes by inject()
    private lateinit var adapter: GastoSetorAdapter
    private lateinit var adapterGraph: GraphGastoAdapter
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var chipSelected: Chip
    private var anoSelect = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chipGroupItem.chipAll.apply {
            chipSelected = this
            hideView.enableView(this)
        }
        recyclerAdapter()
        modifyElementTop()
        observerGastoCamara()
        listener()
        listenerChip()
    }

    private fun recyclerAdapter(){
        val recycler = binding.recyclerGastoSetor
        adapter = GastoSetorAdapter(FormaterValueBilhoes(), crossFade, this)
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter

        val recyclerGraph = binding.layoutGraph.recyclerGraph
        adapterGraph = GraphGastoAdapter(addValue, RetValueFloatOrInt())
        recyclerGraph.layoutManager =
            LinearLayoutManager(
                this.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerGraph.adapter = adapterGraph
    }
    
    private fun modifyElementTop(){
        binding.run {
            layoutTop.run {
                modifyTextTop(anoSelect)
                hideView.enableView(textViewDescriptionTop)
            }
            chipGroupItem.chip2023.isChecked = false
        }
    }

    private fun modifyTextTop(textString: String){
        binding.layoutTop.run {
            textViewDescriptionTop.run{
                when(textString) {
                    "Todos" -> {
                        text = getString(R.string.gastoGeral12Anos)
                        modifyItemValueGraph(
                            "$ 700 mi", "$ 560 mi", "$ 420 mi", "$ 280 mi", "$ 140 mi")
                    }
                    "2023" -> {
                        text = "Gasto Geral - $textString"
                        modifyItemValueGraph(
                            "$ 5 mi", "$ 4 mi", "$ 3 mi", "$ 2 mi", "$ 1 mi")
                    }
                    else -> {
                        text = "Gasto Geral - $textString"
                        modifyItemValueGraph(
                            "$ 70 mi", "$ 56 mi", "$ 42 mi", "$ 28 mi", "$ 14 mi")
                    }
                }
            }
            textViewDescriptionTop
                .startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.click_votacao))
        }
    }

    private fun modifyItemValueGraph(v1: String, v2: String, v3: String, v4: String, v5: String){
        binding.layoutGraph.run {
            textTop.text = v1
            textTopBellow.text = v2
            textMedium.text = v3
            textMediumBellow.text = v4
            textBottom.text = v5
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
            adapter.updateData(arrayListOf())
            anoSelect = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
            observerGastoCamara()
        }
    }

    private fun observerGastoCamara() {

        viewModel.gastoGeralCamara(anoSelect).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            gastoCamara = gastos
                            modifyTextTop(anoSelect)
                            addElementCamara()
                            viewModel.buildGraphCamara(
                                gastoCamara, adapter, adapterGraph, anoSelect, applicationContext)
                        }
                    }
                    is ResultGastoGeralCamara.Error -> {
                        result.exception.message?.let {
                            binding.layoutProgressAndText.run {
                                hideView.disableView(progressActive)
                                textNotValue.apply {
                                    this.text = "Por enquanto não temos dados do ano $anoSelect"
                                    hideView.enableView(this)
                                }
                            }
                        }
                    }
                    is ResultGastoGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun addElementCamara() {
        binding.run {
            gastoCamara.total.run {
                val total = formatValor.formatValor(totalGeral.toDouble())
                textViewGastoParlamentar.text = "+$total"
                val notes = converterValue.formatString(totalNotas)
                textViewTotalNotas.text = notes
                hideView.run {
                    disableView(layoutProgressAndText.progressActive)
                    crossFade.crossFade(linearLayout, true)
                    crossFade.crossFade(constraintNumberParlamentar, true)
                    crossFade.crossFade(frameGraph, true)
                    crossFade.crossFade(constraintNumberTotal, true)
                    crossFade.crossFade(constraintNumberNotas, true)
                }
            }
        }
    }

    private fun smoothRecycler(position: Int, view: View){
        val recycler = binding.recyclerGastoSetor
        recycler.smoothScrollToPosition(position)
        view.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
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