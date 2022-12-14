package com.example.portaldatransparencia.views.deputado.gastos_deputado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormatValor
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastos: Fragment(R.layout.fragment_gastos), INoteDespesas {

    private var binding: FragmentGastosBinding? = null
    private val viewModel: DespesasViewModel by viewModel()
    private lateinit var adapter: DespesasAdapter
    private val adapterDimension: DimensionAdapter by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private val formatValue: FormatValor by inject()
    private lateinit var chipEnabled: Chip
    private lateinit var id: String
    private var total = 0.0
    private var numberNote = 0
    private var page = 1
    private var listDadosDimension: ArrayList<DadoDespesas> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGastosBinding.bind(view)

        chipEnabled = binding!!.chipGroupItem.chip2022
        id = securityPreferences.getString("id")
        recyclerView()
        observer(id, "2022", page)
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = DespesasAdapter(this, FormatValor(), requireContext())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val recyclerDimension = binding!!.frameRecyclerDimension.recyclerDimension
        recyclerDimension.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        recyclerDimension.adapter = adapterDimension
    }

    private fun observer(id: String, year: String, pagina: Int) {

        viewModel.searchDespesasDeputado(id, year, pagina).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultDespesasRequest.Success -> {
                        result.dado?.let { despesas ->
                            if (despesas.dados.isNotEmpty()){

                                if (page == 1) listDadosDimension = arrayListOf()
                                listDadosDimension += despesas.dados

                                statusView.disableView(binding!!.textNotValue)
                                val size = despesas.dados.size
                                numberNote += size
                                calculateNumberNote()
                                calculateTotal(despesas.dados, page)
                                adapter.updateData(despesas.dados, page)
                                page += 1

                                if (size >= 100) observer(id, year, page)
                                else viewModel.captureDataNotes(listDadosDimension, adapterDimension)

                            }else{
                                if (numberNote == 0) {
                                    binding?.run {
                                        statusView.disableView(progressDespesas)
                                        statusView.enableView(textNotValue)
                                        textNotValue.text = "Não há dados no ano ${year}"
                                    }
                                    adapter.updateData(despesas.dados, page)
                                }
                            }
                        }
                    }
                    is ResultDespesasRequest.Error -> {
                        result.exception.message?.let {  }
                    }
                    is ResultDespesasRequest.ErrorConnection -> {
                        result.exception.message?.let {  }
                    }
                }
            }
        }
    }

    private fun calculateNumberNote(){
        binding!!.textNotesSend.text = "$numberNote notas"
    }

    private fun calculateTotal(dados: List<DadoDespesas>, page: Int) {
        if (page == 1) total = 0.0
        dados.forEach { total = (total+it.valorDocumento) }

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
        numberNote = 0
        page = 1
        chipEnabled = viewDisabled
        binding?.run {
            statusView.enableView(progressDespesas)
            statusView.disableView(textNotesSend)
            statusView.disableView(textTotal)
            statusView.disableView(imageView1)
            statusView.disableView(imageView2)
        }
        adapter.updateData(deputados = arrayListOf(), 1)
        adapterDimension.updateData(arrayListOf())
        observer(id, viewDisabled.text as String, page)
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