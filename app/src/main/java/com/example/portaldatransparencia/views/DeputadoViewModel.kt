package com.example.portaldatransparencia.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.remote.SearchRepository

class DeputadoViewModel(private val repository: SearchRepository): ViewModel() {

    fun searchDataDeputado(ordenarPor: String):
            LiveData<ResultRequest<MainDataClass?>> = repository.searchData(ordenarPor)

}

