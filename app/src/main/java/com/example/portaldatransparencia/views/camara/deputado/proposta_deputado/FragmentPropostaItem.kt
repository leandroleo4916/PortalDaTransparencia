package com.example.portaldatransparencia.views.camara.deputado.proposta_deputado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentPropostaItemBinding
import com.example.portaldatransparencia.dataclass.DadosProposicao
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPropostaItem: AppCompatActivity() {

    private val binding by lazy { FragmentPropostaItemBinding.inflate(layoutInflater) }
    private val viewModel: PropostaViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private val verifyInternet: ValidationInternet by inject()
    private lateinit var id: String
    private lateinit var idParlamentar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.extras?.getString("id").toString()
        idParlamentar = securityPreferences.getString("id")
        modifyTop()
        observer()
    }

    private fun modifyTop() {
        binding.groupTop.run {
            textViewTitleTop.text = "Projeto de Lei"
            statusView.run { disableView(imageViewFilter) }
            imageViewBack.setOnClickListener { finish() }
        }
    }

    private fun observer() {

        val internet = verifyInternet.validationInternet(baseContext.applicationContext)
        if (internet){
            viewModel.propostaIdDeputado(id)
            viewModel.responseLive.observe(this){
                addElementToView(it)
                if (idParlamentar != "") addRelatorId()
                else binding.frameParlamentar.visibility = View.GONE
            }
            viewModel.responseErrorLiveData.observe(this){
                notValueOrNoInternet(R.string.nao_ecnontrado_proposta)
            }
        }
        else notValueOrNoInternet(R.string.verifique_sua_internet)
    }

    private fun addRelatorId(){
        viewModel.getParlamentar(idParlamentar)
        viewModel.responseLiveParlamentar.observe(this){ addElementViewParlamentar(it) }
        viewModel.responseErrorLiveDataParlamentar.observe(this){
            binding.layoutParlamentar.textErrorRelator.visibility = View.VISIBLE
        }
    }

    private fun notValueOrNoInternet(value: Int) {
        binding.layoutProgress.run {
            statusView.run {
                disableView(progressActive)
                textNotValue.text = getString(value)
                enableView(textNotValue)
            }
        }
    }

    private fun addElementToView(body: DadosProposicao) {
        binding.run {
            statusView.run {
                disableView(layoutProgress.progressActive)
                disableView(layoutProgress.constraintValorNotes)
                enableView(frameParlamentar)
                enableView(constraintLayoutProject)
                enableView(layoutParlamentar.constraintLayoutRelator)
                enableView(constraintLayoutInfo)
            }

            body.run {
                textEmenta.text = ementa
                textTipoDescricao.text = descricaoTipo
                statusProposicao.run {
                    textDescriptionTramitacao.text =
                        if (descricaoTramitacao != "" && descricaoTramitacao != null) descricaoTramitacao
                        else "Tramitação não informada"
                    textDescriptionSituacao.text =
                        if (descricaoSituacao != "" && descricaoSituacao != null) descricaoSituacao
                        else "Situação não informada"
                    textDescriptionDespacho.text =
                        if (despacho != "" && despacho != null) despacho
                        else "Despacho não informado"
                    textDescriptionApreciacao.text =
                        if (apreciacao != "" && apreciacao != null) apreciacao
                        else "Preciação não informada"
                }
                val date = dataApresentacao.split("T")
                val dateDiv = date[0].split("-")
                "${dateDiv[2]}/${dateDiv[1]}/${dateDiv[0]}".also { textDateVotacao.text = it }
            }
            buttonSeeDoc.setOnClickListener {
                animeClickView(it)
                if (body.urlInteiroTeor != null && body.urlInteiroTeor != "" ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(body.urlInteiroTeor))
                    startActivity(intent)
                }
                else {
                    Toast.makeText(application,
                        "Documento não foi anexado", Toast.LENGTH_SHORT).show()
                }
            }
            buttonSeeVideo.setOnClickListener { animeClickView(it) }
        }
    }

    private fun addElementViewParlamentar(item: IdDeputadoDataClass) {
        binding.layoutParlamentar.run {
            Glide.with(application)
                .load(item.dados.ultimoStatus.urlFoto)
                .circleCrop()
                .into(iconParlamentar)
            textNameParlamentar.text = item.dados.ultimoStatus.nome
            textPartidoEUf.text =
                "${item.dados.ultimoStatus.siglaPartido} - ${item.dados.ultimoStatus.siglaUf}"

            statusView.run {
                disableView(progressRelator)
                enableView(textNameParlamentar)
                enableView(textPartidoEUf)
                enableView(iconParlamentar)
                enableView(progressListRelator)
            }
            var value = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (value <= 20) {
                        withContext(Dispatchers.Main) {
                            progressListRelator.progress = value
                        }
                        delay(5)
                        value++
                    }
                }
            }
        }
    }

    private fun animeClickView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(application, R.anim.click))
    }
}