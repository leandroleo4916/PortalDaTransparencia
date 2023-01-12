package com.example.portaldatransparencia.views.camara.deputado.frente_deputado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentFrenteIdBinding
import com.example.portaldatransparencia.dataclass.FrenteId
import com.example.portaldatransparencia.remote.ApiServiceFrenteId
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFrenteId: AppCompatActivity() {

    private val binding by lazy { FragmentFrenteIdBinding.inflate(layoutInflater) }
    private val viewModel: FrenteViewModel by viewModel()
    private val statusView: EnableDisableView by inject()
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        modifyInfo()
        id = intent.extras?.getString("id").toString()
        observer()
    }

    private fun modifyInfo(){
        binding.layoutParlamentar.run {
            statusView.run {
                disableView(progressRelator)
                enableView(iconParlamentar)
                enableView(textNameParlamentar)
                enableView(textPartidoEUf)
                textUltimoRelator.text = "Coordenador"
            }
        }
    }

    override fun onBackPressed() = finish()

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServiceFrenteId::class.java)
        val call: Call<FrenteId> = retrofit.getFrenteId(id)
        call.enqueue(object: Callback<FrenteId> {
            override fun onResponse(call: Call<FrenteId>, res: Response<FrenteId>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            addElementFront(res.body()!!)
                        }
                        else {}
                    }
                    429 -> observer()
                    else -> {}
                }
            }

            override fun onFailure(call: Call<FrenteId>, t: Throwable) {

            }
        })
    }

    private fun addElementFront(front: FrenteId){

        binding.run {
            statusView.run {
                disableView(progressFrontId)
                enableView(frameParlamentar)
                enableView(constraintLayout2)
                enableView(layoutParlamentar.textNameParlamentar)
            }
            textFrenteTitle.text = front.dados.titulo
            textFrenteSituation.text = front.dados.situacao
            layoutParlamentar.textNameParlamentar.text = front.dados.coordenador.nome

            layoutParlamentar.run {
                "${front.dados.coordenador.siglaPartido} - ${front.dados.coordenador.siglaUf}"
                    .also { textPartidoEUf.text = it }

                Glide.with(applicationContext)
                    .load(front.dados.coordenador.urlFoto)
                    .circleCrop()
                    .into(iconParlamentar)

                var value = 0
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Default) {
                        while (value <= 20) {
                            withContext(Dispatchers.Main) {
                                layoutParlamentar.progressListRelator.progress = value
                            }
                            delay(5)
                            value++
                        }
                    }
                }
            }
            iconReturn.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.click))
                finish()
            }
        }
    }
}