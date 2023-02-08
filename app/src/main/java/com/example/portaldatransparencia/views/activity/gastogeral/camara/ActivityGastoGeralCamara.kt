package com.example.portaldatransparencia.views.activity.gastogeral.camara

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara
import com.example.portaldatransparencia.util.FormatValor
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.eazegraph.lib.models.PieModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralCamara: AppCompatActivity() {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelCamara by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val crossFade: AnimationView by inject()
    private lateinit var adapter: GastoSetorAdapter
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var chipSelected: Chip
    private var infoSetor: ArrayList<AddInfoSetor> = arrayListOf()
    private var ano = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chipGroupItem.chipAll.apply {
            chipSelected = this
            hideView.enableView(this)
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
        adapter = GastoSetorAdapter(FormatValor(), crossFade)
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter
    }
    
    private fun modifyElementTop(){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text = getString(R.string.gastoGeral8Anos)
                hideView.enableView(textViewDescriptionTop)
            }
            chipGroupItem.chip2023.isChecked = false
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
            }
        }
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {
        if (ano != viewClicked.text){
            hideView.run {
                disableView(binding.progressGastoGeral)
                disableView(binding.textResultRacking)
                disableView(binding.linearLayout)
            }
            adapter.updateData(arrayListOf())
            ano = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
            observerGastoCamara()
        }
    }

    private fun observerGastoCamara() {

        viewModel.gastoGeralCamara(ano).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            gastoCamara = gastos
                            addElementCamara(gastoCamara)
                            buildGraphCamara()
                        }
                    }
                    is ResultGastoGeralCamara.Error -> {
                        result.exception.message?.let {
                            hideView.disableView(binding.progressGastoGeral)
                            binding.textResultRacking.apply {
                                this.text = "Por enquanto não temos dados do ano $ano"
                                hideView.enableView(this)
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

    private fun addElementCamara(dados: GastoGeralCamara) {
        binding.run {
            dados.total.run {
                val total = formatValor.formatValor(totalGeral.toDouble())
                textViewGastoParlamentar.text = total
                textViewTotalNotas.text = dados.total.totalNotas
                hideView.run {
                    disableView(progressGastoGeral)
                    enableView(linearLayout)
                    enableView(constraintNumberParlamentar)
                    crossFade.crossFade(constraintNumberParlamentar)
                    enableView(constraintNumberTotal)
                    crossFade.crossFade(constraintNumberTotal)
                    enableView(constraintNumberNotas)
                    crossFade.crossFade(constraintNumberNotas)
                }
            }
        }
    }

    private fun buildGraphCamara(){
        infoSetor = arrayListOf()
        gastoCamara.total.run {
            infoSetor.add(AddInfoSetor(getString(R.string.manutencao_escritorio), this.manutencao, R.drawable.back_teal))
            infoSetor.add(AddInfoSetor(getString(R.string.combustivel_lubrificante), this.combustivel, R.drawable.back_teal_red))
            infoSetor.add(AddInfoSetor(getString(R.string.passagens_aereas), this.passagens, R.drawable.back_teal_yellow))
            infoSetor.add(AddInfoSetor(getString(R.string.assinaturas), this.assinatura, R.drawable.back_teal))
            infoSetor.add(AddInfoSetor(getString(R.string.divulgacao_parlamentar), this.divulgacao, R.drawable.back_teal_red))
            infoSetor.add(AddInfoSetor(getString(R.string.servicos_telefonicos), this.telefonia, R.drawable.back_teal_yellow))
            infoSetor.add(AddInfoSetor(getString(R.string.servico_postal), this.postais, R.drawable.back_teal))
            infoSetor.add(AddInfoSetor(getString(R.string.hospedagem), this.hospedagem, R.drawable.back_teal_red))
            infoSetor.add(AddInfoSetor(getString(R.string.taxi), this.taxi, R.drawable.back_teal_yellow))
            infoSetor.add(AddInfoSetor(getString(R.string.locacao), this.locacao, R.drawable.back_teal))
            infoSetor.add(AddInfoSetor(getString(R.string.consultoriaAcessoria), this.consultoria, R.drawable.back_teal_red))
            infoSetor.add(AddInfoSetor(getString(R.string.seguranca), this.seguranca, R.drawable.back_teal_yellow))
            infoSetor.add(AddInfoSetor(getString(R.string.curso), this.curso, R.drawable.back_teal))
            infoSetor.add(AddInfoSetor(getString(R.string.alimentacao), this.alimentacao, R.drawable.back_teal_red))
            infoSetor.add(AddInfoSetor(getString(R.string.outros_servicos), this.outros, R.drawable.back_teal_yellow))
        }
        addGraphCamara(infoSetor)
        adapter.updateData(infoSetor)
    }

    private fun addGraphCamara(info: ArrayList<AddInfoSetor>){
        info.forEach {
            binding.run {
                piechart.addPieSlice(PieModel(it.description, it.value.toFloat(), it.color))
                piechart.startAnimation()
            }
            crossFade.crossFade(binding.piechart)
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
}