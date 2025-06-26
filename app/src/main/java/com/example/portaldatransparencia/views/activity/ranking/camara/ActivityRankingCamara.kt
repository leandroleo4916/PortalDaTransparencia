package com.example.portaldatransparencia.views.activity.ranking.camara

import android.content.Intent
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
import com.example.portaldatransparencia.repository.ResultRankingGeralCamara
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.camara.deputado.DeputadoActivity
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
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
    private lateinit var chipSelectedYear: Chip
    private lateinit var chipEnabledPart: Chip
    private var anoSelect = "Todos"
    private var hideFilter = false
    private var textPartido = "TODOS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableChipAll()
        id = securityPreferences.getString("id")
        showFilters()
        modifyElementTop()
        listenerChip()
        recycler()
        listener()
        observerGastoCamara()
    }
    
    private fun enableChipAll(){
        binding.run {
            layoutYear.chipAll.apply {
                chipSelectedYear = this
                hideView.enableView(this)
            }
            layoutYear.chip2023.isChecked = false
            layoutGroupPartidos.run {
                hideView.disableView(scrollState)
            }
            chipEnabledPart = layoutGroupPartidos.chipAll
        }
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
                    showFilters()
                    modifyFilter(true)
                    false
                } else {
                    showFilters()
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
                chipAll.setOnClickListener  { modify(chipSelectedYear, chipAll) }
                chip2025.setOnClickListener { modify(chipSelectedYear, chip2025) }
                chip2024.setOnClickListener { modify(chipSelectedYear, chip2024) }
                chip2023.setOnClickListener { modify(chipSelectedYear, chip2023) }
                chip2022.setOnClickListener { modify(chipSelectedYear, chip2022) }
                chip2021.setOnClickListener { modify(chipSelectedYear, chip2021) }
                chip2020.setOnClickListener { modify(chipSelectedYear, chip2020) }
                chip2019.setOnClickListener { modify(chipSelectedYear, chip2019) }
                chip2018.setOnClickListener { modify(chipSelectedYear, chip2018) }
                chip2017.setOnClickListener { modify(chipSelectedYear, chip2017) }
                chip2016.setOnClickListener { modify(chipSelectedYear, chip2016) }
                chip2015.setOnClickListener { modify(chipSelectedYear, chip2015) }
                chip2014.setOnClickListener { modify(chipSelectedYear, chip2014) }
                chip2013.setOnClickListener { modify(chipSelectedYear, chip2013) }
                chip2012.setOnClickListener { modify(chipSelectedYear, chip2012) }
                chip2011.setOnClickListener { modify(chipSelectedYear, chip2011) }
            }
            layoutGroupPartidos.run {
                chipAll.setOnClickListener { modifyChipPartido(chipEnabledPart, chipAll) }
                chipAvante.setOnClickListener { modifyChipPartido(chipEnabledPart, chipAvante) }
                chipCidadania.setOnClickListener { modifyChipPartido(chipEnabledPart, chipCidadania) }
                chipPv.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPv) }
                chipDem.setOnClickListener { modifyChipPartido(chipEnabledPart, chipDem) }
                chipMdb.setOnClickListener { modifyChipPartido(chipEnabledPart, chipMdb) }
                chipNovo.setOnClickListener { modifyChipPartido(chipEnabledPart, chipNovo) }
                chipPatri.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPatri) }
                chipPatriota.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPatriota) }
                chipPsol.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPsol) }
                chipPcdob.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPcdob) }
                chipPsd.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPsd) }
                chipPdt.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPdt) }
                chipPhs.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPhs) }
                chipPl.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPl) }
                chipPros.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPros) }
                chipPsc.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPsc) }
                chipPsb.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPsb) }
                chipPsdb.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPsdb) }
                chipSolidariedade.setOnClickListener { modifyChipPartido(chipEnabledPart, chipSolidariedade) }
                chipPodemos.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPodemos) }
                chipPp.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPp) }
                chipPt.setOnClickListener { modifyChipPartido(chipEnabledPart, chipPt) }
                chipRepublicanos.setOnClickListener { modifyChipPartido(chipEnabledPart, chipRepublicanos) }
                chipUniao.setOnClickListener { modifyChipPartido(chipEnabledPart, chipUniao) }
            }
        }
    }

    private fun modifyChipPartido(chipPart: Chip, viewClicked: Chip) {
        if (textPartido != viewClicked.text){
            enableProgressAndText()
            textPartido = viewClicked.text as String
            chipPart.isChecked = false
            viewClicked.isChecked = true
            chipEnabledPart = viewClicked

            modifyTextTop("Ranking Gastos - $textPartido")
            adapter.filterList(textPartido)
            disableProgressAndText()
        }
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {
        if (anoSelect != viewClicked.text){
            enableProgressAndText()
            adapter.updateData(arrayListOf())
            anoSelect = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelectedYear = viewClicked
            observerGastoCamara()

            if (chipEnabledPart.text != "TODOS") {
                chipEnabledPart.isChecked = false
                chipEnabledPart = binding.layoutGroupPartidos.chipAll
                chipEnabledPart.isChecked = true
                textPartido = "TODOS"
            }
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

    override fun clickRanking(id: String, nome: String) {
        val intent = Intent(this, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun showErrorApi(){
        binding.layoutProgressAndText.apply {
            animeView.crossFade(textNotValue)
            hideView.disableView(progressActive)
            textNotValue.text = getString(R.string.erro_api_camara)
        }
    }

    private fun showFilters(){
        animeView.crossFade(binding.framePartidos)
        animeView.crossFade(binding.frameYearRanking)
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