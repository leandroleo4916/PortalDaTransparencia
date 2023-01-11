package com.example.portaldatransparencia.views.deputado.proposta_deputado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentPropostaItemBinding
import com.example.portaldatransparencia.dataclass.DadosProposicao
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.ProposicaoDataClass
import com.example.portaldatransparencia.remote.ApiServiceIdDeputado
import com.example.portaldatransparencia.remote.ApiServicePropostaItem
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPropostaItem: AppCompatActivity() {

    private val binding by lazy { FragmentPropostaItemBinding.inflate(layoutInflater) }
    private val viewModel: PropostaViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
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
            statusView.disableView(imageViewFilter)
            imageViewBack.setOnClickListener { finish() }
        }
    }

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServicePropostaItem::class.java)
        val call: Call<ProposicaoDataClass> = retrofit.getPropostaItem(id)
        call.enqueue(object: Callback<ProposicaoDataClass> {
            override fun onResponse(call: Call<ProposicaoDataClass>, res: Response<ProposicaoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null) {
                            addElementToView(res.body()!!.dados)
                            observerParlamentar()
                        }
                        else notValue()
                    }
                    429 -> observer()
                    else -> notValue()
                }
            }
            override fun onFailure(call: Call<ProposicaoDataClass>, t: Throwable) {
                notValue()
            }
        })
    }

    private fun observerParlamentar() {

        val retrofit = Retrofit.createService(ApiServiceIdDeputado::class.java)
        val call: Call<IdDeputadoDataClass> = retrofit.getIdDeputado(idParlamentar)
        call.enqueue(object: Callback<IdDeputadoDataClass> {
            override fun onResponse(call: Call<IdDeputadoDataClass>, res: Response<IdDeputadoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            addElementViewParlamentar(res.body()!!)
                        }
                        else {}
                    }
                    429 -> observerParlamentar()
                    else -> {}
                }
            }

            override fun onFailure(call: Call<IdDeputadoDataClass>, t: Throwable) {

            }
        })
    }

    private fun notValue(){
        binding.run {
            statusView.run {
                disableView(progressProposta)
                enableView(textNotValue)
            }
        }
    }

    private fun addElementToView(body: DadosProposicao) {
        binding.run {
            statusView.run {
                disableView(progressProposta)
                disableView(constraintValorNotes)
                enableView(constraintLayoutProject)
                enableView(constraintLayoutRelator)
                enableView(constraintLayoutInfo)
            }

            body.run {
                textEmenta.text = ementa
                textTipoDescricao.text = descricaoTipo
                statusProposicao.run {
                    textDescriptionTramitacao.text =
                        if (descricaoTramitacao != "") descricaoTramitacao
                        else "Tramitação não informada"
                    textDescriptionSituacao.text =
                        if (descricaoSituacao != "") descricaoSituacao
                        else "Situação não informada"
                    textDescriptionDespacho.text =
                        if (despacho == "") despacho
                        else "Despacho não informado"
                    textDescriptionApreciacao.text =
                        if (apreciacao != "") apreciacao
                        else "Preciação não informada"
                }
                val date = dataApresentacao.split("T")
                val dateDiv = date[0].split("-")
                "${dateDiv[2]}/${dateDiv[1]}/${dateDiv[0]}".also { textDateVotacao.text = it }
            }
            buttonSeeDoc.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.click))
                if (body.urlInteiroTeor != null && body.urlInteiroTeor != "" ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(body.urlInteiroTeor))
                    startActivity(intent)
                }
                else {
                    Toast.makeText(baseContext,
                        "Documento não foi anexado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addElementViewParlamentar(item: IdDeputadoDataClass) {
        binding.run {
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
}