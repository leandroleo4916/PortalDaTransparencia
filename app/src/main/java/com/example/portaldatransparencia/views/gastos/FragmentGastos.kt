package com.example.portaldatransparencia.views.gastos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
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
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = DespesasAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer(){

        viewModel.searchDespesasDeputado(id, ano).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultDespesasRequest.Success -> {
                        result.dado?.let { despesas ->
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
}