package com.example.portaldatransparencia.views.activity.dados

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDadosBinding
import com.example.portaldatransparencia.util.ConverterValueNotes
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.activity.gastogeral.camara.GastoGeralViewModelCamara
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.createDialog
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityDadosCamara: AppCompatActivity() {

    private val binding by lazy { ActivityDadosBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelCamara by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val crossFade: AnimationView by inject()
    private val converterValue: ConverterValueNotes by inject()
    private val animeView: AnimationView by inject()
    private lateinit var create: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        modifyItemTop()
        listenerBack()
        addElementView()
    }

    private fun modifyItemTop(){
        binding.run {
            layoutTop.apply {
                textViewTitleTop.text = "Dados Parlamentares"
                hideView.disableView(imageViewFilter)
                hideView.disableView(imageViewOptionOrQuestion)
            }
            layoutProgressAndText.apply {
                hideView.disableView(progressActive)
            }
        }
    }

    private fun addElementView() {
        binding.run {
            layoutDadosSalario.apply {
                imageQuestion.setOnClickListener {
                    animaView(it)
                    clickOptionOrQuestion(
                        "Salário",
                        "O subsídio (remuneração bruta mensal) do deputado federal é de:\n" +
                            "\n" +
                            " I - R\$ 39.293,32 (trinta e nove mil duzentos e noventa e três reais " +
                                "e trinta e dois centavos), a partir de 1º de janeiro de 2023;\n" +
                            "\n" +
                            "II - R\$ 41.650,92 (quarenta e um mil seiscentos e cinquenta reais e " +
                                "noventa e dois centavos), a partir de 1º de abril de 2023;\n" +
                            "\n" +
                            "III - R\$ 44.008,52 (quarenta e quatro mil e oito reais e cinquenta e " +
                                "dois centavos), a partir de 1º de fevereiro de 2024;\n" +
                            "\n" +
                            "IV - R\$ 46.366,19 (quarenta e seis mil trezentos e sessenta e seis reais " +
                                "e dezenove centavos), a partir de 1º de fevereiro de 2025. " +
                                "(Decreto Legislativo 172/2022).",
                        "https://www2.camara.leg.br/comunicacao/assessoria-de-imprensa/" +
                                "guia-para-jornalistas/salario-de-deputados"
                        )
                }
            }
            frameSalario.apply {
                crossFade.crossFade(this)
            }
            layoutDadosAuxilio.apply {
                textSalario.text = "Auxílio Moradia"
                textSalarioMesValue.text = "R$ 4.253,00"
                textSalarioAnoValue.text = "R$ 51.036,00"
                textSalarioMandatoValue.text = "R$ 204.144,00"
                imageQuestion.setOnClickListener {
                    animaView(it)
                    clickOptionOrQuestion(
                        "Auxílio Moradia", "Os deputados federais têm direito a " +
                                "receber um auxílio-moradia no valor de R$ 4.253,00 quando não " +
                                "ocupam um dos 432 apartamentos funcionais que a Câmara tem em Brasília.",
                        "https://www2.camara.leg.br/transparencia/imoveis-funcionais-e-auxilio-moradia")

                }
            }
            frameAuxilio.apply {
                crossFade.crossFade(this)
            }
            layoutDadosVerba.apply {
                textSalario.text = "Verba de Gabinete"
                textSalarioMesValue.text = "R$ 118.376,13"
                textSalarioAnoValue.text = "R$ 1.420.513,56"
                textSalarioMandatoValue.text = "R$ 5.682.054,24"
                imageQuestion.setOnClickListener {
                    animaView(it)
                    clickOptionOrQuestion(
                        "Verba de Gabinete",
                        "Cada deputado tem R$ 118.376,13 por mês para pagar salários de " +
                                "até 25 secretários parlamentares, que trabalham para o mandato " +
                                "em Brasília ou nos estados. Eles são contratados diretamente pelos " +
                                "deputados, com salários de R$ 1.408,11 a R$ 16.640,22. \n\n" +
                                "Encargos trabalhistas como 13º, férias e auxílio-alimentação dos " +
                                "secretários parlamentares não são cobertos pela verba de gabinete " +
                                "- são pagos com recursos da Câmara, não inclui na contagem da Verba.",
                        "https://www2.camara.leg.br/comunicacao/assessoria-de-imprensa/" +
                                "guia-para-jornalistas/verba-de-gabinete"
                    )
                }
            }
            frameVerba.apply {
                crossFade.crossFade(this)
            }
            layoutDadosCotas.apply {
                textSalario.text = "Media - Cotas Parlamentares"
                textSalarioMesValue.text = "R$ 40.330,00"
                textSalarioAnoValue.text = "R$ 483.960,00"
                textSalarioMandatoValue.text = "R$ 1.935.840,00"
                imageQuestion.setOnClickListener {
                    animaView(it)
                    clickOptionOrQuestion(
                        "Cotas Parlamentares",
                        "A Cota para o Exercício da Atividade " +
                            "Parlamentar – CEAP (antiga verba indenizatória) é uma cota única " +
                            "mensal destinada a custear os gastos dos deputados exclusivamente " +
                            "vinculados ao exercício da atividade parlamentar.",
                        "https://www2.camara.leg.br/transparencia/acesso-a-informacao/" +
                                "copy_of_perguntas-frequentes/cota-para-o-exercicio-da-atividade-" +
                                "parlamentar#:~:text=A%20Cota%20para%20o%20Exerc%C3%ADcio," +
                                "ao%20exerc%C3%ADcio%20da%20atividade%20parlamentar.")
                }
            }
            frameCotas.apply {
                crossFade.crossFade(this)
            }
        }
    }

    private fun clickOptionOrQuestion(description: String, text: String, url: String){
        val dialog = createDialog()
        val viewDialog = layoutInflater.inflate(R.layout.layout_dialog_question, null)
        val imageClose = viewDialog.findViewById<ImageView>(R.id.image_close)
        val textTitle = viewDialog.findViewById<TextView>(R.id.text_description_dialog)
        val textDescription = viewDialog.findViewById<TextView>(R.id.text_not_value)
        val seeMore = viewDialog.findViewById<TextView>(R.id.text_see_more)
        textTitle.text = description
        textDescription.text = text
        seeMore.setOnClickListener {
            animaView(seeMore)
            create.dismiss()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        imageClose.setOnClickListener { create.dismiss() }
        dialog.setView(viewDialog)
        create = dialog.create()
        create.show()
    }

    private fun animaView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
    }

    private fun listenerBack() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                animaView(it)
                finish()
            }
        }
    }
}