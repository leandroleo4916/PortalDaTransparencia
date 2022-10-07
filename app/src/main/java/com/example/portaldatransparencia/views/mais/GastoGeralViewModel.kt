package com.example.portaldatransparencia.views.mais

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.dataclass.OccupationDataClass
import com.example.portaldatransparencia.remote.*

class GastoGeralViewModel(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeral():
            LiveData<ResultGastoGeralRequest<GastoGeralDataClass?>> = repository.gastoGeralData()

}

