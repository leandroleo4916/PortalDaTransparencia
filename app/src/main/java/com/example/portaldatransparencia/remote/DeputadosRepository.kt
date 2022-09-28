package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultRequest<T?>()
    data class Error(val exception: Exception) : ResultRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultRequest<Nothing>()
}

class SearchRepository(private val serviceApi: ApiServiceMain) {

    fun searchData(ordenarPor: String) = liveData {
        try {
            val request = serviceApi.getDeputados(ordem = "ASC", ordenarPor)
            if(request.isSuccessful){
                emit(ResultRequest.Success(dado = request.body()))
            } else {
                emit(ResultRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultRequest.Error(exception = e))
        }
    }
}