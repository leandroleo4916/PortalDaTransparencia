package com.example.portaldatransparencia.views.senador.votacoes_senador

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.DataClassVotacoesSenador
import com.example.portaldatransparencia.dataclass.PropostaDataClass
import com.example.portaldatransparencia.remote.*

class VotacoesViewModel(private val repository: VotacoesRepository): ViewModel() {

    fun votacoes(id: String, year: String):
            LiveData<ResultVotacoesRequest<DataClassVotacoesSenador?>> = repository.votacoesData(id, year)

}

