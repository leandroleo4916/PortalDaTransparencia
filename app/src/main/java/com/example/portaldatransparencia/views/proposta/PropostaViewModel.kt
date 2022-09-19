package com.example.portaldatransparencia.views.proposta

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.Frente
import com.example.portaldatransparencia.dataclass.Proposta
import com.example.portaldatransparencia.dataclass.PropostaDataClass
import com.example.portaldatransparencia.remote.*

class PropostaViewModel(private val repository: PropostaRepository): ViewModel() {

    fun propostaDeputado(year: String, id: String, page: Int):
            LiveData<ResultPropostaRequest<PropostaDataClass?>> = repository.propostaData(year, id, page)

}

