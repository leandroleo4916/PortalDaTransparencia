package com.example.portaldatransparencia.views.geral

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentFrenteBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.deputado.DeputadoViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGeral: Fragment(R.layout.fragment_frente) {

    private var binding: FragmentFrenteBinding? = null
    private val viewModel: DeputadoViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFrenteBinding.bind(view)
        id = securityPreferences.getStoredString("id")
        observer()
    }

    private fun observer() {

        viewModel.searchDataDeputado(id).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultIdRequest.Success -> {
                        result.dado?.let { deputado ->
                            binding?.progressFront?.visibility = View.GONE
                            addElementView(deputado.dados)
                        }
                    }
                    is ResultIdRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultIdRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun addElementView(dados: Dados) {

    }

}