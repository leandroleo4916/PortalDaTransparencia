package com.example.portaldatransparencia.views.senador.geral_senador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralSenadorBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.remote.ResultCargosRequest
import com.example.portaldatransparencia.remote.ResultSenadorRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.senador.SenadorViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGeralSenador: Fragment(R.layout.fragment_geral_senador) {

    private var binding: FragmentGeralSenadorBinding? = null
    private val viewModel: GeralSenadorViewModel by viewModel()
    private val senadorViewModel: SenadorViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val calculateAge: CalculateAge by inject()
    private val statusView: EnableDisableView by inject()
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralSenadorBinding.bind(view)
        id = securityPreferences.getString("id")
        observerSenador()
        observer()
    }

    private fun observer() {
        senadorViewModel.searchDataSenador(id).observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is ResultSenadorRequest.Success -> {
                        result.dado?.let { senador ->
                            addElementView(senador.detalheParlamentar.parlamentar)
                        }
                    }
                    is ResultSenadorRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultSenadorRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun observerSenador() {

        viewModel.cargosSenador(id).observe(viewLifecycleOwner){
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

    private fun addElementView(dados: ParlamentarItem) {

        val dadosBasicos = dados.dadosBasicosParlamentar
        val detalhes = dados.identificacaoParlamentar
        val sexo = if (detalhes.sexoParlamentar == "Masculino") "Senador"
        else "Senadora"

        val age = calculateAge.age(dadosBasicos.dataNascimento)

        binding?.run {
            (detalhes.nomeCompletoParlamentar+", "+age+" anos, nascido na cidade de "+
                    dadosBasicos.naturalidade+" - "+dadosBasicos.ufNaturalidade+", é "+
                    sexo+" em exércicio, filiado ao partido "+ detalhes.siglaPartidoParlamentar+
                    " pelo estado de "+ detalhes.ufParlamentar+".")
                .also { textGeralInformation.text = it }
            statusView.enableView(textGeralInformation)

            textSitePessoal.text = detalhes.urlPaginaParticular+" >"
            textSiteSenado.text = detalhes.urlPaginaParlamentar+" >"
            textGeralPredio.text = dadosBasicos.enderecoParlamentar
            textGeralAndar.text = detalhes.emailParlamentar

            val phone = dados.telefones.telefone.toString()
            textGeralPhone.text =
                if (phone.contains("[{")) phone.substring(17, 25)
                else phone.substring(16, 24)

            val https = "https:/"
            val urlFoto = detalhes.urlFotoParlamentar.split(":/")
            val photo = https+urlFoto[1]
            Glide.with(requireContext())
                .load(photo)
                .circleCrop()
                .into(iconSenadorGeral)
            statusView.enableView(iconSenadorGeral)
        }
        listenerSite(detalhes)
    }

    private fun addElementCargo(dados: List<Cargo>) {
        val cargo = dados[0]
        binding?.run {
            ("Exerceu o cargo de "+cargo.descricaoCargo).also { textCargoSenador.text = it }
            (cargo.identificacaoComissao.nomeComissao).also { textCargoDescription.text = it }
        }
    }

    private fun listenerSite(site: IdentificacaoParlamentarItem){
        binding?.run {
            textSitePessoal.setOnClickListener { choose(site.urlPaginaParticular) }
            textSiteSenado.setOnClickListener { choose(site.urlPaginaParlamentar) }
        }
    }

    private fun choose(social: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(social))
        startActivity(browserIntent)
    }
}