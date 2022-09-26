package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultSenadoRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultSenadoRequest<T?>()
    data class Error(val exception: Exception) : ResultSenadoRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultSenadoRequest<Nothing>()
}

class SenadoRepository(private val serviceApi: ApiServiceSenado) {

    fun senadoData() = liveData {
        try {
            val request = serviceApi.getSenado()
            if(request.isSuccessful){
                emit(ResultSenadoRequest.Success(dado = request.body()))
            } else {
                emit(ResultSenadoRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultSenadoRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultSenadoRequest.Error(exception = e))
        }
    }
}