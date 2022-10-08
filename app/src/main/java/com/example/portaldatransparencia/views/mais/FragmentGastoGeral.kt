package com.example.portaldatransparencia.views.mais

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.dataclass.ListSenador
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.remote.ResultGastoGeralRequest
import com.example.portaldatransparencia.remote.ResultSenadoRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.senado.SenadoViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.FormatValor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastoGeral: Fragment(R.layout.fragment_mais) {

    private var binding: FragmentMaisBinding? = null
    private val viewModel: GastoGeralViewModel by viewModel()
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private val converterValor = FormatValor()
    private lateinit var adapter: GastoGeralAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var listSenador: ArrayList<Parlamentar>
    private lateinit var listGastoGeral: ArrayList<ListSenador>
    private var listAdpter: ArrayList<ListSenador> = arrayListOf()
    private lateinit var gasto: GastoGeralDataClass
    private lateinit var sizeSenador: String
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMaisBinding.bind(view)
        id = securityPreferences.getString("id")

        recyclerView()
        observerSenado()
    }

    private fun recyclerView() {

        val recyclerSenador = binding!!.recyclerRanckingSenador
        adapter = GastoGeralAdapter(FormatValor())
        recyclerSenador.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerSenador.adapter = adapter
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
                            observer()
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

    private fun observer() {

        viewModel.gastoGeral().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralRequest.Success -> {
                        result.dado?.let { gasto ->
                            listGastoGeral = gasto.gastoGeral.listSenador as ArrayList
                            this.gasto = gasto
                            processList()
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

    private fun processList(){

        addElement(gasto)
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
        binding?.progressRanckingSenador?.let { hideView.disableView(it) }
        binding?.progressRanckingDeputado?.let { hideView.disableView(it) }
        binding?.textResultRackingDeputado?.let { hideView.enableView(it) }
        adapter.updateData(listAdpter)
    }

    private fun addElement(dados: GastoGeralDataClass) {
        binding?.run {
            val total = converterValor.formatValor(dados.gastoGeral.totalGeral.toDouble())
            textViewGastoTotalSenador.text = "R$ ${total}"
            textViewSenador.text = "$sizeSenador Senadores em exércicio"
        }
    }

}