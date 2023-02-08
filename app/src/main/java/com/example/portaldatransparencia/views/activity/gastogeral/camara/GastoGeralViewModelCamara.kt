package com.example.portaldatransparencia.views.activity.gastogeral.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara

class GastoGeralViewModelCamara(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeralCamara(ano: String):
            LiveData<ResultGastoGeralCamara<GastoGeralCamara?>> = repository.gastoGeralCamara(ano)

}

