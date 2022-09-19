package com.example.portaldatransparencia.views.geral

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.Despesas
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.OccupationDataClass
import com.example.portaldatransparencia.remote.*

class OccupationViewModel(private val repository: OccupationRepository): ViewModel() {

    fun occupationDeputado(id: String):
            LiveData<ResultOccupationRequest<OccupationDataClass?>> = repository.occupationData(id)

}

