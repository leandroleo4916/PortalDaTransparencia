package com.example.portaldatransparencia.views.activity.gastogeral.camara

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara
import com.example.portaldatransparencia.util.ConverterValueNotes
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralCamara: AppCompatActivity() {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelCamara by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val crossFade: AnimationView by inject()
    private val converterValue: ConverterValueNotes by inject()
    private lateinit var adapter: GastoSetorAdapter
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var chipSelected: Chip
    private var anoSelect = "Todos"
    private var hideFilter = false
    private val animeView: AnimationView by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chipGroupItem.chipAll.apply {
            chipSelected = this
            hideView.enableView(this)
        }
        recyclerAdapter()
        modifyItemTop()
        observerGastoCamara()
        listener()
        listenerChip()
        clickIconFilter()
    }

    private fun recyclerAdapter(){
        val recycler = binding.recyclerGastoSetor
        adapter = GastoSetorAdapter(FormaterValueBilhoes(), crossFade)
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter
    }

    private fun modifyItemTop(){
        binding.run {
            layoutTop.run {
                modifyTextTop("últimos 12 anos")
                hideView.enableView(textViewDescriptionTop)
            }
            chipGroupItem.chip2023.isChecked = false
        }
    }

    private fun clickIconFilter(){
        binding.layoutTop.imageViewFilter.setOnClickListener {
            animaView(it)
            showFilterIcons()
        }
    }

    private fun modifyTextTop(text: String){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text =
                    if (text != "Todos") "Gasto Geral - $text"
                    else getString(R.string.gastoGeral12Anos)
                textViewDescriptionTop
                    .startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.click_votacao))
            }
        }
    }

    private fun showFilterIcons(){
        binding.run {
            if (hideFilter){
                layoutTop.imageViewFilter.setImageResource(R.drawable.ic_no_filter_dark)
                hideFilter = false
                animeView.crossFade(frameYear, true)
                animeView.crossFade(viewDiv, true)
            }
            else {
                layoutTop.imageViewFilter.setImageResource(R.drawable.ic_filter_dark)
                hideFilter = true
                animeView.crossFade(frameYear,false)
                animeView.crossFade(viewDiv,false)
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
                            addElementCamara()
                            modifyTextTop(anoSelect)
                            viewModel.buildGraphCamara(gastoCamara, adapter, applicationContext)
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
                    crossFade.crossFade(constraintNumberTotal, true)
                    crossFade.crossFade(constraintNumberNotas, true)
                }
            }
        }
    }

    private fun animaView(view: View){
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
}