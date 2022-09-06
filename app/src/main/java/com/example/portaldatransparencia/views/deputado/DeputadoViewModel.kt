package com.example.portaldatransparencia.views.deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.remote.IdDeputadoRepository
import com.example.portaldatransparencia.remote.ResultIdRequest

class DeputadoViewModel(private val repository: IdDeputadoRepository): ViewModel() {

    fun searchDataDeputado(id: String):
            LiveData<ResultIdRequest<IdDeputadoDataClass?>> = repository.searchIdData(id)

}

