package com.example.portaldatransparencia.views.deputado.gastos_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.Despesas
import com.example.portaldatransparencia.dataclass.SenadorGastosDataClass
import com.example.portaldatransparencia.remote.IdDespesasRepository
import com.example.portaldatransparencia.remote.ResultCotaRequest
import com.example.portaldatransparencia.remote.ResultDespesasRequest

class DespesasViewModel(private val repository: IdDespesasRepository): ViewModel() {

    fun searchDespesasDeputado(id: String, ano: String, pagina: Int):
            LiveData<ResultDespesasRequest<Despesas?>> = repository.searchDespesasData(id, ano, pagina)

    fun searchGastosSenador(ano: String, nome: String):
            LiveData<ResultCotaRequest<SenadorGastosDataClass?>> = repository.gastosData(ano, nome)

}

