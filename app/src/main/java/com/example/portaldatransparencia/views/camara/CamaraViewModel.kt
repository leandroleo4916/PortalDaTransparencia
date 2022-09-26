package com.example.portaldatransparencia.views.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.remote.SearchRepository

class CamaraViewModel(private val repository: SearchRepository): ViewModel() {

    fun searchData(ordenarPor: String):
            LiveData<ResultRequest<MainDataClass?>> = repository.searchData(ordenarPor)

}

