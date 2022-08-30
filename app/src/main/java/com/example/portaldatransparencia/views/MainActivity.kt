package com.example.portaldatransparencia.views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.adapter.MainAdapter
import com.example.portaldatransparencia.databinding.ActivityMainBinding
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.interfaces.IClickDeputado
import com.example.portaldatransparencia.remote.ResultRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), IClickDeputado {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var adapter: MainAdapter
    private lateinit var data: List<Dado>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        observer()
        search()

    }

    private fun recycler() {
        val recycler = binding.recyclerDeputados
        adapter = MainAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observer() {
        mainViewModel.searchDataWeather(ordenarPor = "nome").observe(this) {
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

    private fun search() {
        binding.textSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count != 0) {

                } else {

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun clickDeputado(id: String) {
        val intent = Intent()
        intent.putExtra("id", id)
        startActivity(intent)
    }
}