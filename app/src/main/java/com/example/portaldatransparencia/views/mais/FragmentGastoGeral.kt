package com.example.portaldatransparencia.views.mais

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.adapter.GastoGeralAdapterCamara
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara
import com.example.portaldatransparencia.remote.ResultGastoGeralRequest
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.remote.ResultSenadoRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.util.FormatValor
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastoGeral: Fragment(R.layout.fragment_mais) {

    private var binding: FragmentMaisBinding? = null
    private val viewModel: GastoGeralViewModel by viewModel()
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val camaraViewModel: CamaraViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private val converterValor = FormatValor()
    private val formatValor = FormaterValueBilhoes()
    private lateinit var adapter: GastoGeralAdapter
    private lateinit var adapterCamara: GastoGeralAdapterCamara
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var listSenador: ArrayList<Parlamentar>
    private lateinit var listDeputados: ArrayList<Dado>
    private lateinit var listGastoGeral: ArrayList<ListSenador>
    private lateinit var listGastoGeralDeputado: ArrayList<ListDeputado>
    private var listAdpter: ArrayList<ListSenador> = arrayListOf()
    private var listAdpterDeputado: ArrayList<ListDeputado> = arrayListOf()
    private lateinit var gasto: GastoGeralDataClass
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var sizeSenador: String
    private lateinit var sizeDeputado: String
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMaisBinding.bind(view)
        id = securityPreferences.getString("id")

        recyclerViewSenado()
        recyclerViewCamara()
        observerSenado()
        observerCamara()
    }

    private fun recyclerViewSenado() {
        val recyclerSenador = binding?.layoutUnic!!.recyclerRanckingSenado
        adapter = GastoGeralAdapter(FormatValor())
        recyclerSenador.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerSenador.adapter = adapter
    }

    private fun recyclerViewCamara() {
        val recyclerDeputado = binding?.layoutUnic!!.recyclerRanckingDeputado
        adapterCamara = GastoGeralAdapterCamara(FormatValor())
        recyclerDeputado.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerDeputado.adapter = adapterCamara
    }

    private fun observerSenado(){
        senadoViewModel.searchDataSenado().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultSenadoRequest.Success -> {
                        result.dado?.let { senado ->
                            val list = senado.listaParlamentarEmExercicio.parlamentares.parlamentar
                            sizeSenador = list.size.toString()
                            listSenador = list as ArrayList
                            observerGastoSenado()
                        }
                    }
                    is ResultSenadoRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultSenadoRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun observerCamara(){
        camaraViewModel.searchData(ordenarPor = "nome").observe(viewLifecycleOwner){
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
                        result.exception.message?.let { it -> }
                    }
                    is ResultRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun observerGastoSenado() {

        viewModel.gastoGeral().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralRequest.Success -> {
                        result.dado?.let { gasto ->
                            listGastoGeral = gasto.gastoGeral.listSenador as ArrayList
                            this.gasto = gasto
                            processListSenado()
                        }
                    }
                    is ResultGastoGeralRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultGastoGeralRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun observerGastoCamara() {

        viewModel.gastoGeralCamara().observe(viewLifecycleOwner){
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
                        result.exception.message?.let { it -> }
                    }
                    is ResultGastoGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun processListSenado(){

        addElementSenado(gasto)
        listSenador.forEach { senador ->
            val nome = senador.identificacaoParlamentar.nomeParlamentar
            val urlFoto = senador.identificacaoParlamentar.urlFotoParlamentar
            val https = "https:/"
            listGastoGeral.forEach{
                val n = it.nome
                if (n == nome){
                    val url = urlFoto.split(":/")
                    val photo = https + url[1]
                    it.urlFoto = photo
                }
            }
        }
        listGastoGeral.forEach {
            val item = it
            if (item.urlFoto != null){
                listAdpter.add(item)
            }
        }
        binding?.layoutUnic?.run {
            progressRanckingSenador.let { hideView.disableView(it) }
            textResultRackingSenador.let { hideView.disableView(it) }
        }
        adapter.updateData(listAdpter)
    }

    private fun processListCamara(){

        addElementCamara(gastoCamara)
        listDeputados.forEach { deputado ->
            val nome = deputado.nome
            val urlFoto = deputado.urlFoto
            listGastoGeralDeputado.forEach{
                val n = it.nome
                if (n == nome){
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
        binding?.layoutUnic?.run {
            progressRancking.let { hideView.disableView(it) }
            textResultRackingDeputado.let { hideView.disableView(it) }
        }
        adapterCamara.updateData(listAdpterDeputado)
    }

    private fun addElementSenado(dados: GastoGeralDataClass) {
        binding?.layoutUnic?.run {
            val total = formatValor.formatValor(dados.gastoGeral.totalGeral.toDouble())
            textViewGastoSenadores.text = total
        }
    }

    private fun addElementCamara(dados: GastoGeralCamara) {
        binding?.layoutUnic?.run {
            val total = formatValor.formatValor(dados.gastoGeral.totalGeral.toDouble())
            textViewGastoDeputados.text = total
        }
    }

}