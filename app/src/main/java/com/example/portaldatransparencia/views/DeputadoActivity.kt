package com.example.portaldatransparencia.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeputadoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val mainViewModel: DeputadoViewModel by viewModel()
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = Bundle().getString("id").toString()
        setupViews()
        observer()
    }

    private fun setupViews(){
        val tabs = arrayOf(R.string.junho, R.string.julho, R.string.agosto)
        val tabLayout = binding.tabGastos
        val pagerGastos = binding.viewPagerGastos
        val adapter = TabViewPagerAdapter(this)
        pagerGastos.adapter = adapter

        TabLayoutMediator(tabLayout, pagerGastos){ tab, position ->
            tab.text = getString(tabs[position])
        }.attach()
    }

    private fun observer() {
        mainViewModel.searchDataDeputado(id).observe(this) {
            it?.let { result ->
                when (result) {
                    is ResultIdRequest.Success -> {
                        result.dado?.let { deputado ->

                        }
                    }
                    is ResultIdRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultIdRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                    else -> {}
                }
            }
        }
    }

}