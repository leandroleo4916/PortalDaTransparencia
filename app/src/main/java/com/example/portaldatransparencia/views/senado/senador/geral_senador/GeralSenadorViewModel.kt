package com.example.portaldatransparencia.views.senado.senador.geral_senador

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.CargoSenadorDataClass
import com.example.portaldatransparencia.repository.*

class GeralSenadorViewModel(private val repository: GeralSenadorRepository): ViewModel() {

    fun cargosSenador(id: String):
            LiveData<ResultCargosRequest<CargoSenadorDataClass?>> = repository.cargosDataSenador(id)

}

