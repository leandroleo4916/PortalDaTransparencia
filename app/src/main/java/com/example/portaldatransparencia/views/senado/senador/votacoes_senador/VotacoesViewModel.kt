package com.example.portaldatransparencia.views.senado.senador.votacoes_senador

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.DataClassVotacoesItem
import com.example.portaldatransparencia.dataclass.DataClassVotacoesSenador
import com.example.portaldatransparencia.repository.*

class VotacoesViewModel(private val repository: VotacoesRepository,
                        private val repositoryItem: VotacoesRepositoryItem): ViewModel() {

    fun votacoes(id: String, year: String):
            LiveData<ResultVotacoesRequest<DataClassVotacoesSenador?>> = repository.votacoesData(id, year)

    fun votacoesItem(id: String, year: String):
            LiveData<ResultVotacoesItemRequest<DataClassVotacoesItem?>> =
        repositoryItem.votacoesItem(id, year)

}

