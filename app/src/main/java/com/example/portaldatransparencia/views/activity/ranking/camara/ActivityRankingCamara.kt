package com.example.portaldatransparencia.views.activity.ranking.camara

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.portaldatransparencia.repository.ResultRankingGeralCamara
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.camara.deputado.DeputadoActivity
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.ModifyChip
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityRankingCamara: AppCompatActivity(), IClickOpenDeputadoRanking, INotification {

    private val binding by lazy { LayoutRankingBinding.inflate(layoutInflater) }
    private val viewModel: RankingViewModelCamara by viewModel()
    private val camaraViewModel: CamaraViewModel by viewModel()
    private val validationInternet: ValidationInternet by inject()
    private val hideView: EnableDisableView by inject()
    private val animeView: AnimationView by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var adapter: GastoGeralAdapter
    private lateinit var listGastoGeralDeputado: ArrayList<Ranking>
    private lateinit var id: String
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

        id = securityPreferences.getString("id")
        modifyElementTop()
        listenerChip()
        recycler()
        listener()
        observerGastoCamara()
    }

    private fun recycler(){
        val recycler = binding.recyclerRancking
        adapter = GastoGeralAdapter(formatValor, this, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun listener() {
        binding.layoutTop.run {
            hideView.enableView(textViewDescriptionTop)
            textViewTitleTop.text = "Ranking - Câmara"
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
            imageViewFilter.setOnClickListener {
                hideFilter = if (hideFilter) {
                    showFilters(true)
                    modifyFilter(true)
                    false
                } else {
                    showFilters(false)
                    modifyFilter(false)
                    true
                }
            }
            imageViewOptionOrQuestion.apply { hideView.disableView(this) }
        }
    }

    private fun modifyElementTop(){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text = getString(R.string.ranking_de_gastos)
                hideView.enableView(textViewDescriptionTop)
            }
        }
    }

    private fun observerGastoCamara() {

        val internet = this.let { validationInternet.validationInternet(it) }
        if (internet){
            viewModel.rankingCamara(anoSelect).observe(this){
                it?.let { result ->
                    when (result) {
                        is ResultRankingGeralCamara.Success -> {
                            result.dado?.let { gastos ->
                                listGastoGeralDeputado = gastos.ranking as ArrayList
                                disableProgressAndText()
                                showFilters(true)
                                modifyTextTop("Ranking Gastos - $anoSelect")
                                adapter.updateData(listGastoGeralDeputado)
                            }
                        }
                        is ResultRankingGeralCamara.Error -> {
                            showErrorApi()
                        }
                        is ResultRankingGeralCamara.ErrorConnection -> {
                            showErrorApi()
                        }
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
        else adapter.filterList(viewDisabled.text as String)
        modifyTextTop("Ranking Gastos - $textPartido - $anoSelect")
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {
        if (anoSelect != viewClicked.text){
            enableProgressAndText()
            adapter.updateData(arrayListOf())
            anoSelect = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
            observerGastoCamara()
        }
        viewClicked.isChecked = true
    }

    private fun disableProgressAndText(){
        hideView.run {
            binding.layoutProgressAndText.run {
                disableView(progressActive)
                disableView(textNotValue)
            }
        }
    }

    private fun enableProgressAndText(){
        hideView.run {
            binding.layoutProgressAndText.run {
                enableView(progressActive)
                disableView(textNotValue)
            }
        }
    }

    override fun clickRanking(id: String) {
        val intent = Intent(this, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun showErrorApi(){
        binding.layoutProgressAndText.apply {
            animeView.crossFade(textNotValue, true)
            hideView.disableView(progressActive)
            textNotValue.text = getString(R.string.erro_api_camara)
        }
    }

    private fun showFilters(boolean: Boolean){
        animeView.crossFade(binding.framePartidos, boolean)
        animeView.crossFade(binding.frameYearRanking, boolean)
    }

    private fun modifyFilter(boolean: Boolean){
        binding.layoutTop.imageViewFilter.run {
            if (boolean) this.setImageResource(R.drawable.ic_no_filter_dark)
            else this.setImageResource(R.drawable.ic_filter_dark)
        }
    }

    override fun notification() {
        Toast.makeText(applicationContext, getString(R.string.nao_encontrado_dep_partido),
            Toast.LENGTH_SHORT).show()
    }

    private fun modifyTextTop(textResult: String){
        binding.run {
            layoutTop.textViewDescriptionTop.run {
                text = textResult
                this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_votacao))
            }
        }
    }
}