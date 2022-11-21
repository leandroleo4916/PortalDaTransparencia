package com.example.portaldatransparencia.views.activity.ranking.camara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.databinding.LayoutRankingBinding
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.ListParlamentar
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityRankingCamara: AppCompatActivity() {

    private val binding by lazy { LayoutRankingBinding.inflate(layoutInflater) }
    private val viewModel: RankingViewModelCamara by viewModel()
    private val camaraViewModel: CamaraViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var adapter: GastoGeralAdapter
    private lateinit var listDeputados: ArrayList<Dado>
    private lateinit var listGastoGeralDeputado: ArrayList<ListParlamentar>
    private var listAdpterDeputado: ArrayList<ListParlamentar> = arrayListOf()
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var sizeDeputado: String
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = securityPreferences.getString("id")
        recycler()
        observerCamara()
    }
    private fun recycler(){
        val recycler = binding.recyclerRancking
        adapter = GastoGeralAdapter(formatValor)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerCamara(){
        camaraViewModel.searchData().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { camara ->
                            val list = camara.dados
                            sizeDeputado = list.size.toString()
                            listDeputados = list as ArrayList
                            observerGastoCamara()
                        }
                    }
                    is ResultRequest.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultRequest.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun observerGastoCamara() {

        viewModel.rankingCamara().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            listGastoGeralDeputado = gastos.gastoGeral.listDeputado as ArrayList
                            gastoCamara = gastos
                            processListCamara()
                        }
                    }
                    is ResultGastoGeralCamara.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultGastoGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun processListCamara(){

        listDeputados.forEach { deputado ->
            val id = deputado.id
            val partido = deputado.siglaPartido
            val uf = deputado.siglaUf
            val nome = deputado.nome
            val urlFoto = deputado.urlFoto
            listGastoGeralDeputado.forEach{
                val n = it.nome
                if (n == nome){
                    it.id = id.toString()
                    it.partido = partido
                    it.uf = uf
                    it.urlFoto = urlFoto
                }
            }
        }
        listGastoGeralDeputado.forEach {
            val item = it
            if (item.urlFoto != null){
                listAdpterDeputado.add(item)
            }
        }
        binding.run {
            progressRancking.let { hideView.disableView(it) }
            textResultRacking.let { hideView.disableView(it) }
        }
        adapter.updateData(listAdpterDeputado)
    }
}