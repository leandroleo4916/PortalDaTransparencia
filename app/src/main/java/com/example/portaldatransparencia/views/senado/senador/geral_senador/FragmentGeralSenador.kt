package com.example.portaldatransparencia.views.senado.senador.geral_senador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralSenadorBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.repository.ResultCargosRequest
import com.example.portaldatransparencia.repository.ResultSenadorRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.util.CotaState
import com.example.portaldatransparencia.util.FormatValueFloat
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.senado.senador.SenadorViewModel
import com.example.portaldatransparencia.views.view_generics.CreateDialogClass
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGeralSenador: Fragment(R.layout.fragment_geral_senador) {

    private var binding: FragmentGeralSenadorBinding? = null
    private val viewModel: GeralSenadorViewModel by viewModel()
    private val senadorViewModel: SenadorViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val verifyInternet: ValidationInternet by inject()
    private val calculateAge: CalculateAge by inject()
    private val statusView: EnableDisableView by inject()
    private val createDialog: CreateDialogClass by inject()
    private lateinit var dadosBasicos: DadosBasicosParlamentar
    private lateinit var detalhes: IdentificacaoParlamentarItem
    private val formatValue: FormatValueFloat by inject()
    private val cotaState: CotaState by inject()
    private lateinit var id: String
    private lateinit var create: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralSenadorBinding.bind(view)
        id = securityPreferences.getString("id")
        observerSenador()
        observer()
    }

    private fun observer() {

        val internet = verifyInternet.validationInternet(requireContext().applicationContext)
        if (internet){
            senadorViewModel.searchDataSenador(id).observe(viewLifecycleOwner) {
                it?.let { result ->
                    when (result) {
                        is ResultSenadorRequest.Success -> {
                            result.dado?.let { senador ->
                                senador.detalheParlamentar.parlamentar.run {
                                    dadosBasicos = this.dadosBasicosParlamentar
                                    detalhes = this.identificacaoParlamentar
                                    addValueToLimitCotas(this.identificacaoParlamentar.ufParlamentar)
                                    addElementSiteBlog()
                                    addElementView()
                                    addElementAddressContact(this.telefones.telefone.toString())
                                }
                            }
                        }
                        is ResultSenadorRequest.Error -> {
                            notValue(R.string.erro_api_senado)
                        }
                        is ResultSenadorRequest.ErrorConnection -> {
                            notValue(R.string.erro_api_senado)
                        }
                    }
                }
            }
        }
        else notValue(R.string.verifique_sua_internet)
    }

    private fun notValue(text: Int){
        binding?.layoutProgressAndText?.run {
            textNotValue.text = getString(text)
            statusView.enableView(textNotValue)
            statusView.disableView(progressActive)
        }
    }

    private fun observerSenador() {

        viewModel.cargosSenador(id).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultCargosRequest.Success -> {
                        result.dado?.let { senador ->
                            addElementCargo(senador.cargoParlamentar.parlamentar.cargos.cargo)
                        }
                    }
                    is ResultCargosRequest.Error -> {
                        result.exception.message?.let {  }
                    }
                    is ResultCargosRequest.ErrorConnection -> {
                        result.exception.message?.let {  }
                    }
                }
            }
        }
    }

    private fun addElementView() {

        val sexo = if (detalhes.sexoParlamentar == "Masculino") "Senador" else "Senadora"
        val age = calculateAge.age(dadosBasicos.dataNascimento)

        binding?.run {
            (detalhes.nomeCompletoParlamentar+", "+age+" anos, nascido na cidade de "+
                    dadosBasicos.naturalidade+" - "+dadosBasicos.ufNaturalidade+", é "+
                    sexo+" em exércicio, filiado ao partido "+ detalhes.siglaPartidoParlamentar+
                    " pelo estado de "+ detalhes.ufParlamentar+".")
                .also { textGeralInformation.text = it }

            val https = "https:/"
            val urlFoto = detalhes.urlFotoParlamentar.split(":/")
            val photo = https+urlFoto[1]
            context?.let {
                Glide.with(it)
                    .load(photo)
                    .circleCrop()
                    .into(iconSenadorGeral)
            }

            statusView.run {
                disableView(layoutProgressAndText.progressActive)
                enableView(iconSenadorGeral)
                enableView(textGeralInformation)
            }
        }
    }

    private fun addElementSiteBlog() {

        binding?.run {
            statusView.enableView(constraintSocialMedia)
            textSitePessoal.text =
                if (detalhes.urlPaginaParticular != null) {
                    detalhes.urlPaginaParticular+" >"
                } else "Não informou sua página particular"

            textSiteSenado.text =
                if (detalhes.urlPaginaParlamentar != null) {
                    detalhes.urlPaginaParlamentar+" >"
                } else "Não foi informado página pelo senado"
        }
        listenerSite(detalhes)
    }

    private fun addElementAddressContact(phone: String){

        binding?.run {
            statusView.enableView(constraintGabinete)
            textGeralPredio.text = dadosBasicos.enderecoParlamentar
            textGeralAndar.text = detalhes.emailParlamentar
            textGeralPhone.text =
                if (phone.contains("[{")) phone.substring(17, 25)
                else phone.substring(16, 24)
        }
    }

    private fun addElementCargo(dados: List<Cargo>) {
        val cargo = dados[0]
        binding?.run {
            statusView.enableView(constraintOccupation)
            ("Exerceu o cargo de "+cargo.descricaoCargo).also { textCargoSenador.text = it }
            (cargo.identificacaoComissao.nomeComissao).also { textCargoDescription.text = it }
        }
    }

    private fun addValueToLimitCotas(estado: String) {
        binding?.run {
            if (estado != "" && estado != null){
                val limit = cotaState.cotaStateSenado(estado)
                if (limit != 0){
                    layoutLimitCotas.run {
                        textSalario.text = "Limite de Cotas"
                        textSalarioMes.text = "Durante 1 mês"
                        textSalarioMesValue.text = formatValue.transformIntToString(limit)
                        textSalarioAno.text = "Durante 1 ano"
                        textSalarioAnoValue.text = formatValue.transformIntToString(limit * 12)
                        textSalarioMandato.text = "Durante 1 mandato"
                        textSalarioMandatoValue.text = formatValue.transformIntToString(limit * 96)
                        imageQuestion.setOnClickListener {
                            animaView(it)
                            clickQuestion()
                        }
                        statusView.disableView(viewLateral)
                    }
                    statusView.enableView(frameLimitCotas)
                }
            }
        }
    }

    private fun listenerSite(site: IdentificacaoParlamentarItem){
        binding?.run {
            textSitePessoal.setOnClickListener {
                if (site.urlPaginaParticular != null){
                    choose(site.urlPaginaParticular)
                }
            }
            textSiteSenado.setOnClickListener {
                if (site.urlPaginaParlamentar != null){
                    choose(site.urlPaginaParlamentar)
                }
            }
        }
    }

    private fun choose(social: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(social))
        startActivity(browserIntent)
    }

    private fun clickQuestion(){
        val dialog = createDialog.createDialog(requireContext())
        val viewDialog = layoutInflater.inflate(R.layout.layout_dialog_question, null)
        val seeMore = viewDialog.findViewById<TextView>(R.id.text_see_more)
        seeMore.setOnClickListener {
            animaView(seeMore)
            create.dismiss()
            val url = "https://www2.camara.leg.br/transparencia/acesso-a-informacao/" +
                    "copy_of_perguntas-frequentes/cota-para-o-exercicio-da-atividade-parlamentar#" +
                    ":~:text=A%20Cota%20para%20o%20Exerc%C3%ADcio,ao%20exerc%C3%ADcio%20da%20atividade%20parlamentar."
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        dialog.setView(viewDialog)
        create = dialog.create()
        create.show()
    }

    private fun animaView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.click))
    }
}