package com.example.portaldatransparencia.views.deputado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.CalculateAge
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.TabViewAdapterDeputado
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeputadoActivity: AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val mainViewModel: DeputadoViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private val calculateAge: CalculateAge by inject()
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.extras?.getString("id").toString()
        securityPreferences.putString("id", id)
        setupViewGeral()
        observer()
        listener()
    }

    private fun setupViewGeral(){
        val tabs = arrayOf(R.string.geral, R.string.gastos, R.string.acao, R.string.frente)
        val icons = arrayOf(R.drawable.insta, R.drawable.phone, R.drawable.twitter, R.drawable.voto)
        val tabLayout = binding.tabDeputado
        val pagerGeral = binding.viewPagerDeputado
        val adapter = TabViewAdapterDeputado(this)
        pagerGeral.adapter = adapter

        TabLayoutMediator(tabLayout, pagerGeral){ tab, position ->
            tab.text = getString(tabs[position])
            tab.icon = getDrawable(icons[position])
        }.attach()
    }

    private fun observer() {
        mainViewModel.searchDataDeputado(id).observe(this) {
            it?.let { result ->
                when (result) {
                    is ResultIdRequest.Success -> {
                        result.dado?.let { deputado -> addElementView(deputado) }
                    }
                    is ResultIdRequest.Error -> {
                        result.exception.message?.let {  }
                    }
                    is ResultIdRequest.ErrorConnection -> {
                        result.exception.message?.let {  }
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
            textDescription.text = item.dados.ultimoStatus.nome
            statusView.disableView(progressDeputado)
            statusView.enableView(textDescription)
            statusView.enableView(imageDeputado)
        }
    }

    private fun listener(){
        binding.imageBack.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click))
            finish()
        }
    }
}