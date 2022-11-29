package com.example.portaldatransparencia.views.activity.votacoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.VotacoesList
import com.example.portaldatransparencia.remote.GastoGeralRepository
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara
import com.example.portaldatransparencia.remote.ResultVotacoesCamara
import com.example.portaldatransparencia.remote.VotacoesCamaraRepository

class VotacoesViewModelCamara(private val repository: VotacoesCamaraRepository): ViewModel() {

    fun gastoGeralCamara(page: Int):
            LiveData<ResultVotacoesCamara<VotacoesList?>> = repository.votacoesData(page)

}

