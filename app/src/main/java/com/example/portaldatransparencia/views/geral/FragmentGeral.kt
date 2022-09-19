package com.example.portaldatransparencia.views.geral

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.dataclass.Occupation
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.example.portaldatransparencia.remote.ResultOccupationRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.deputado.DeputadoViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGeral: Fragment(R.layout.fragment_geral) {

    private var binding: FragmentGeralBinding? = null
    private val viewModel: DeputadoViewModel by viewModel()
    private val viewModelOccupation: OccupationViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val calculateAge: CalculateAge by inject()
    private lateinit var id: String
    private var deputadoOccupation = "deputado"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralBinding.bind(view)
        id = securityPreferences.getStoredString("id")
        observerDeputado()
        observerOccupation()
    }

    private fun observerOccupation() {
        viewModelOccupation.occupationDeputado(id).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultOccupationRequest.Success -> {
                        result.dado?.let { occupation ->
                            addElementOccupation(occupation.dados)
                        }
                    }
                    is ResultOccupationRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultOccupationRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun observerDeputado() {

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
        val deputado: String
        val oDeputado: String
        if (dados.sexo == "M") {
            deputado = "Deputado Federal"
            oDeputado = "o deputado"
            deputadoOccupation = "deputado"
        } else {
            deputado = "Deputada Federal"
            oDeputado = "a deputada"
            deputadoOccupation = "deputada"
        }
        val age = calculateAge.age(dados.dataNascimento)
        val status = dados.ultimoStatus
        val redeSocial = dados.redeSocial

        binding?.run {
            textAcompanheRede.text = "Acompanhe "+oDeputado+" nas redes sociais"
            (status.nome+", "+age+" anos, nascido na cidade de "+
                    dados.municipioNascimento+" - "+dados.ufNascimento+", é "+
                    deputado+" em "+status.situacao+", filiado ao partido "+
                    status.siglaPartido+" pelo estado de "+ status.siglaUf+
                    ", sua escolaridade atual é " +dados.escolaridade+".")
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
                binding.run {

                }
            }
        }
    }

    private fun addElementOccupation(dados: List<Occupation>) {
        val occupation = dados[0]
        if (dados.isNotEmpty()){
            binding?.textOccupationDescription?.text = "Trabalhou como "+occupation.titulo+" na "+
                    occupation.entidade+" em "+occupation.entidadeUF+" - "+occupation.entidadePais+
                    ", no ano de "+occupation.anoInicio+"."
            binding?.textWork?.text = "Seu trabalho antes de ser $deputadoOccupation"
        }
        else {
            binding?.textOccupationDescription?.text = "Não tem informação da última ocupação"
        }
    }

}