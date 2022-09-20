package com.example.portaldatransparencia.views.geral

import android.content.Intent
import android.net.Uri
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
    private var deputadoOccupation = "deputado"
    private lateinit var id: String

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
                            addElementRedeSocial(deputado.dados)
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

        binding?.run {
            ("Acompanhe $oDeputado nas redes sociais").also { textAcompanheRede.text = it }
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
        }
    }

    private fun addElementOccupation(dados: List<Occupation>) {
        val occupation = dados[0]
        "Seu trabalho antes de ser $deputadoOccupation".also { binding?.textWork?.text = it }
        if (occupation.titulo != null){
            ("Trabalhou como "+occupation.titulo+" na "+ occupation.entidade+" em "+
                    occupation.entidadeUF+" - "+occupation.entidadePais+ ", no ano de "+
                    occupation.anoInicio+".").also { binding?.textOccupationDescription?.text = it }
        }
    }

    private fun addElementRedeSocial(dados: Dados){
        var facebook = ""
        var instagram = ""
        var twitter = ""
        var youtube = ""

        if (dados.redeSocial.isNotEmpty()){
            dados.redeSocial.forEach {
                if (it.contains("facebook")) facebook = it
                else if (it.contains("instagram")) instagram = it
                else if (it.contains("twitter")) twitter = it
                else if (it.contains("youtube")) youtube = it
            }
            binding?.textInformationRede?.visibility = View.INVISIBLE
            listenerRedeSocial(facebook, instagram, twitter, youtube)
        }

        if (facebook.isNotEmpty()) binding?.constraint1?.visibility = View.VISIBLE
        if (instagram.isNotEmpty()) binding?.constraint2?.visibility = View.VISIBLE
        if (twitter.isNotEmpty()) binding?.constraint3?.visibility = View.VISIBLE
        if (youtube.isNotEmpty()) binding?.constraint4?.visibility = View.VISIBLE
    }

    private fun listenerRedeSocial(f: String, i: String, t: String, y: String){
        binding?.run {
            constraint1.setOnClickListener { choose(f) }
            constraint2.setOnClickListener { choose(i) }
            constraint3.setOnClickListener { choose(t) }
            constraint4.setOnClickListener { choose(y) }
        }
    }

    private fun choose(social: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(social))
        startActivity(browserIntent)
    }
}