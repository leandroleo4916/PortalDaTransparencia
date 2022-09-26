package com.example.portaldatransparencia.views.senador

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.remote.ResultSenadorRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.EnableDisableView
import com.example.portaldatransparencia.views.TabViewAdapterSenador
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SenadorActivity: AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val senadorViewModel: SenadorViewModel by viewModel()
    private val statusView: EnableDisableView by inject()
    private val calculateAge: CalculateAge by inject()
    private var name = ""
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.extras?.getString("id").toString()
        securityPreferences.storeString("name", name)
        setupViewGeral()
        observer()

    }

    private fun setupViewGeral(){
        val tabs = arrayOf(R.string.geral, R.string.gastos, R.string.acao)
        val tabLayout = binding.tabDeputado
        val pagerGeral = binding.viewPagerDeputado
        val adapter = TabViewAdapterSenador(this)
        pagerGeral.adapter = adapter

        TabLayoutMediator(tabLayout, pagerGeral){ tab, position ->
            tab.text = getString(tabs[position])
        }.attach()
    }

    private fun observer() {
        senadorViewModel.searchDataSenador(id).observe(this) {
            it?.let { result ->
                when (result) {
                    is ResultSenadorRequest.Success -> {
                        result.dado?.let { senador ->
                            addElementView(senador.detalheParlamentar.parlamentar)
                        }
                    }
                    is ResultSenadorRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultSenadorRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun addElementView(item: Parlamentar) {
        val itemSenador = item.identificacaoParlamentar
        //val itemDados = item.dadosBasicosParlamentar
        binding.run {
            Glide.with(application)
                .load(itemSenador.urlFotoParlamentar)
                .circleCrop()
                .into(imageDeputado)
            /*val age = calculateAge.age(itemDados.dataNascimento)
            ("${itemSenador.nomeParlamentar}, $age anos, natutal do " +
                    "${itemDados.naturalidade} - ${itemDados.ufNaturalidade}. " +
                    "Filiado ao partido ${itemSenador.siglaPartidoParlamentar}")
                .also { textDescription.text = it }*/
            statusView.disableView(progressDeputado)
            statusView.enableView(textDescription)
            statusView.enableView(imageDeputado)
        }
    }
}