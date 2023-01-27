package com.example.portaldatransparencia.views.activity.ranking.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara

class RankingViewModelCamara(private val repository: GastoGeralRepository): ViewModel() {

    fun rankingCamara():
            LiveData<ResultGastoGeralCamara<GastoGeralCamara?>> = repository.gastoGeralCamara()

}

