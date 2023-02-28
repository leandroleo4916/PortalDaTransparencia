package com.example.portaldatransparencia.views.activity.ranking.senado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.dataclass.RankingDataClass
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralSenado
import com.example.portaldatransparencia.repository.ResultRankingSenado

class RankingViewModelSenado(private val repository: GastoGeralRepository): ViewModel() {

    fun rankingSenado(ano: String):
            LiveData<ResultRankingSenado<RankingDataClass?>> = repository.rankingGeralSenado(ano)

}

