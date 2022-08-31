package com.example.portaldatransparencia.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.remote.IdDeputadoRepository
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.remote.SearchRepository

class DeputadoViewModel(private val repository: IdDeputadoRepository): ViewModel() {

    fun searchDataDeputado(id: String):
            LiveData<ResultRequest<IdDeputadoDataClass?>> = repository.searchIdData(id)

}

