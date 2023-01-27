package com.example.portaldatransparencia.views.senado.senador

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.ParlamentarItem
import com.example.portaldatransparencia.repository.ResultSenadorRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.TabViewAdapterSenador
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SenadorActivity: AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val senadorViewModel: SenadorViewModel by viewModel()
    private val statusView: EnableDisableView by inject()
    private var id = ""
    private var nome = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPagerDeputado.isUserInputEnabled = false
        id = intent.extras?.getString("id").toString()
        nome = intent.extras?.getString("nome").toString()
        securityPreferences.putString("id", id)
        securityPreferences.putString("nome", nome)
        setupViewGeral()
        observer()
        listener()

    }

    private fun setupViewGeral(){
        val tabs = arrayOf(R.string.geral, R.string.gastos, R.string.acao)
        val icons = arrayOf(
            R.drawable.ic_avatar,
            R.drawable.ic_money,
            R.drawable.ic_project
        )
        val tabLayout = binding.tabDeputado
        val pagerGeral = binding.viewPagerDeputado
        val adapter = TabViewAdapterSenador(this)
        pagerGeral.adapter = adapter

        TabLayoutMediator(tabLayout, pagerGeral){ tab, position ->
            tab.text = getString(tabs[position])
            tab.icon = getDrawable(icons[position])
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
                        result.exception.message?.let {  }
                    }
                    is ResultSenadorRequest.ErrorConnection -> {
                        result.exception.message?.let {  }
                    }
                }
            }
        }
    }

    private fun addElementView(item: ParlamentarItem) {

        val itemSenador = item.identificacaoParlamentar
        val itemPhoto = item.identificacaoParlamentar
        val https = "https:/"
        val urlFoto = itemPhoto.urlFotoParlamentar.split(":/")
        val photo = https+urlFoto[1]

        binding.run {
            Glide.with(application)
                .load(photo)
                .circleCrop()
                .into(imageDeputado)
            textDescription.text = itemSenador.nomeParlamentar
            statusView.run {
                disableView(progressDeputado)
                enableView(textDescription)
                enableView(imageDeputado)
            }
        }
    }

    private fun listener(){
        binding.imageBack.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click))
            finish()
        }
    }
}