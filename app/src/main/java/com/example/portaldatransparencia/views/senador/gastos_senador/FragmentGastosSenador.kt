package com.example.portaldatransparencia.views.senador.gastos_senador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastorSenadorAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.GastosSenador
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.remote.ResultCotaRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.deputado.gastos_deputado.DespesasViewModel
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class FragmentGastosSenador: Fragment(R.layout.fragment_gastos), INoteDespesas {

    private var binding: FragmentGastosBinding? = null
    private val viewModelGastos: DespesasViewModel by viewModel()
    private lateinit var adapter: GastorSenadorAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private lateinit var chipEnabled: Chip
    private lateinit var nome: String
    private var ano = "2022"
    private var numberNote = 0

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
        adapter = GastorSenadorAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
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
                                calculateTotalSenador(gastos.gastosSenador)
                                adapter.updateDataSenador(gastos.gastosSenador)
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

    private fun calculateTotalSenador(dados: List<GastosSenador>) {

        var total = 0.0
        dados.forEach  {
            val valor = it.valorReembolsado
            total += if (valor.contains(",")){
                val value = valor.split(",")
                if (value[0].isNotEmpty()){
                    value[0].toFloat()
                }
                else 0.1F
            }
            else {
                if (valor.isNotEmpty()) valor.toFloat()
                else  0.0F
            }
        }
        statusView(total)
    }

    private fun statusView(total: Double) {
        val format = DecimalFormat("#.00")
        val formatTotal = format.format(total)

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