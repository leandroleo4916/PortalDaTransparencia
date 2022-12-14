package com.example.portaldatransparencia.views.activity.votacoes

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.VotacoesCamaraAdapter
import com.example.portaldatransparencia.databinding.ActivityVotacoesBinding
import com.example.portaldatransparencia.remote.ResultVotacoesCamara
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityVotacoesCamara: AppCompatActivity() {

    private val binding by lazy { ActivityVotacoesBinding.inflate(layoutInflater) }
    private val viewModel: VotacoesViewModelCamara by viewModel()
    private lateinit var adapter: VotacoesCamaraAdapter
    private val statusView: EnableDisableView by inject()
    private var votacoesSize = 0
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        observerVotacoesCamara()
        modifyTitle()
        listener()
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    private fun modifyTitle() {
        binding.layoutTop.run {
            textViewTitleTop.text = "Votacões - Deputados"
            textViewDescriptionTop.text = "Todas as votações registradas"
        }
    }

    private fun recycler() {
        val recycler = binding.recyclerVotacoes
        adapter = VotacoesCamaraAdapter(baseContext)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerVotacoesCamara() {

        viewModel.gastoGeralCamara(page).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultVotacoesCamara.Success -> {
                        result.dado?.let { votacoes ->
                            adapter.updateData(votacoes.dados)
                            votacoesSize += votacoes.dados.size
                            page += 1
                            if (votacoes.dados.size == 200){
                                observerVotacoesCamara()
                            }
                            binding.run {
                                statusView.run {
                                    disableView(progressVotacoes)
                                    enableView(iconVotacoes)
                                    enableView(textNumberVotacoes)
                                }
                                textNumberVotacoes.text = votacoesSize.toString()+" Votações"
                            }
                        }
                    }
                    is ResultVotacoesCamara.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultVotacoesCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }
}