package com.example.portaldatransparencia.views.activity.ranking.senado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.remote.GastoGeralRepository
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara
import com.example.portaldatransparencia.remote.ResultGastoGeralSenado

class RankingViewModelSenado(private val repository: GastoGeralRepository): ViewModel() {

    fun rankingSenado():
            LiveData<ResultGastoGeralSenado<GastoGeralDataClass?>> = repository.gastoGeralSenado()

}

