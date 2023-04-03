package com.example.portaldatransparencia.views.activity.votacoes.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.ProposicaoDataClass
import com.example.portaldatransparencia.repository.PropostaIdRepository
import com.example.portaldatransparencia.repository.ResultIdPropostaRequest

class VotacoesViewModelCamara(private val repository: PropostaIdRepository): ViewModel() {

    fun getProposta(id: String):
            LiveData<ResultIdPropostaRequest<ProposicaoDataClass?>> = repository.propostaData(id)

}

