package com.example.portaldatransparencia.views.camara.deputado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.network.ApiServiceIdDeputado
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.TabViewAdapterDeputado
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeputadoActivity: FragmentActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val mainViewModel: DeputadoViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val verifyInternet: ValidationInternet by inject()
    private val statusView: EnableDisableView by inject()
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPagerDeputado.isUserInputEnabled = false
        id = intent.extras?.getString("id").toString()
        securityPreferences.putString("id", id)
        setupViewGeral()
        observer()
        listener()
    }

    override fun onBackPressed() = finish()

    private fun setupViewGeral(){
        val tabs = arrayOf(R.string.geral, R.string.gastos, R.string.acao, R.string.frente)
        val icons = arrayOf(
            R.drawable.ic_avatar,
            R.drawable.ic_money,
            R.drawable.ic_project,
            R.drawable.ic_front
        )
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

        val internet = verifyInternet.validationInternet(baseContext.applicationContext)
        if (internet){
            mainViewModel.searchDataDeputado(id)
            mainViewModel.responseLiveData.observe(this){
                addElementView(it)
            }
            mainViewModel.responseErrorLiveData.observe(this){
                errorData(R.string.api_nao_respondeu)
            }
        }
        else errorData(R.string.verifique_sua_internet)
    }

    private fun errorData(value: Int){
        binding.run {
            textNotValue.text = getString(value)
            statusView.enableView(textNotValue)
            statusView.disableView(progressDeputado)
        }
    }

    private fun addElementView(item: Dados) {
        binding.run {
            Glide.with(application)
                .load(item.ultimoStatus.urlFoto)
                .circleCrop()
                .into(imageDeputado)
            textDescription.text = item.ultimoStatus.nome
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