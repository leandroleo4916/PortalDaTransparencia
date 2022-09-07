package com.example.portaldatransparencia.views.gastos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.FilterItem
import com.example.portaldatransparencia.dataclass.toChip
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
        observer()
        filterYear()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = DespesasAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer(){

        viewModel.searchDespesasDeputado("204521", "2022").observe(viewLifecycleOwner){
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
        binding!!.textNotesSend.text = dados.size.toString()+" notas"
        var total = 0.0
        dados.forEach { it ->
            total = (total+it.valorDocumento)
            binding!!.textTotal.text = "R$ "+total.toString()
        }
    }

    private fun filterYear(){
        val filter = arrayOf(
            FilterItem(1,"2022"),
            FilterItem(2,"2021"),
            FilterItem(3,"2020"),
            FilterItem(4,"2019"),
            FilterItem(5,"2018"),
            FilterItem(6,"2017"),
            FilterItem(7,"2016"),
            FilterItem(8,"2015")
        )

        filter.forEach { filter ->
            binding?.chipFilter?.addView(filter.toChip(requireContext()))
        }
    }
}