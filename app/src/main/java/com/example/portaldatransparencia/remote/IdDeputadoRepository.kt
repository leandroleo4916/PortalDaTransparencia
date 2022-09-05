package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultIdRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultIdRequest<T?>()
    data class Error(val exception: Exception) : ResultIdRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultIdRequest<Nothing>()
}

class IdDeputadoRepository(private val serviceApi: ApiServiceIdDeputado) {

    fun searchIdData(id: String) = liveData {
        try {
            val request = serviceApi.getIdDeputado(id)
            if(request.isSuccessful){
                emit(ResultIdRequest.Success(dado = request.body()))
            } else {
                emit(ResultIdRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultIdRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultIdRequest.Error(exception = e))
        }
    }
}