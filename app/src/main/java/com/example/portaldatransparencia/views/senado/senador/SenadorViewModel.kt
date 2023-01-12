package com.example.portaldatransparencia.views.senado.senador

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.SenadorDataClass
import com.example.portaldatransparencia.remote.*

class SenadorViewModel(private val repositorySenador: SenadorRepository): ViewModel() {

    fun searchDataSenador(id: String):
            LiveData<ResultSenadorRequest<SenadorDataClass?>> = repositorySenador.senadorData(id)

}

