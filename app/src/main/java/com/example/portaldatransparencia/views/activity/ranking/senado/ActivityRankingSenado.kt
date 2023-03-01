package com.example.portaldatransparencia.views.activity.ranking.senado

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.databinding.LayoutRankingBinding
import com.example.portaldatransparencia.dataclass.Ranking
import com.example.portaldatransparencia.interfaces.IClickOpenDeputadoRanking
import com.example.portaldatransparencia.interfaces.INotification
import com.example.portaldatransparencia.repository.ResultRankingSenado
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.ModifyChip
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityRankingSenado: AppCompatActivity(), IClickOpenDeputadoRanking, INotification {

    private val binding by lazy { LayoutRankingBinding.inflate(layoutInflater) }
    private val viewModel: RankingViewModelSenado by viewModel()
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val animeView: AnimationView by inject()
    private lateinit var adapter: GastoGeralAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var listGastoGeralSenador: List<Ranking>
    private lateinit var chipSelected: Chip
    private var anoSelect = "Todos"
    private var hideFilter = false

    private var chipEnabled: Chip? = null
    private var chipEnabledState: Chip? = null
    private val modifyChip: ModifyChip by inject()
    private var textPartido = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            layoutYear.chipAll.apply {
                chipSelected = this
                hideView.enableView(this)
            }
            layoutYear.chip2023.isChecked = false
            layoutGroupPartidos.run {
                hideView.disableView(scrollState)
            }
        }

        listenerChip()
        modifyTextTitleAndListenerBack()
        recycler()
        observerRanking()
    }

    private fun modifyTextTitleAndListenerBack() {
        binding.layoutTop.run {
            hideView.disableView(binding.layoutGroupPartidos.scrollState)
            hideView.enableView(textViewDescriptionTop)
            textViewTitleTop.text = getString(R.string.senado_federal)
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
            imageViewFilter.setOnClickListener {
                hideFilter = if (hideFilter) {
                    showFilters(true)
                    false
                } else {
                    showFilters(false)
                    true
                }
            }
        }
    }

    private fun recycler(){
        val recycler = binding.recyclerRancking
        adapter = GastoGeralAdapter(formatValor, this, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerRanking() {

        viewModel.rankingSenado(anoSelect).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRankingSenado.Success -> {
                        result.dado?.let { gastos ->
                            listGastoGeralSenador = gastos.ranking
                            disableProgressAndText()
                            showFilters(true)
                            adapter.updateData(listGastoGeralSenador as ArrayList)
                        }
                    }
                    is ResultRankingSenado.Error -> {
                        showErrorApi()
                    }
                    is ResultRankingSenado.ErrorConnection -> {
                        showErrorApi()
                    }
                }
            }
        }
    }

    private fun listenerChip(){
        binding.run {
            layoutYear.run {
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
            layoutGroupPartidos.run {
                chipAvante.setOnClickListener { modifyChipPartido(chipAvante) }
                chipCidadania.setOnClickListener { modifyChipPartido(chipCidadania) }
                chipDc.setOnClickListener { modifyChipPartido(chipDc) }
                chipDem.setOnClickListener { modifyChipPartido(chipDem) }
                chipMdb.setOnClickListener { modifyChipPartido(chipMdb) }
                chipNovo.setOnClickListener { modifyChipPartido(chipNovo) }
                chipPatri.setOnClickListener { modifyChipPartido(chipPatri) }
                chipPatriota.setOnClickListener { modifyChipPartido(chipPatriota) }
                chipPcb.setOnClickListener { modifyChipPartido(chipPcb) }
                chipPcdob.setOnClickListener { modifyChipPartido(chipPcdob) }
                chipPco.setOnClickListener { modifyChipPartido(chipPco) }
                chipPdt.setOnClickListener { modifyChipPartido(chipPdt) }
                chipPhs.setOnClickListener { modifyChipPartido(chipPhs) }
                chipPl.setOnClickListener { modifyChipPartido(chipPl) }
                chipPros.setOnClickListener { modifyChipPartido(chipPros) }
                chipPsc.setOnClickListener { modifyChipPartido(chipPsc) }
                chipPmb.setOnClickListener { modifyChipPartido(chipPmb) }
                chipPodemos.setOnClickListener { modifyChipPartido(chipPodemos) }
                chipPp.setOnClickListener { modifyChipPartido(chipPp) }
                chipPt.setOnClickListener { modifyChipPartido(chipPt) }
                chipRepublicanos.setOnClickListener { modifyChipPartido(chipRepublicanos) }
                chipUniao.setOnClickListener { modifyChipPartido(chipUniao) }
            }
        }
    }

    private fun modifyChipPartido(viewDisabled: Chip) {
        chipEnabled = modifyChip.modify(chipEnabled, viewDisabled)
        if (chipEnabledState != null) {
            chipEnabledState!!.isChecked = false
        }
        textPartido = viewDisabled.text as String
        if (!viewDisabled.isChecked) {
            adapter.filterList("")
            textPartido = ""
        }
        else {
            adapter.filterList(viewDisabled.text as String)
        }
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {
        if (anoSelect != viewClicked.text){
            disableProgressAndText()
            adapter.updateData(arrayListOf())
            anoSelect = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
            observerRanking()
        }
    }

    private fun disableProgressAndText(){
        hideView.run {
            binding.layoutProgressAndText.run {
                disableView(progressActive)
                disableView(textNotValue)
            }
        }
    }

    override fun clickRanking(id: String) {}

    private fun showErrorApi(){
        binding.layoutProgressAndText.apply {
            animeView.crossFade(textNotValue, true)
            hideView.disableView(progressActive)
            textNotValue.text = getString(R.string.erro_api_senado)
        }
    }

    private fun showFilters(boolean: Boolean){
        animeView.crossFade(binding.framePartidos, boolean)
        animeView.crossFade(binding.frameYearRanking, boolean)
    }

    override fun notification() {
        Toast.makeText(applicationContext, getString(R.string.nao_encontrado_dep_partido),
            Toast.LENGTH_SHORT).show()
    }
}