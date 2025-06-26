package com.example.portaldatransparencia.views.activity.gastogeral.senado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralSenadoData
import com.example.portaldatransparencia.repository.ResultGastoGeralSenado
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.createDialog
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralSenado: AppCompatActivity() {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelSenado by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val crossFade: AnimationView by inject()
    private lateinit var gastoSenado: GastoGeralSenadoData
    private lateinit var adapter: GastoSetorAdapter
    private lateinit var chipSelected: Chip
    private var anoSelect = "Todos"
    private var hideFilter = false
    private val animeView: AnimationView by inject()
    private lateinit var create: AlertDialog

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
        clickIconFilter()
    }

    private fun recycler(){
        val recycler = binding.recyclerGastoSetor
        adapter = GastoSetorAdapter(FormaterValueBilhoes(), crossFade)
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter
    }

    private fun modifyItemTop(){
        binding.run {
            layoutTop.run {
                textViewTitleTop.text = "Cotas - Senado"
                modifyTextTop("Últimos 12 anos")
                hideView.enableView(textViewDescriptionTop)
            }
            chipGroupItem.chip2023.isChecked = false
        }
    }

    private fun modifyTextTop(text: String){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text =
                    if (text != "Todos") "Gasto Geral - $text"
                    else getString(R.string.gastoGeral12Anos)
                animaView2(textViewDescriptionTop)
            }
        }
    }

    private fun clickIconFilter(){
        binding.layoutTop.imageViewFilter.setOnClickListener {
            animaView(it)
            showFilterIcons()
        }
        binding.layoutTop.imageViewOptionOrQuestion.setOnClickListener {
            animaView(it)
            clickOptionOrQuestion()
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
                            viewModel.buildGraphCamara(gasto, adapter, applicationContext)
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
                chip2025.setOnClickListener { modify(chipSelected, chip2025) }
                chip2024.setOnClickListener { modify(chipSelected, chip2024) }
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

    private fun showFilterIcons(){
        binding.run {
            if (hideFilter){
                layoutTop.imageViewFilter.setImageResource(R.drawable.ic_no_filter_dark)
                hideFilter = false
                animeView.crossFade(frameYear)
                animeView.crossFade(viewDiv)
            }
            else {
                layoutTop.imageViewFilter.setImageResource(R.drawable.ic_filter_dark)
                hideFilter = true
                animeView.crossFade(frameYear)
                animeView.crossFade(viewDiv)
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
            observerGastoSenado()
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
                    crossFade.crossVisibleView(linearLayout)
                    crossFade.crossVisibleView(constraintNumberParlamentar)
                    crossFade.crossVisibleView(constraintNumberTotal)
                    crossFade.crossVisibleView(constraintNumberNotas)
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

    private fun clickOptionOrQuestion(){
        val dialog = createDialog()
        val viewDialog = layoutInflater.inflate(R.layout.layout_dialog_question, null)
        val seeMore = viewDialog.findViewById<TextView>(R.id.text_see_more)
        val close = viewDialog.findViewById<ImageView>(R.id.image_close)
        seeMore.setOnClickListener {
            animaView(seeMore)
            create.dismiss()
            val url = "https://www2.camara.leg.br/transparencia/acesso-a-informacao/" +
                    "copy_of_perguntas-frequentes/cota-para-o-exercicio-da-atividade-parlamentar#" +
                    ":~:text=A%20Cota%20para%20o%20Exerc%C3%ADcio,ao%20exerc%C3%ADcio%20da%20atividade%20parlamentar."
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        close.setOnClickListener { create.dismiss() }
        dialog.setView(viewDialog)
        create = dialog.create()
        create.show()
    }

    private fun animaView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
    }
    private fun animaView2(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click_votacao))
    }
}