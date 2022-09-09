package com.example.portaldatransparencia.views.gastos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class FragmentGastos: Fragment(R.layout.fragment_gastos) {

    private var binding: FragmentGastosBinding? = null
    private val viewModel: DespesasViewModel by viewModel()
    private lateinit var adapter: DespesasAdapter
    private lateinit var chipEnabled: Chip
    private lateinit var id: String
    private lateinit var ano: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGastosBinding.bind(view)
        chipEnabled = binding!!.chip2022
        recyclerView()
        observer("204521", "2022")
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = DespesasAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer(id: String, year: String) {

        viewModel.searchDespesasDeputado(id, year).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultDespesasRequest.Success -> {
                        result.dado?.let { despesas ->
                            if (despesas.dados.isNotEmpty()){
                                calculateNotes(despesas.dados)
                                adapter.updateData(despesas.dados)
                            }else{
                                binding?.progressDespesas?.visibility = View.GONE
                                binding?.textNotesSend?.visibility = View.INVISIBLE
                                binding?.textTotal?.text = "Não há dados no ano ${year}"
                                adapter.updateData(despesas.dados)
                            }
                        }
                    }
                    is ResultDespesasRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultDespesasRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun calculateNotes(dados: List<DadoDespesas>) {
        (dados.size.toString()+" notas").also { binding!!.textNotesSend.text = it }
        var total = 0.0
        dados.forEach { it -> total = (total+it.valorDocumento) }

        val format = DecimalFormat("#.00")
        val formatTotal = format.format(total)
        ("R$ $formatTotal").also { binding!!.textTotal.text = it }

        binding?.progressDespesas?.visibility = View.GONE
        binding?.textNotesSend?.visibility = View.VISIBLE
        binding?.textTotal?.visibility = View.VISIBLE
    }

    private fun listenerChip(){
        binding?.run {
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

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {
        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        chipEnabled = viewDisabled
        observer("204521", viewDisabled.text as String)
    }
}