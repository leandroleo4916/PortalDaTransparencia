package com.example.portaldatransparencia.views.camara.deputado.geral_deputado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralDeputadoBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.dataclass.Occupation
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.*
import com.example.portaldatransparencia.views.camara.deputado.DeputadoViewModel
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.CreateDialogClass
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URL

class FragmentGeralDeputado: Fragment(R.layout.fragment_geral_deputado) {

    private var binding: FragmentGeralDeputadoBinding? = null
    private val viewModel: DeputadoViewModel by viewModel()
    private val viewModelOccupation: OccupationViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val dayOfMonth: DaysOfMonth by inject()
    private val verifyInternet: ValidationInternet by inject()
    private val calculateAge: CalculateAge by inject()
    private val formatValue: FormatValueFloat by inject()
    private val statusView: EnableDisableView by inject()
    private val createDialog: CreateDialogClass by inject()
    private val animeView: AnimationView by inject()
    private val cotaState: CotaState by inject()
    private var sexoDeputado = ""
    private lateinit var id: String
    private lateinit var create: AlertDialog
    private lateinit var dataIni: String
    private lateinit var dataFim: String
    private lateinit var matricula: String
    private lateinit var chipEnabled: Chip
    private var mesSelected = "2023"
    private var mesSelectedValue = "2023"
    private var present = 0
    private var faltaJust = 0
    private var faltaInjust = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralDeputadoBinding.bind(view)

        beHideView()
        chipEnabled = binding!!.layoutPresent.layoutMesPresent.chipAll
        id = securityPreferences.getString("id")
        observerDeputado()
        observerOccupation()
        getPresentSessions()
        listenerChip()
    }

    private fun getPresentSessions(){
        val month = dayOfMonth.month(mesSelected)
        dataIni = month[0]
        dataFim = month[1]
        CoroutineScope(Dispatchers.Main).launch {
            getIdMatriculaParlamentar()
        }
    }

    private fun beHideView(){
        binding!!.layoutPresent.layoutMesPresent.run {
            chipJaneiro.visibility = View.GONE
            toolbasMesTop.visibility = View.GONE
            toolbarMesBottom.visibility = View.GONE
        }
    }

    private fun observerOccupation() {
        viewModelOccupation.getOccupation(id)
        viewModelOccupation.responseLiveData.observe(viewLifecycleOwner){
            addElementOccupation(it)
        }
    }

    private fun observerDeputado() {

        val internet = verifyInternet.validationInternet(requireContext().applicationContext)
        if (internet){
            viewModel.searchDataDeputado(id)
            viewModel.responseLiveData.observe(viewLifecycleOwner){
                addElementView(it)
                addElementRedeSocial(it)
                addValueToLimitCotas(it)
                sexoDeputado = it.sexo
            }
            viewModel.responseErrorLiveData.observe(viewLifecycleOwner){ addValueText(it) }
        }
        else addValueText(R.string.verifique_sua_internet)
    }

    private fun addValueText(text: Int) {
        binding?.layoutProgressAndText?.run {
            progressActive.smoothToHide()
            statusView.enableView(textNotValue)
            textNotValue.text = getString(text)
        }
    }

    private fun addElementView(dados: Dados) {
        val deputado: String
        val oDeputado: String
        if (dados.sexo == "F") {
            deputado = "Deputada Federal"
            oDeputado = "a deputada"
        } else {
            deputado = "Deputado Federal"
            oDeputado = "o deputado"
        }
        val age = calculateAge.age(dados.dataNascimento)
        val status = dados.ultimoStatus

        val situation =
            if (dados.ultimoStatus.situacao == "Exercício") "é $deputado em exercício"
            else "foi $deputado - fim de mandato"

        val escolaridade =
            if (dados.escolaridade == "null" || dados.escolaridade == null) "."
            else ", sua escolaridade atual é " +dados.escolaridade+"."

        binding?.run {
            ("Acompanhe $oDeputado nas redes sociais").also { textAcompanheRede.text = it }
            (dados.nomeCivil+", "+age+" anos, nascido na cidade de "+
                    dados.municipioNascimento+" - "+dados.ufNascimento+", $situation, filiado ao partido "+
                    status.siglaPartido+" pelo estado de "+ status.siglaUf+escolaridade)
                .also { textGeralInformation.text = it }

            textGeralPredio.text = "Predio: "+ (status.gabinete?.predio ?: "Não informado")
            textGeralAndar.text = "Andar: "+ (status.gabinete?.andar ?: "Não informado")
            textGeralSala.text = "Sala: "+ (status.gabinete?.sala ?: "Não informado")
            textGeralPhone.text = status.gabinete?.telefone ?: "Não informado"

            Glide.with(requireContext())
                .load(dados.ultimoStatus.urlFoto)
                .circleCrop()
                .into(iconDeputadoGeral)

            statusView.run {
                disableView(layoutProgressAndText.progressActive)
                enableView(iconDeputadoGeral)
                enableView(textGeralInformation)
                enableView(constraintSocialMedia)
                enableView(constraintGabinete)
            }
        }
    }

    private fun addElementOccupation(dados: List<Occupation>) {
        val occupation = dados[0]
        val deputadoOccupation = if (sexoDeputado == "M") "deputado" else "deputada"

        val em = " em "
        val hifen = " - "
        val uf = occupation.entidadeUF
        val pais = occupation.entidadePais
        val part = if (uf != "") em+uf+hifen+pais else ""

        binding?.run {
            "Seu trabalho antes de ser $deputadoOccupation".also { textWork.text = it }
            if (occupation.titulo != null){
                ("Trabalhou como "+occupation.titulo+" na "+ occupation.entidade+part+", no ano de "+
                        occupation.anoInicio+".").also { textOccupationDescription.text = it }
            }
            statusView.enableView(constraintOccupation)
        }
    }

    private fun addElementRedeSocial(dados: Dados){
        var facebook = ""
        var instagram = ""
        var twitter = ""
        var youtube = ""

        if (dados.redeSocial.isNotEmpty()){
            dados.redeSocial.forEach {
                when {
                    it.contains("facebook") -> facebook = it
                    it.contains("instagram") -> instagram = it
                    it.contains("twitter") -> twitter = it
                    it.contains("youtube") -> youtube = it
                }
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

    private fun addValueToLimitCotas(dados: Dados) {
        binding?.run {
            if (dados.ultimoStatus.siglaUf != "" && dados.ultimoStatus.siglaUf != null){
                val limit = cotaState.cotaStateCamara(dados.ultimoStatus.siglaUf)
                if (limit != 0){
                    layoutLimitCotas.run {
                        textSalario.text = "Limite de Cotas"
                        textSalarioMes.text = "Durante 1 mês"
                        textSalarioMesValue.text = formatValue.transformIntToString(limit)
                        textSalarioAno.text = "Durante 1 ano"
                        textSalarioAnoValue.text = formatValue.transformIntToString(limit * 12)
                        textSalarioMandato.text = "Durante 1 mandato"
                        textSalarioMandatoValue.text = formatValue.transformIntToString(limit * 48)
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

    private fun listenerRedeSocial(f: String, i: String, t: String, y: String){
        binding?.run {
            constraint1.setOnClickListener {
                animaView(it)
                choose(f) }
            constraint2.setOnClickListener {
                animaView(it)
                choose(i) }
            constraint3.setOnClickListener {
                animaView(it)
                choose(t) }
            constraint4.setOnClickListener {
                animaView(it)
                choose(y) }
        }
    }

    private fun clickQuestion(){
        val dialog = createDialog.createDialog(requireContext())
        val viewDialog = layoutInflater.inflate(R.layout.layout_dialog_question, null)
        val seeMore = viewDialog.findViewById<TextView>(R.id.text_see_more)
        val closeDialog = viewDialog.findViewById<ImageView>(R.id.image_close)
        seeMore.setOnClickListener {
            animaView(seeMore)
            create.dismiss()
            val url = "https://www2.camara.leg.br/transparencia/acesso-a-informacao/" +
                    "copy_of_perguntas-frequentes/cota-para-o-exercicio-da-atividade-parlamentar#" +
                    ":~:text=A%20Cota%20para%20o%20Exerc%C3%ADcio,ao%20exerc%C3%ADcio%20da%20atividade%20parlamentar."
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        closeDialog.setOnClickListener {
            create.dismiss()
        }
        dialog.setView(viewDialog)
        create = dialog.create()
        create.show()
    }

    private fun choose(social: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(social))
        startActivity(browserIntent)
    }

    private fun animaView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.click))
    }

    // Todas as classes abaixo refere-se ao Layout Presença em Sessões
    private suspend fun getIdMatriculaParlamentar(){

        var call = false
        var count = 3

        withContext(Dispatchers.Default) {
            try {
                val urlForm = "https://www.camara.leg.br/SitCamaraWS/deputados.asmx/ObterDeputados"
                val url = URL(urlForm)
                val connection = url.openConnection()
                val inputStream = connection.getInputStream()

                val reader = inputStream.bufferedReader()
                val res = reader.readLines()

                while (!call) {
                    if (!res[count].contains(id)){
                        count += 21
                    }
                    else {
                        call = true
                        val str = res[count+3].substring(15)
                        val strDiv = str.split("<")
                        matricula = strDiv[0]
                    }
                }
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){
                    noValue()
                }
            }
        }
        if (call){
            CoroutineScope(Dispatchers.Main).launch {
                getPresent()
            }
        }
    }

    private fun listenerChip() {
        binding!!.layoutPresent.layoutMesPresent.run {
            chipAll.setOnClickListener { modify(chipEnabled, chipAll) }
            chipFevereiro.setOnClickListener { modify(chipEnabled, chipFevereiro) }
            chipMarco.setOnClickListener { modify(chipEnabled, chipMarco) }
            chipAbril.setOnClickListener { modify(chipEnabled, chipAbril) }
            chipMaio.setOnClickListener { modify(chipEnabled, chipMaio) }
            chipJunho.setOnClickListener { modify(chipEnabled, chipJunho) }
            chipJulho.setOnClickListener { modify(chipEnabled, chipJulho) }
            chipAgosto.setOnClickListener { modify(chipEnabled, chipAgosto) }
            chipSetembro.setOnClickListener { modify(chipEnabled, chipSetembro) }
            chipOutubro.setOnClickListener { modify(chipEnabled, chipOutubro) }
            chipNovembro.setOnClickListener { modify(chipEnabled, chipNovembro) }
            chipDezembro.setOnClickListener { modify(chipEnabled, chipDezembro) }
        }
    }

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {
        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        chipEnabled = viewDisabled
        mesSelected = viewDisabled.hint.toString()
        mesSelectedValue = viewDisabled.text.toString()
        binding!!.layoutPresent.run {
            textSessions.text = "Buscando..."
            animeView.crossFade(constraintLayout5)
            animeView.crossFade(constraintLayout6)
            animeView.crossFade(constraintLayout7)
            statusView.enableView(layoutProgressAndText.progressActive)
            statusView.disableView(layoutProgressAndText.textNotValue)
        }
        getPresentSessions()
    }

    private suspend fun getPresent() {
        present = 0
        faltaJust = 0
        faltaInjust = 0
        var call = false

        withContext(Dispatchers.Default) {
            try {
                val urlForm =
                    "https://www.camara.leg.br/SitCamaraWS/sessoesreunioes.asmx/ListarPresencasParlamentar?dataIni=$dataIni&dataFim=$dataFim&numMatriculaParlamentar=$matricula"
                val url = URL(urlForm)
                val connection = url.openConnection()
                val inputStream = connection.getInputStream()
                val xmlStringBuilder = StringBuilder()

                val reader = inputStream.bufferedReader()
                var line: String? = reader.readLine()
                while (line != null) {
                    xmlStringBuilder.append(line)
                    line = reader.readLine()
                    parseXml(line)
                }
                call = true
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){
                    noValue()
                }
            }
        }
        if (call){
            setValueToView()
            modifyTextViewTitle()
        }
    }

    private fun modifyTextViewTitle(){
        val totalSessions = present+faltaJust+faltaInjust
        binding!!.run {
            layoutPresent.textSessions.text =
                (if (mesSelectedValue != "Todos") "$totalSessions Sessões em $mesSelectedValue"
                 else "$totalSessions Sessões em 2023").toString()
            framePresent.visibility = View.VISIBLE
        }
    }

    private fun parseXml(line: String?) {
        if (line != null) {
            when {
                line.contains("Presença") -> present += 1
                line.contains("Ausência justificada") -> faltaJust += 1
                line.contains("Ausência") -> faltaInjust += 1
            }
        }
    }

    private fun setValueToView() {

        binding!!.layoutPresent.run {
            progressPresentSessions.progress = 0
            progressFaltasJust.progress = 0
            progressFaltasInjust.progress = 0
            progressPresentSessions.max = present+faltaJust+faltaInjust
            progressFaltasJust.max = faltaJust+faltaInjust
            progressFaltasInjust.max = faltaJust+faltaInjust
            statusView.disableView(layoutProgressAndText.progressActive)
            statusView.disableView(layoutProgressAndText.textNotValue)
            animeView.crossFade(constraintLayout5)
            animeView.crossFade(constraintLayout6)
            animeView.crossFade(constraintLayout7)
        }

        var valuePresent = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (valuePresent <= present) {
                    withContext(Dispatchers.Main) {
                        binding!!.layoutPresent.run {
                            progressPresentSessions.progress = valuePresent
                            textPresentSessionsValue.text = valuePresent.toString()
                        }
                    }
                    delay(5)
                    valuePresent++
                }
            }
        }

        var valueFaltasJust = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (valueFaltasJust <= faltaJust) {
                    withContext(Dispatchers.Main) {
                        binding!!.layoutPresent.run {
                            progressFaltasJust.progress = valueFaltasJust
                            textFaltasJustValue.text = valueFaltasJust.toString()
                        }
                    }
                    delay(100)
                    valueFaltasJust++
                }
            }
        }

        var valueFaltasInjust = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (valueFaltasInjust <= faltaInjust) {
                    withContext(Dispatchers.Main) {
                        binding!!.layoutPresent.run {
                            progressFaltasInjust.progress = valueFaltasInjust
                            textFaltasInjustValue.text = valueFaltasInjust.toString()
                        }
                    }
                    delay(100)
                    valueFaltasInjust++
                }
            }
        }
    }

    private fun noValue(){
        binding!!.layoutPresent.run {
            textSessions.text = "Presença em Sessões"

            layoutProgressAndText.run {
                progressActive.smoothToHide()
                textNotValue.text =
                    (if (mesSelectedValue != "Todos") "Não tem informações para $mesSelectedValue"
                    else "Não tem informações para 2023").toString()
                statusView.enableView(textNotValue)
            }
        }
    }
}