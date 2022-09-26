package com.example.portaldatransparencia.views.senado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.SenadoresDataClass
import com.example.portaldatransparencia.remote.IdDeputadoRepository
import com.example.portaldatransparencia.remote.ResultIdRequest
import com.example.portaldatransparencia.remote.ResultSenadoRequest
import com.example.portaldatransparencia.remote.SenadoRepository

class SenadoViewModel(private val repositorySenado: SenadoRepository): ViewModel() {

    fun searchDataSenado():
            LiveData<ResultSenadoRequest<SenadoresDataClass?>> = repositorySenado.senadoData()

}

