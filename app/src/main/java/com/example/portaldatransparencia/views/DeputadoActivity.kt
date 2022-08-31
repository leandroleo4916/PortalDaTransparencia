package com.example.portaldatransparencia.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.adapter.MainAdapter
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.remote.ResultRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeputadoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val mainViewModel: DeputadoViewModel by viewModel()
    private lateinit var adapter: MainAdapter
    private lateinit var data: List<Dado>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observer()

    }

    private fun observer() {
        mainViewModel.searchDataDeputado(ordenarPor = "nome").observe(this) {
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { deputados ->
                            adapter.updateData(deputados.dados)
                            data = deputados.dados
                        }
                    }
                    is ResultRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

}