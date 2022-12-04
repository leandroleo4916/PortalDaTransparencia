package com.example.portaldatransparencia.views.deputado.frente_deputado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentFrenteIdBinding
import com.example.portaldatransparencia.dataclass.FrenteId
import com.example.portaldatransparencia.remote.ResultFrenteIdRequest
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        viewModel.frenteDeputadoId(id).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultFrenteIdRequest.Success -> {
                        result.dado?.let { front ->
                            if (front.dados != null) addElementFront(front)
                        }
                    }
                    is ResultFrenteIdRequest.Error -> {
                        result.exception.message?.let {}
                    }
                    is ResultFrenteIdRequest.ErrorConnection -> {
                        result.exception.message?.let {}
                    }
                }
            }
        }
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