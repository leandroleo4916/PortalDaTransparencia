package com.example.portaldatransparencia.views.senado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.SenadoresDataClass
import com.example.portaldatransparencia.repository.ResultSenadoRequest
import com.example.portaldatransparencia.repository.SenadoRepository

class SenadoViewModel(private val repositorySenado: SenadoRepository): ViewModel() {

    fun searchDataSenado():
            LiveData<ResultSenadoRequest<SenadoresDataClass?>> = repositorySenado.senadoData()

}

