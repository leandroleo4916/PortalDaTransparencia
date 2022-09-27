package com.example.portaldatransparencia.views.senador.geral_senador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralSenadorBinding
import com.example.portaldatransparencia.dataclass.Cargo
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.remote.ResultCargosRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGeralSenador: Fragment(R.layout.fragment_geral_senador) {

    private var binding: FragmentGeralSenadorBinding? = null
    private val viewModel: GeralSenadorViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val calculateAge: CalculateAge by inject()
    private val statusView: EnableDisableView by inject()
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralSenadorBinding.bind(view)
        id = securityPreferences.getStoredString("id")
        observerSenador()
        observerDescription()
    }

    private fun observerDescription() {}

    private fun observerSenador() {

        viewModel.cargosSenador().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultCargosRequest.Success -> {
                        result.dado?.let { senador ->
                            statusView.disableView(binding!!.progressGeral)
                            addElementCargo(senador.cargoParlamentar.parlamentar.cargos.cargo)
                        }
                    }
                    is ResultCargosRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultCargosRequest.ErrorConnection -> {
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
        } else {
            deputado = "Deputada Federal"
            oDeputado = "a deputada"
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
            textGeralPhone.text = status.gabinete?.telefone ?: "Não informado"

            Glide.with(requireContext())
                .load(dados.ultimoStatus.urlFoto)
                .circleCrop()
                .into(iconDeputadoGeral)
        }
    }

    private fun addElementCargo(dados: List<Cargo>) {
        val cargo = dados[0]

        binding?.run {
            ("Exerceu o cargo de "+cargo.descricaoCargo).also { textCargoSenador.text = it }
            (cargo.identificacaoComissao.nomeComissao).also { textCargoDescription.text = it }
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

        binding?.run {
            if (facebook.isNotEmpty()) statusView.enableView(constraint1)
            if (instagram.isNotEmpty()) statusView.enableView(constraint2)
            if (twitter.isNotEmpty()) statusView.enableView(constraint3)
            if (youtube.isNotEmpty()) statusView.enableView(constraint4)
        }
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