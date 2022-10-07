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
import com.example.portaldatransparencia.remote.ResultGastoGeralRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.FormatValor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastoGeral: Fragment(R.layout.fragment_mais) {

    private var binding: FragmentMaisBinding? = null
    private val viewModel: GastoGeralViewModel by viewModel()
    private val converterValor = FormatValor()
    private lateinit var adapter: GastoGeralAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMaisBinding.bind(view)
        id = securityPreferences.getString("id")

        recyclerView()
        observer()
    }

    private fun recyclerView() {

        val recyclerSenador = binding!!.recyclerRanckingSenador
        adapter = GastoGeralAdapter(FormatValor())
        recyclerSenador.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerSenador.adapter = adapter
    }

    private fun observer() {

        viewModel.gastoGeral().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralRequest.Success -> {
                        result.dado?.let { gasto ->
                            addElement(gasto)
                            adapter.updateData(gasto.gastoGeral.listSenador)
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

    private fun addElement(dados: GastoGeralDataClass) {
        binding?.run {
            val total = converterValor.formatValor(dados.gastoGeral.totalGeral.toDouble())
            textViewGastoTotalSenador.text = "R$ ${total}"
        }
    }

}