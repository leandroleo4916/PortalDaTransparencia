package com.example.portaldatransparencia.views.activity.gastogeral.senado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.dataclass.GastoGeralSenadoData
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralSenado

class GastoGeralViewModelSenado(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeralSenado(ano: String):
            LiveData<ResultGastoGeralSenado<GastoGeralSenadoData?>> = repository.gastoGeralSenado(ano)

}

