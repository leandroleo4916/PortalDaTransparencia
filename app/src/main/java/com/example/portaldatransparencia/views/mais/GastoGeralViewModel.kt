package com.example.portaldatransparencia.views.mais

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.remote.GastoGeralRepository
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara
import com.example.portaldatransparencia.remote.ResultGastoGeralRequest

class GastoGeralViewModel(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeral():
            LiveData<ResultGastoGeralRequest<GastoGeralDataClass?>> = repository.gastoGeralData()

    fun gastoGeralCamara():
            LiveData<ResultGastoGeralCamara<GastoGeralCamara?>> = repository.gastoGeralCamara()

}

