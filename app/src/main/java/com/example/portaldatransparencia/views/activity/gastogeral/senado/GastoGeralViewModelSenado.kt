package com.example.portaldatransparencia.views.activity.gastogeral.senado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralSenado

class GastoGeralViewModelSenado(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeralSenado():
            LiveData<ResultGastoGeralSenado<GastoGeralDataClass?>> = repository.gastoGeralSenado()

}

