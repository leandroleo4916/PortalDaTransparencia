package com.example.portaldatransparencia.views.gastos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.dataclass.Despesas
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.remote.IdDeputadoRepository
import com.example.portaldatransparencia.remote.IdDespesasRepository
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import com.example.portaldatransparencia.remote.ResultIdRequest

class DespesasViewModel(private val repository: IdDespesasRepository): ViewModel() {

    fun searchDespesasDeputado(id: String, ano: String, pagina: Int):
            LiveData<ResultDespesasRequest<Despesas?>> = repository.searchDespesasData(id, ano, pagina)

}

