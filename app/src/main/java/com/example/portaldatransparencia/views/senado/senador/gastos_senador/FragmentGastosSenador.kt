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
import com.example.portaldatransparencia.interfaces.IClickTipoDespesa
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.interfaces.INotification
import com.example.portaldatransparencia.repository.ResultCotaRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.Normalizer
import java.util.regex.Pattern

class FragmentGastosSenador: Fragment(R.layout.fragment_gastos), INoteDespesas, IClickTipoDespesa, INotification {

    private var binding: FragmentGastosBinding? = null
    private val viewModelGastos: CotasSenadorViewModel by viewModel()
    private lateinit var adapter: GastoSenadorAdapter
    private lateinit var adapterDimension: DimensionAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
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
        observerGastosSenador()
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = GastoSenadorAdapter(FormaterValueBilhoes(), this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val recyclerDimension = binding!!.frameRecyclerDimension.recyclerDimension
        adapterDimension = DimensionAdapter(FormaterValueBilhoes(), this)
        recyclerDimension.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        recyclerDimension.adapter = adapterDimension
    }

    private fun observerGastosSenador() {

        viewModelGastos.searchGastosSenador(ano, nome).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultCotaRequest.Success -> {
                        result.dado?.let { gastos ->
                            if (gastos.gastosSenador.isNotEmpty()){
                                dados = gastos.gastosSenador
                                adapter.updateDataSenador(dados)
                                statusView.disableView(binding!!.layoutProgress.progressActive)
                                viewModelGastos.addCotasAdapterDimension(dados, adapterDimension)
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
                            errorCallApi(getString(R.string.erro_ao_buscar_dados))
                        }
                    }
                }
            }
        }
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
                chip2014.setOnClickListener { modify(chipEnabled, chip2014) }
                chip2013.setOnClickListener { modify(chipEnabled, chip2013) }
                chip2012.setOnClickListener { modify(chipEnabled, chip2012) }
                chip2011.setOnClickListener { modify(chipEnabled, chip2011) }
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
        observerGastosSenador()
    }

    override fun listenerDespesas(note: String?) {
        if (note != null){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(note))
            startActivity(browserIntent)
        } else {
            Toast.makeText(context, getString(R.string.comprovante_nao_enviado),
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun clickTipoDespesa(type: String) {
        val typeSubString = type.substring(0,5)
        adapter.openDataSelect(typeSubString)
    }

    override fun notification() {
        Toast.makeText(requireContext(), "Comprovante não enviado", Toast.LENGTH_SHORT).show()
    }
}