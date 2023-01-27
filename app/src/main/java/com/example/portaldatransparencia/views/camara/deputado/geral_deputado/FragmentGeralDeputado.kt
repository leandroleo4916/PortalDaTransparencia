package com.example.portaldatransparencia.views.camara.deputado.geral_deputado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentGeralDeputadoBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.Occupation
import com.example.portaldatransparencia.dataclass.OccupationDataClass
import com.example.portaldatransparencia.remote.ApiServiceIdDeputado
import com.example.portaldatransparencia.remote.ApiServiceOccupation
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.camara.deputado.DeputadoViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentGeralDeputado: Fragment(R.layout.fragment_geral_deputado) {

    private var binding: FragmentGeralDeputadoBinding? = null
    private val viewModel: DeputadoViewModel by viewModel()
    private val viewModelOccupation: OccupationViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val calculateAge: CalculateAge by inject()
    private val statusView: EnableDisableView by inject()
    private var sexoDeputado = ""
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeralDeputadoBinding.bind(view)
        id = securityPreferences.getString("id")
        observerDeputado()
        observerOccupation()
    }

    private fun observerOccupation() {

        val retrofit = Retrofit.createService(ApiServiceOccupation::class.java)
        val call: Call<OccupationDataClass> = retrofit.getOccupation(id)
        call.enqueue(object: Callback<OccupationDataClass>{
            override fun onResponse(call: Call<OccupationDataClass>, res: Response<OccupationDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            addElementOccupation(res.body()!!.dados)
                        }
                    }
                    429 -> observerOccupation()
                    else -> {}
                }
            }
            override fun onFailure(call: Call<OccupationDataClass>, t: Throwable) {}
        })
    }

    private fun observerDeputado() {

        val retrofit = Retrofit.createService(ApiServiceIdDeputado::class.java)
        val call: Call<IdDeputadoDataClass> = retrofit.getIdDeputado(id)
        call.enqueue(object: Callback<IdDeputadoDataClass>{
            override fun onResponse(call: Call<IdDeputadoDataClass>, res: Response<IdDeputadoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            addElementView(res.body()!!.dados)
                            addElementRedeSocial(res.body()!!.dados)
                            sexoDeputado = res.body()!!.dados.sexo
                        }
                        else addValueText("Sem informação na API")
                    }
                    429 -> observerDeputado()
                    else -> addValueText("Erro na API do Senado")
                }
            }
            override fun onFailure(call: Call<IdDeputadoDataClass>, t: Throwable) {
                addValueText("API do Senado não respondeu")
            }
        })
    }

    private fun addValueText(txt: String) {
        binding?.textGeralInformation?.run {
            text = txt
            statusView.enableView(this)
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

            context?.let {
                Glide.with(it)
                    .load(dados.ultimoStatus.urlFoto)
                    .circleCrop()
                    .into(iconDeputadoGeral)
            }

            statusView.run {
                disableView(progressGeral)
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
            constraint1.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                choose(f) }
            constraint2.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                choose(i) }
            constraint3.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                choose(t) }
            constraint4.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                choose(y) }
        }
    }

    private fun choose(social: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(social))
        startActivity(browserIntent)
    }
}