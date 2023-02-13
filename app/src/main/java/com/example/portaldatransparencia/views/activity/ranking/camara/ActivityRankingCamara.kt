package com.example.portaldatransparencia.views.activity.ranking.camara

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.databinding.LayoutRankingBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.interfaces.IClickOpenDeputadoRanking
import com.example.portaldatransparencia.network.ApiServiceMain
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.repository.ResultRankingGeralCamara
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.camara.deputado.DeputadoActivity
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRankingCamara: AppCompatActivity(), IClickOpenDeputadoRanking {

    private val binding by lazy { LayoutRankingBinding.inflate(layoutInflater) }
    private val viewModel: RankingViewModelCamara by viewModel()
    private val camaraViewModel: CamaraViewModel by viewModel()
    private val validationInternet: ValidationInternet by inject()
    private val hideView: EnableDisableView by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var adapter: GastoGeralAdapter
    private lateinit var listDeputados: ArrayList<Dado>
    private lateinit var listGastoGeralDeputado: ArrayList<Ranking>
    private var listAdpterDeputado: ArrayList<ListParlamentar> = arrayListOf()
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var sizeDeputado: String
    private lateinit var id: String
    private val ano = "Geral"
    private lateinit var chipSelected: Chip
    private var anoSelect = "Todos"

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
        observerCamara()
        listener()
    }

    private fun listener() {
        binding.layoutTop.run {
            hideView.disableView(imageViewFilter)
            hideView.enableView(textViewDescriptionTop)
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    private fun modifyElementTop(){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text = getString(R.string.ranking_de_gastos)
                hideView.enableView(textViewDescriptionTop)
                hideView.disableView(imageViewFilter)
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
            }
        }
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {
        if (ano != viewClicked.text){
            disableProgressAndText()
            adapter.updateData(arrayListOf())
            anoSelect = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
            observerGastoCamara()
        }
    }

    private fun recycler(){
        val recycler = binding.recyclerRancking
        adapter = GastoGeralAdapter(formatValor, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerCamara(){

        val internet = this.let { validationInternet.validationInternet(it) }
        val retrofit = Retrofit.createService(ApiServiceMain::class.java)
        val call: Call<MainDataClass> = retrofit.getDeputados(ordem = "ASC", "nome")
        if (internet){
            call.enqueue(object: Callback<MainDataClass> {
                override fun onResponse(call: Call<MainDataClass>, res: Response<MainDataClass>) {
                    when (res.code()) {
                        200 -> {
                            if (res.body()!!.dados.isNotEmpty()){
                                val list = res.body()!!.dados
                                sizeDeputado = list.size.toString()
                                listDeputados = list as ArrayList
                                observerGastoCamara()
                            }
                        }
                        429 -> observerCamara()
                    }
                }
                override fun onFailure(call: Call<MainDataClass>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        else {
            //showValidationInternet(R.string.verifique_sua_internet)
        }
    }

    private fun observerGastoCamara() {

        viewModel.rankingCamara(ano).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRankingGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            listGastoGeralDeputado = gastos.ranking as ArrayList
                            processListCamara()
                        }
                    }
                    is ResultRankingGeralCamara.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultRankingGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun processListCamara(){
        disableProgressAndText()
        adapter.updateData(listGastoGeralDeputado)
    }

    private fun disableProgressAndText(){
        hideView.run {
            binding.layoutProgressAndText.run {
                disableView(progressActive)
                disableView(textNotValue)
            }
        }
    }

    override fun clickRanking(id: String) {
        val intent = Intent(this, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}