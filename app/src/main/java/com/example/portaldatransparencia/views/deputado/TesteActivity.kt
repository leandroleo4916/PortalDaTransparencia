package com.example.portaldatransparencia.views.deputado

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.SimpleGridItemBinding
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import com.example.portaldatransparencia.views.gastos.DespesasViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class TesteActivity : AppCompatActivity() {

    private val binding by lazy { SimpleGridItemBinding.inflate(layoutInflater) }
    private val viewModel: DespesasViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observer("204521", "2022")

    }

    private fun observer(id: String, year: String) {

        viewModel.searchDespesasDeputado(id, year).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultDespesasRequest.Success -> {
                        result.dado?.let { despesas ->
                            setupViewGeral(despesas.dados[0].urlDocumento)
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

    private fun setupViewGeral(note: String){

    }

}