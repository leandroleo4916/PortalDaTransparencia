package com.example.portaldatransparencia.views.deputado.frente_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.Frente
import com.example.portaldatransparencia.remote.*

class FrenteViewModel(private val repository: FrenteRepository): ViewModel() {

    fun frenteDeputado(id: String):
            LiveData<ResultFrenteRequest<Frente?>> = repository.frenteData(id)

}

