package com.example.portaldatransparencia.views.camara.deputado.geral_deputado

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
import com.example.portaldatransparencia.databinding.FragmentGeralDeputadoBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.dataclass.Occupation
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.*
import com.example.portaldatransparencia.views.camara.deputado.DeputadoViewModel
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
    private val cotaState: CotaState by inject()
    private var sexoDeputado = ""
    private lateinit var id: String
    private lateinit var create: AlertDialog
    private lateinit var dataIni: String
    private lateinit var dataFim: String
    private lateinit var matricula: String
    private lateinit var chipEnabled: Chip
    private var present = 0
    private var falta = 0
    private var faltaJust = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralDeputadoBinding.bind(view)

        chipEnabled = binding!!.layoutPresent.layoutMesPresent.chipAll
        id = securityPreferences.getString("id")
        observerDeputado()
        observerOccupation()
        val month = dayOfMonth.month("Todos")
        dataIni = month[0]
        dataFim = month[1]
        matricula = "319"
        CoroutineScope(Dispatchers.Main).launch {
            getPresent()
        }
        listenerChip()
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
            statusView.disableView(progressActive)
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

    private fun choose(social: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(social))
        startActivity(browserIntent)
    }

    private fun animaView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.click))
    }

    private fun listenerChip() {
        binding!!.layoutPresent.layoutMesPresent.run {
            chipAll.setOnClickListener { modify(chipEnabled, chipAll) }
            chipJaneiro.setOnClickListener { modify(chipEnabled, chipJaneiro) }
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
        val month = dayOfMonth.month(viewDisabled.text.toString())
        dataIni = month[0]
        dataFim = month[1]
        runBlocking {
            launch {
                getPresent()
            }
        }
    }

    private suspend fun getPresent() {
        present = 0
        faltaJust = 0
        falta = 0

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
            }
            catch (e:Exception){
                println("Falta implemtar validações")
                noValue("")
            }
        }
        setValueToView()
    }

    private fun parseXml(line: String?) {
        if (line != null) {
            when {
                line.contains("Presença") -> present += 1
                line.contains("Ausência justificada") -> faltaJust += 1
                line.contains("Ausência") -> falta += 1
            }
        }
    }

    private fun setValueToView() {

        val totalSessions = present+faltaJust+falta
        binding!!.layoutPresent.progressPresent.max = totalSessions
        var valuePresent = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (valuePresent <= present) {
                    withContext(Dispatchers.Main) {
                        binding!!.layoutPresent.run {
                            progressPresent.progress = valuePresent
                            textPresentValue.text = valuePresent.toString()
                        }
                    }
                    delay(5)
                    valuePresent++
                }
            }
        }

        binding!!.layoutPresent.progressFaltas.max = totalSessions
        var valueFaltas = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (valueFaltas <= faltaJust+falta) {
                    withContext(Dispatchers.Main) {
                        binding!!.layoutPresent.run {
                            progressFaltas.progress = valueFaltas
                            textFaltasValue.text = valueFaltas.toString()
                        }
                    }
                    delay(5)
                    valueFaltas++
                }
            }
        }

        binding!!.layoutPresent.progressSessions.max = totalSessions
        var valueSessions = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (valueSessions <= totalSessions) {
                    withContext(Dispatchers.Main) {
                        binding!!.layoutPresent.run {
                            progressSessions.progress = valueSessions
                            textSessionsValue.text = valueSessions.toString()
                        }
                    }
                    delay(5)
                    valueSessions++
                }
            }
        }
    }

    private fun noValue(value: String){

    }
}