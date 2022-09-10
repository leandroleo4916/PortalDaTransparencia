package com.example.portaldatransparencia.views.deputado

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.example.portaldatransparencia.views.TabViewAdapterGeral
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DeputadoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val mainViewModel: DeputadoViewModel by viewModel()
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.extras?.getString("id").toString()
        setupViewGeral()
        observer()
    }

    private fun setupViewGeral(){
        val tabs = arrayOf(R.string.geral, R.string.gastos, R.string.acoes)
        val tabLayout = binding.tabDeputado
        val pagerGeral = binding.viewPagerDeputado
        val adapter = TabViewAdapterGeral(this)
        pagerGeral.adapter = adapter

        TabLayoutMediator(tabLayout, pagerGeral){ tab, position ->
            tab.text = getString(tabs[position])
        }.attach()
    }

    private fun observer() {
        mainViewModel.searchDataDeputado(id).observe(this) {
            it?.let { result ->
                when (result) {
                    is ResultIdRequest.Success -> {
                        result.dado?.let { deputado ->
                            addElementView(deputado)
                        }
                    }
                    is ResultIdRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultIdRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun addElementView(item: IdDeputadoDataClass) {
        binding.run {
            Glide.with(application)
                .load(item.dados.ultimoStatus.urlFoto)
                .circleCrop()
                .into(imageDeputado)
            val age = age(item.dados.dataNascimento)
            ("${item.dados.ultimoStatus.nome}, $age anos, nascido em " +
                    "${item.dados.municipioNascimento} - ${item.dados.ufNascimento}. " +
                    "Filiado ao partido ${item.dados.ultimoStatus.siglaPartido}")
                .also { textDescription.text = it }
            progressDeputado.visibility = View.GONE
            textDescription.visibility = View.VISIBLE
            imageDeputado.visibility = View.VISIBLE
        }
    }

    private fun age(birth: String): Int {
        val date = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val dateCurrent = LocalDate.now()
        val periodo = Period.between(date, dateCurrent)
        return periodo.years
    }
}