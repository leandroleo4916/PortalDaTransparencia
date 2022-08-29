package com.example.portaldatransparencia.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.remote.SearchRepository

class MainViewModel(private val repository: SearchRepository): ViewModel() {

    fun searchDataWeather(ordenarPor: String):
            LiveData<ResultRequest<MainDataClass?>> = repository.searchData(ordenarPor)

}

