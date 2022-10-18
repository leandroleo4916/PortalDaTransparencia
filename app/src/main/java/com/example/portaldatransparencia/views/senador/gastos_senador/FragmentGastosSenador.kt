package com.example.portaldatransparencia.views.senador.gastos_senador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.adapter.GastorSenadorAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.GastosSenador
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.remote.ResultCotaRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.deputado.gastos_deputado.DespesasViewModel
import com.example.portaldatransparencia.util.FormatValor
import com.example.portaldatransparencia.util.FormatValueFloat
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastosSenador: Fragment(R.layout.fragment_gastos), INoteDespesas {

    private var binding: FragmentGastosBinding? = null
    private val viewModelGastos: DespesasViewModel by viewModel()
    private lateinit var adapter: GastorSenadorAdapter
    private lateinit var adapterDimension: DimensionAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private val formatValue: FormatValor by inject()
    private val formatFloat: FormatValueFloat by inject()
    private lateinit var chipEnabled: Chip
    private lateinit var nome: String
    private var ano = "2022"
    private var numberNote = 0
    private var dados: List<GastosSenador> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGastosBinding.bind(view)

        chipEnabled = binding!!.chipGroupItem.chip2022
        nome = securityPreferences.getString("nome")
        recyclerView()
        observerGastosSenador(ano, nome)
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = GastorSenadorAdapter(FormatValor())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val recyclerDimension = binding!!.frameRecyclerDimension.recyclerDimension
        adapterDimension = DimensionAdapter(FormatValor(), requireContext())
        recyclerDimension.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerDimension.adapter = adapterDimension
    }

    private fun observerGastosSenador(year: String, nome: String) {

        viewModelGastos.searchGastosSenador(year, nome).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultCotaRequest.Success -> {
                        result.dado?.let { gastos ->
                            if (gastos.gastosSenador.isNotEmpty()){
                                numberNote = gastos.gastosSenador.size
                                calculateNumberNote()
                                dados = gastos.gastosSenador
                                calculateTotalSenador()
                                adapter.updateDataSenador(dados)
                                captureDataNotes()
                            }
                            else errorCallApi("Não tem dados para $ano")
                        }
                    }
                    is ResultCotaRequest.Error -> {
                        result.exception.message?.let { it ->
                            errorCallApi("Não tem dados para $ano")
                        }
                    }
                    is ResultCotaRequest.ErrorConnection -> {
                        result.exception.message?.let { it ->
                            errorCallApi("Erro ao buscar os dados, tente novamnete mais tarde!")
                        }
                    }
                }
            }
        }
    }

    private fun captureDataNotes(){

        val subList: ArrayList<SublistDataClass> = arrayListOf()
        var aluguel = 0.0F
        var divulgacao = 0.0F
        var passagens = 0.0F
        var contratacao = 0.0F
        var locomocao = 0.0F
        var aquisicao = 0.0F
        var servico = 0.0F
        var outros = 0.0F

        dados.forEach {
            val value = formatFloat.formatFloat(it.valorReembolsado)
            when (it.tipoDespesa.substring(0,5)){
                "Alugu" -> aluguel += value
                "Divul" -> divulgacao += value
                "Passa" -> passagens += value
                "Contr" -> contratacao += value
                "Locom" -> locomocao += value
                "Aquis" -> aquisicao += value
                "Servi" -> servico += value
                else -> outros += value
            }
        }
        if (aluguel.toInt() != 0){
            subList.add(SublistDataClass(aluguel.toInt(), "Aluguel de imóveis", R.drawable.back_7,
                "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg"))
        }
        if (divulgacao.toInt() != 0){
            subList.add(SublistDataClass(divulgacao.toInt(), "Divulgação parlamentar", R.drawable.back_6,
                "https://cdn-icons-png.flaticon.com/512/6520/6520327.png"))
        }
        if (passagens.toInt() != 0){
            subList.add(SublistDataClass(passagens.toInt(), "Passagens aéreas", R.drawable.back_5,
                "https://cdn-icons-png.flaticon.com/512/5014/5014749.png"))
        }
        if (contratacao.toInt() != 0){
            subList.add(SublistDataClass(contratacao.toInt(), "Consultoria, assessoria", R.drawable.back_4,
                "https://cdn-icons-png.flaticon.com/512/1522/1522778.png"))
        }
        if (locomocao.toInt() != 0){
            subList.add(SublistDataClass(locomocao.toInt(), "Hospedagem, alimentação", R.drawable.back_3,
                "https://cdn-icons-png.flaticon.com/512/6799/6799692.png"))
        }
        if (aquisicao.toInt() != 0){
            subList.add(SublistDataClass(aquisicao.toInt(), "Aquisição de materiais", R.drawable.back_2,
                "https://cdn-icons-png.flaticon.com/512/4135/4135635.png"))
        }
        if (servico.toInt() != 0){
            subList.add(SublistDataClass(servico.toInt(), "Serviços postais", R.drawable.back_1,
                "https://cdn-icons-png.flaticon.com/512/4280/4280211.png"))
        }
        if (outros.toInt() != 0){
            subList.add(SublistDataClass(outros.toInt(), "Outros serviços", R.drawable.back_7,
                "https://cdn-icons-png.flaticon.com/512/4692/4692103.png"))
        }
        adapterDimension.updateData(subList)
    }

    private fun errorCallApi(value: String){
        binding?.run {
            statusView.disableView(progressDespesas)
            statusView.enableView(textNotValue)
            textNotValue.text = value
        }
    }

    private fun calculateNumberNote(){
        binding!!.textNotesSend.text = "$numberNote notas"
    }

    private fun calculateTotalSenador() {

        var total = 0.0
        dados.forEach  {
            val valor = it.valorReembolsado
            total += formatFloat.formatFloat(valor)
        }
        statusView(total)
    }

    private fun statusView(total: Double) {

        val formatTotal = formatValue.formatValor(total)
        binding?.run {
            (formatTotal).also { textTotal.text = it }
            statusView.disableView(progressDespesas)
            statusView.enableView(textNotesSend)
            statusView.enableView(textTotal)
            statusView.enableView(imageView1)
            statusView.enableView(imageView2)
        }
    }

    private fun listenerChip(){
        binding?.run {
            chipGroupItem.run {
                chip2022.setOnClickListener { modify(chipEnabled, chip2022) }
                chip2021.setOnClickListener { modify(chipEnabled, chip2021) }
                chip2020.setOnClickListener { modify(chipEnabled, chip2020) }
                chip2019.setOnClickListener { modify(chipEnabled, chip2019) }
                chip2018.setOnClickListener { modify(chipEnabled, chip2018) }
                chip2017.setOnClickListener { modify(chipEnabled, chip2017) }
                chip2016.setOnClickListener { modify(chipEnabled, chip2016) }
                chip2015.setOnClickListener { modify(chipEnabled, chip2015) }
            }
        }
    }

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {

        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        chipEnabled = viewDisabled
        binding?.run {
            statusView.enableView(progressDespesas)
            statusView.disableView(textNotesSend)
            statusView.disableView(textNotValue)
            statusView.disableView(textTotal)
            statusView.disableView(imageView1)
            statusView.disableView(imageView2)
        }
        ano = viewDisabled.text.toString()
        adapter.updateDataSenador(listOf())
        adapterDimension.updateData(arrayListOf())
        observerGastosSenador(ano, nome)
    }

    override fun listenerDespesas(note: DadoDespesas) {
        if (note.urlDocumento != null){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(note.urlDocumento))
            startActivity(browserIntent)
        } else {
            Toast.makeText(context, "Comprovante não enviado", Toast.LENGTH_SHORT).show()
        }
    }
}