package com.example.portaldatransparencia.views.senado.senador.gastos_senador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.adapter.GastoSenadorAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.GastosSenador
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.interfaces.IClickTipoDespesa
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.repository.ResultCotaRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormatValueFloat
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.camara.deputado.gastos_deputado.DespesasViewModel
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastosSenador: Fragment(R.layout.fragment_gastos), INoteDespesas, IClickTipoDespesa {

    private var binding: FragmentGastosBinding? = null
    private val viewModelGastos: DespesasViewModel by viewModel()
    private val adapter: GastoSenadorAdapter by inject()
    private lateinit var adapterDimension: DimensionAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private val formatFloat: FormatValueFloat by inject()
    private lateinit var chipEnabled: Chip
    private lateinit var nome: String
    private var ano = "2023"
    private var dados: List<GastosSenador> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGastosBinding.bind(view)

        chipEnabled = binding!!.chipGroupItem.chip2023
        nome = securityPreferences.getString("nome")
        recyclerView()
        observerGastosSenador(ano, nome)
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val recyclerDimension = binding!!.frameRecyclerDimension.recyclerDimension
        adapterDimension = DimensionAdapter(FormaterValueBilhoes(), this)
        recyclerDimension.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        recyclerDimension.adapter = adapterDimension
    }

    private fun observerGastosSenador(year: String, nome: String) {

        viewModelGastos.searchGastosSenador(year, nome).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultCotaRequest.Success -> {
                        result.dado?.let { gastos ->
                            if (gastos.gastosSenador.isNotEmpty()){
                                dados = gastos.gastosSenador
                                adapter.updateDataSenador(dados)
                                captureDataNotes()
                            }
                            else errorCallApi("Não tem dados para $ano")
                        }
                    }
                    is ResultCotaRequest.Error -> {
                        result.exception.message?.let {
                            errorCallApi("Não tem dados para $ano")
                        }
                    }
                    is ResultCotaRequest.ErrorConnection -> {
                        result.exception.message?.let {
                            errorCallApi("Erro ao buscar os dados, tente novamnete mais tarde!")
                        }
                    }
                }
            }
        }
    }

    private fun captureDataNotes(){

        val subList: ArrayList<SublistDataClass> = arrayListOf()
        val size = dados.size
        var total = 0.0F
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
            if (value != 0.0F){
                total += value
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
        }
        if (total.toInt() != 0){
            subList.add(SublistDataClass(total.toInt(), "$size notas",
                "https://cdn-icons-png.flaticon.com/512/116/116638.png",
                "Total geral"))
        }
        if (divulgacao.toInt() != 0){
            subList.add(SublistDataClass(divulgacao.toInt(), "Divulgação parlamentar",
                "https://cdn-icons-png.flaticon.com/512/6520/6520327.png",
                "Divulgação"))
        }
        if (passagens.toInt() != 0){
            subList.add(SublistDataClass(passagens.toInt(), "Passagens aéreas",
                "https://cdn-icons-png.flaticon.com/512/5014/5014749.png",
                "Passagens"))
        }
        if (contratacao.toInt() != 0){
            subList.add(SublistDataClass(contratacao.toInt(), "Consultoria, assessoria",
                "https://cdn-icons-png.flaticon.com/512/1522/1522778.png",
                "Contratação"))
        }
        if (locomocao.toInt() != 0){
            subList.add(SublistDataClass(locomocao.toInt(), "Hospedagem, alimentação",
                "https://cdn-icons-png.flaticon.com/512/6799/6799692.png",
                "Locomoção"))
        }
        if (aquisicao.toInt() != 0){
            subList.add(SublistDataClass(aquisicao.toInt(), "Aquisição de materiais",
                "https://cdn-icons-png.flaticon.com/512/6169/6169675.png",
                "Aquisição"))
        }
        if (servico.toInt() != 0){
            subList.add(SublistDataClass(servico.toInt(), "Serviços postais",
                "https://cdn-icons-png.flaticon.com/512/4280/4280211.png",
                "Serviços"))
        }
        if (outros.toInt() != 0){
            subList.add(SublistDataClass(outros.toInt(), "Outros serviços",
                "https://cdn-icons-png.flaticon.com/512/4692/4692103.png",
                "Outros"))
        }
        statusView.disableView(binding!!.layoutProgress.progressActive)
        adapterDimension.updateData(subList)
    }

    private fun errorCallApi(value: String){
        binding!!.layoutProgress.run {
            statusView.disableView(progressActive)
            statusView.enableView(textNotValue)
            textNotValue.text = value
        }
    }

    private fun listenerChip(){
        binding?.run {
            chipGroupItem.run {
                chip2023.setOnClickListener { modify(chipEnabled, chip2023) }
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
        binding!!.layoutProgress.run {
            statusView.enableView(progressActive)
            statusView.disableView(textNotValue)
        }
        ano = viewDisabled.text.toString()
        adapter.updateDataSenador(listOf())
        adapterDimension.updateData(arrayListOf())
        observerGastosSenador(ano, nome)
    }

    override fun listenerDespesas(note: String?) {
        if (note != null){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(note))
            startActivity(browserIntent)
        } else {
            Toast.makeText(context, "Comprovante não enviado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun clickTipoDespesa(type: String) {}
}