package com.example.portaldatransparencia.views.activity.ranking.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.RankingCamara
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara
import com.example.portaldatransparencia.repository.ResultRankingGeralCamara

class RankingViewModelCamara(private val repository: GastoGeralRepository): ViewModel() {

    fun rankingCamara(ano: String):
            LiveData<ResultRankingGeralCamara<RankingCamara?>> = repository.rankingGeralCamara(ano)

}

