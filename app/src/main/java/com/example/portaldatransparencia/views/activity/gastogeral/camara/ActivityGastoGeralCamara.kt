package com.example.portaldatransparencia.views.activity.gastogeral.camara

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.eazegraph.lib.models.PieModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralCamara: AppCompatActivity() {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelCamara by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var gastoCamara: GastoGeralCamara

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observerGastoCamara()
        listener()
    }

    private fun observerGastoCamara() {

        viewModel.gastoGeralCamara().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            gastoCamara = gastos
                            addElementCamara(gastoCamara)
                            buildGraphCamara()
                        }
                    }
                    is ResultGastoGeralCamara.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultGastoGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun addElementCamara(dados: GastoGeralCamara) {
        binding.run {
            dados.gastoGeral.run {
                val total = formatValor.formatValor(totalGeral.toDouble())
                textViewGastoParlamentar.text = total
                textViewTotalNotas.text = dados.gastoGeral.totalNotas
                hideView.run {
                    disableView(progressGastoGeral)
                    enableView(constraintNumberParlamentar)
                    enableView(constraintNumberTotal)
                    enableView(constraintNumberNotas)
                    enableView(linearLayout2)
                    enableView(toolbarSeparate)
                }
                textAluguelValue.text = formatValor.formatValor(manutencao.toDouble())
                textDivulgacaoValue.text = formatValor.formatValor(divulgacao.toDouble())
                textPassagensValue.text = formatValor.formatValor(passagens.toDouble())
                textTelefonicosValue.text = formatValor.formatValor(telefonia.toDouble())
                textHospedagensValue.text = formatValor.formatValor(alimentacao.toDouble())
                textCombustiveisValue.text = formatValor.formatValor(combustivel.toDouble())
                textPostaisValue.text = formatValor.formatValor(servicos.toDouble())
                textOutrosServicosValue.text = formatValor.formatValor(outros.toDouble())
            }
            layoutTop.run {
                textViewDescriptionTop.text = getString(R.string.gastoGeral8Anos)
                hideView.enableView(textViewDescriptionTop)
                hideView.disableView(imageViewFilter)
            }
        }
    }

    private fun buildGraphCamara(){
        gastoCamara.gastoGeral.run {
            addGraphCamara(getString(R.string.manutencao_escritorio), this.manutencao.toFloat(), "#D50000")
            addGraphCamara(getString(R.string.divulgacao_parlamentar), this.divulgacao.toFloat(), "#2467AA")
            addGraphCamara(getString(R.string.passagens_aereas), this.passagens.toFloat(), "#37505C")
            addGraphCamara(getString(R.string.servicos_telefonicos), this.telefonia.toFloat(), "#F47B37")
            addGraphCamara(getString(R.string.hospedagem_alimentacao), this.alimentacao.toFloat(), "#17DC1B")
            addGraphCamara(getString(R.string.combustivel_lubrificante), this.combustivel.toFloat(), "#6378AF")
            addGraphCamara(getString(R.string.servico_postal), this.servicos.toFloat(), "#40A89A")
            addGraphCamara(getString(R.string.outros_servicos), this.outros.toFloat(), "#08197A")
        }
    }

    private fun addGraphCamara(title: String, value: Float, color: String){
        binding.run {
            piechart.addPieSlice(PieModel(title, value, Color.parseColor(color)))
            piechart.startAnimation()
        }
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }
}