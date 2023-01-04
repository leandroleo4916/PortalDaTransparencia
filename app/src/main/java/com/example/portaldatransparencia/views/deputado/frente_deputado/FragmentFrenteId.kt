package com.example.portaldatransparencia.views.deputado.frente_deputado

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

        id = intent.extras?.getString("id").toString()
        observer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

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
                        else {

                        }
                    }
                    429 -> observer()
                    else -> {

                    }
                }
            }

            override fun onFailure(call: Call<FrenteId>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addElementFront(front: FrenteId){

        binding.run {
            statusView.disableView(progressFrontId)
            statusView.enableView(constraintLayout)
            statusView.enableView(constraintLayout2)

            textFrenteTitle.text = front.dados.titulo
            textFrenteSituation.text = front.dados.situacao
            textNomeCoordinator.text = front.dados.coordenador.nome

            if (front.dados.coordenador.siglaPartido != null){
                statusView.enableView(textFrentePartido)
                (front.dados.coordenador.siglaPartido+" - "+front.dados.coordenador.siglaUf)
                    .also { textFrentePartido.text = it }
            }
            if (front.dados.coordenador.urlFoto != null){
                Glide.with(applicationContext)
                    .load(front.dados.coordenador.urlFoto)
                    .circleCrop()
                    .into(iconCoordinator)
            }
            iconReturn.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.click))
                finish()
            }
        }
    }
}