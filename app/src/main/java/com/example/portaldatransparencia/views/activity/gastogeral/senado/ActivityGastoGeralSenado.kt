package com.example.portaldatransparencia.views.activity.gastogeral.senado

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.remote.ResultGastoGeralSenado
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.eazegraph.lib.models.PieModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralSenado: AppCompatActivity() {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelSenado by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var gastoSenado: GastoGeralDataClass
    private lateinit var sizeSenador: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        modifyItemGraph()
        observerGastoSenado()
    }

    private fun modifyItemGraph() {
        binding.layoutUnic.run {
            textViewTitle.text = getString(R.string.senado_8anos)
            hideView.disableView(layoutServicosPostais)
            hideView.disableView(layoutOutrosServicos)
            hideView.disableView(toolbarAquisicao)
            hideView.disableView(toolbarOutrosServicos)
            textAluguel.text = getString(R.string.alugueDeImoveis)
            textServicosTelefonicos.text = getString(R.string.consultoriaAcessoria)
            textAquisicao.text = getString(R.string.aquisicaoDeMateriais)
        }
    }

    private fun observerGastoSenado() {

        viewModel.gastoGeralSenado().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralSenado.Success -> {
                        result.dado?.let { gasto ->
                            gastoSenado = gasto
                            addElementSenado(gastoSenado)
                            buildGraphSenado()
                        }
                    }
                    is ResultGastoGeralSenado.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultGastoGeralSenado.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun addElementSenado(dados: GastoGeralDataClass) {
        binding.layoutUnic.run {
            dados.gastoGeral.run {
                val total = formatValor.formatValor(totalGeral.toDouble())
                textViewParlamentar.text = getString(R.string.senadores)
                textViewTotalParlamentar.text = sizeSenador
                textViewGastoParlamentar.text = total
                textViewTotalNotas.text = totalNotas

                textAluguelValue.text = formatValor.formatValor(aluguel.toDouble())
                textDivulgacaoValue.text = formatValor.formatValor(divulgacao.toDouble())
                textPassagensValue.text = formatValor.formatValor(passagens.toDouble())
                textTelefonicosValue.text = formatValor.formatValor(contratacao.toDouble())
                textHospedagensValue.text = formatValor.formatValor(locomocao.toDouble())
                textCombustiveisValue.text = formatValor.formatValor(aquisicao.toDouble())
            }
        }
    }

    private fun buildGraphSenado(){
        gastoSenado.gastoGeral.run {
            addGraphSenado(getString(R.string.manutencao_escritorio), this.aluguel.toFloat(), "#D50000")
            addGraphSenado(getString(R.string.divulgacao_parlamentar), this.divulgacao.toFloat(), "#2467AA")
            addGraphSenado(getString(R.string.passagens_aereas), this.passagens.toFloat(), "#37505C")
            addGraphSenado(getString(R.string.servicos_telefonicos), this.contratacao.toFloat(), "#F47B37")
            addGraphSenado(getString(R.string.hospedagem_alimentacao), this.locomocao.toFloat(), "#17DC1B")
            addGraphSenado(getString(R.string.combustivel_lubrificante), this.aquisicao.toFloat(), "#6378AF")
        }
    }

    private fun addGraphSenado(title: String, value: Float, color: String){
        binding.layoutUnic.run {
            piechart.addPieSlice(PieModel(title, value, Color.parseColor(color)))
            piechart.startAnimation()
        }
    }
}