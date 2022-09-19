package com.example.portaldatransparencia.views.geral

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.deputado.DeputadoViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGeral: Fragment(R.layout.fragment_geral) {

    private var binding: FragmentGeralBinding? = null
    private val viewModel: DeputadoViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val calculateAge: CalculateAge by inject()
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralBinding.bind(view)
        id = securityPreferences.getStoredString("id")
        observer()
    }

    private fun observer() {

        viewModel.searchDataDeputado(id).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultIdRequest.Success -> {
                        result.dado?.let { deputado ->
                            binding?.progressGeral?.visibility = View.GONE
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
        val deputado =
            if (dados.sexo == "M") "Deputado Federal"
            else "Deputada Federal"
        val age = calculateAge.age(dados.dataNascimento)
        val status = dados.ultimoStatus
        val redeSocial = dados.redeSocial

        binding?.run {
            (status.nome+", "+age+" anos, nascido na cidade de "+
                    dados.municipioNascimento+" - "+dados.ufNascimento+", é "+
                    deputado+", filiado ao partido "+ status.siglaPartido+" pelo estado de "+
                    status.siglaUf+", sua escolaridade atual: " +dados.escolaridade+".")
                .also { textGeralInformation.text = it }
            textGeralPredio.text = "Predio: "+ (status.gabinete?.predio ?: "Não informado")
            textGeralAndar.text = "Andar: "+ (status.gabinete?.andar ?: "Não informado")
            textGeralSala.text = "Sala: "+ (status.gabinete?.sala ?: "Não informado")
            textGeralPhone.text = "Telefone: "+ (status.gabinete?.telefone ?: "Não informado")

            Glide.with(requireContext())
                .load(dados.ultimoStatus.urlFoto)
                .circleCrop()
                .into(iconDeputadoGeral)

            if (redeSocial.isNotEmpty()){

            }
        }
    }

}