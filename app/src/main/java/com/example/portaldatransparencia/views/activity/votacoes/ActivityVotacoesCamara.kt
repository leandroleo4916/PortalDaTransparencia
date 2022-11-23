package com.example.portaldatransparencia.views.activity.votacoes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.adapter.VotacoesCamaraAdapter
import com.example.portaldatransparencia.databinding.ActivityVotacoesBinding
import com.example.portaldatransparencia.remote.ResultVotacoesCamara
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityVotacoesCamara: AppCompatActivity() {

    private val binding by lazy { ActivityVotacoesBinding.inflate(layoutInflater) }
    private val viewModel: VotacoesViewModelCamara by viewModel()
    private lateinit var adapter: VotacoesCamaraAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        observerVotacoesCamara()
        modifyTitle()
    }

    private fun modifyTitle() {
        binding.layoutTop.run {
            textViewTitleTop.text = "Votacões - Deputados"
            textViewDescriptionTop.text = "Todas as votações registradas"
        }
    }

    private fun recycler() {
        val recycler = binding.recyclerVotacoes
        adapter = VotacoesCamaraAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerVotacoesCamara() {

        viewModel.gastoGeralCamara().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultVotacoesCamara.Success -> {
                        result.dado?.let { votacoes ->
                            adapter.updateData(votacoes.dados)
                            binding.progressVotacoes.visibility = View.GONE
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