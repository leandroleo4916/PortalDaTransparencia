package com.example.portaldatransparencia.views.activity.gastogeral.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.remote.GastoGeralRepository
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara

class GastoGeralViewModelCamara(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeralCamara():
            LiveData<ResultGastoGeralCamara<GastoGeralCamara?>> = repository.gastoGeralCamara()

}

