package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultDespesasRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultDespesasRequest<T?>()
    data class Error(val exception: Exception) : ResultDespesasRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultDespesasRequest<Nothing>()
}

class IdDespesasRepository(private val serviceApi: ApiServiceIdDespesas) {

    fun searchDespesasData(id: String, ano: String) = liveData {
        try {
            val request = serviceApi.getIdDespesas(id, ano)
            if(request.isSuccessful){
                emit(ResultDespesasRequest.Success(dado = request.body()))
            } else {
                emit(ResultDespesasRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultDespesasRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultDespesasRequest.Error(exception = e))
        }
    }
}