package com.example.portaldatransparencia.views.activity.ranking.senado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.databinding.LayoutRankingBinding
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.dataclass.ListParlamentar
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.remote.ResultGastoGeralSenado
import com.example.portaldatransparencia.remote.ResultSenadoRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityRankingSenado: AppCompatActivity() {

    private val binding by lazy { LayoutRankingBinding.inflate(layoutInflater) }
    private val viewModel: RankingViewModelSenado by viewModel()
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var adapter: GastoGeralAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var listSenadores: ArrayList<Parlamentar>
    private lateinit var listGastoGeralSenador: ArrayList<ListParlamentar>
    private var listAdpterSenador: ArrayList<ListParlamentar> = arrayListOf()
    private lateinit var gastoSenado: GastoGeralDataClass
    private lateinit var sizeSenador: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        modifyTextTitleAndListenerBack()
        recycler()
        observerCamara()
    }

    private fun modifyTextTitleAndListenerBack() {
        binding.layoutTop.run {
            textViewTitleTop.text = getString(R.string.senado_federal)
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    private fun recycler(){
        val recycler = binding.recyclerRancking
        adapter = GastoGeralAdapter(formatValor, applicationContext)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerCamara(){
        senadoViewModel.searchDataSenado().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultSenadoRequest.Success -> {
                        result.dado?.let { senado ->
                            val list = senado.listaParlamentarEmExercicio.parlamentares.parlamentar
                            sizeSenador = list.size.toString()
                            listSenadores = list as ArrayList
                            observerRankingCamara()
                        }
                    }
                    is ResultSenadoRequest.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultSenadoRequest.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun observerRankingCamara() {

        viewModel.rankingSenado().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralSenado.Success -> {
                        result.dado?.let { gastos ->
                            listGastoGeralSenador = gastos.gastoGeral.listSenador as ArrayList
                            gastoSenado = gastos
                            processListCamara()
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

    private fun processListCamara(){

        listSenadores.forEach { deputado ->
            deputado.identificacaoParlamentar.run {
                val id = this.codigoParlamentar
                val partido = siglaPartidoParlamentar
                val uf = ufParlamentar
                val nome = nomeParlamentar
                val urlFoto = urlFotoParlamentar

                listGastoGeralSenador.forEach{
                    val n = it.nome
                    if (n == nome){
                        it.id = id
                        it.partido = partido
                        it.uf = uf
                        it.urlFoto = urlFoto
                    }
                }
            }
        }
        listGastoGeralSenador.forEach {
            val item = it
            if (item.urlFoto != null){
                listAdpterSenador.add(item)
            }
        }
        binding.run {
            progressRancking.let { hideView.disableView(it) }
            textResultRacking.let { hideView.disableView(it) }
        }
        adapter.updateData(listAdpterSenador)
    }
}