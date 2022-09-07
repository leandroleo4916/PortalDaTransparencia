package com.example.portaldatransparencia.views.gastos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.ModifyChip
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGastos: Fragment(R.layout.fragment_gastos) {

    private var binding: FragmentGastosBinding? = null
    private val viewModel: DespesasViewModel by viewModel()
    private lateinit var adapter: DespesasAdapter
    private lateinit var id: String
    private lateinit var ano: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGastosBinding.bind(view)
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
                            calculateNotes(despesas.dados)
                            adapter.updateData(despesas.dados)
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
        dados.forEach { it ->
            total = (total+it.valorDocumento)
            ("R$ $total").also { binding!!.textTotal.text = it }
        }
    }

    private fun listenerChip(){
        binding?.run {
            chip2022.setOnClickListener {
                modifyChip(ModifyChip(chip1 = true, chip2 = false, chip3 = false, chip4 = false,
                    chip5 = false, chip6 = false, chip7 = false, chip8 = false))
                observer("204521", chip2022.text as String)
            }
            chip2021.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = true, chip3 = false, chip4 = false,
                    chip5 = false, chip6 = false, chip7 = false, chip8 = false))
                observer("204521", chip2021.text as String)
            }
            chip2020.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = false, chip3 = true, chip4 = false,
                    chip5 = false, chip6 = false, chip7 = false, chip8 = false))
                observer("204521", chip2020.text as String)
            }
            chip2019.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = false, chip3 = false, chip4 = true,
                    chip5 = false, chip6 = false, chip7 = false, chip8 = false))
                observer("204521", chip2019.text as String)
            }
            chip2018.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = false, chip3 = false, chip4 = false,
                    chip5 = true, chip6 = false, chip7 = false, chip8 = false))
                observer("204521", chip2018.text as String)
            }
            chip2017.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = false, chip3 = false, chip4 = false,
                    chip5 = false, chip6 = true, chip7 = false, chip8 = false))
                observer("204521", chip2017.text as String)
            }
            chip2016.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = false, chip3 = false, chip4 = false,
                    chip5 = false, chip6 = false, chip7 = true, chip8 = false))
                observer("204521", chip2016.text as String)
            }
            chip2015.setOnClickListener {
                modifyChip(ModifyChip(chip1 = false, chip2 = false, chip3 = false, chip4 = false,
                    chip5 = false, chip6 = false, chip7 = false, chip8 = true))
                observer("204521", chip2015.text as String)
            }
        }
    }

    private fun modifyChip(chip: ModifyChip){
        binding?.run {
            chip2022.isChecked = chip.chip1
            chip2021.isChecked = chip.chip2
            chip2020.isChecked = chip.chip3
            chip2019.isChecked = chip.chip4
            chip2018.isChecked = chip.chip5
            chip2017.isChecked = chip.chip6
            chip2016.isChecked = chip.chip7
            chip2015.isChecked = chip.chip8
        }
    }
}