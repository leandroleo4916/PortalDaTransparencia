package com.example.portaldatransparencia.views.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.adapter.MainAdapter
import com.example.portaldatransparencia.databinding.ActivityMainBinding
import com.example.portaldatransparencia.interfaces.IClickDeputado
import com.example.portaldatransparencia.interfaces.INotification
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.views.EnableDisableView
import com.example.portaldatransparencia.views.deputado.DeputadoActivity
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), IClickDeputado, INotification {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private lateinit var adapter: MainAdapter
    private var chipEnabled: Chip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        observer()
        search()
        listenerChip()
    }

    private fun recycler() {
        val recycler = binding.recyclerDeputados
        adapter = MainAdapter(this, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observer() {
        mainViewModel.searchData(ordenarPor = "nome").observe(this) {
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { deputados ->
                            hideView.disableProgress(binding.progressMain)
                            adapter.updateData(deputados.dados)
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
                adapter.filter.filter(s)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun clickDeputado(id: String) {
        val intent = Intent(this, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun listenerChip(){
        binding.run {
            chipAvante.setOnClickListener { modify(chipEnabled, chipAvante) }
            chipCidadania.setOnClickListener { modify(chipEnabled, chipCidadania) }
            chipDc.setOnClickListener { modify(chipEnabled, chipDc) }
            chipDem.setOnClickListener { modify(chipEnabled, chipDem) }
            chipMdb.setOnClickListener { modify(chipEnabled, chipMdb) }
            chipNovo.setOnClickListener { modify(chipEnabled, chipNovo) }
            chipPatri.setOnClickListener { modify(chipEnabled, chipPatri) }
            chipPatriota.setOnClickListener { modify(chipEnabled, chipPatriota) }
            chipPcb.setOnClickListener { modify(chipEnabled, chipPcb) }
            chipPcdob.setOnClickListener { modify(chipEnabled, chipPcdob) }
            chipPco.setOnClickListener { modify(chipEnabled, chipPco) }
            chipPdt.setOnClickListener { modify(chipEnabled, chipPdt) }
            chipPhs.setOnClickListener { modify(chipEnabled, chipPhs) }
            chipPl.setOnClickListener { modify(chipEnabled, chipPl) }
            chipPros.setOnClickListener { modify(chipEnabled, chipPros) }
            chipPsc.setOnClickListener { modify(chipEnabled, chipPsc) }
            chipPmb.setOnClickListener { modify(chipEnabled, chipPmb) }
            chipPp.setOnClickListener { modify(chipEnabled, chipPp) }
            chipPt.setOnClickListener { modify(chipEnabled, chipPt) }
            chipUniao.setOnClickListener { modify(chipEnabled, chipUniao) }
        }
    }

    private fun modify(viewEnabled: Chip?, viewDisabled: Chip) {
        if (viewEnabled != null){
            viewEnabled.isChecked = false
            viewDisabled.isChecked = true

            if (viewEnabled != viewDisabled) {
                chipEnabled = viewDisabled
            }
            else {
                viewDisabled.isChecked = false
                chipEnabled = null
            }
        }
        else {
            viewDisabled.isChecked = true
            chipEnabled = viewDisabled
        }
        if (!viewDisabled.isChecked) adapter.filter.filter("")
        else adapter.filter.filter(viewDisabled.text as String)
    }

    override fun notification() {
        Toast.makeText(this, "Não encontrado deputado com essas iniciais",
            Toast.LENGTH_SHORT).show()
    }
}