package com.example.portaldatransparencia.views.senador.geral_senador

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.CargoSenadorDataClass
import com.example.portaldatransparencia.remote.*

class GeralSenadorViewModel(private val repository: GeralSenadorRepository): ViewModel() {

    fun cargosSenador(id: String):
            LiveData<ResultCargosRequest<CargoSenadorDataClass?>> = repository.cargosDataSenador(id)

}

