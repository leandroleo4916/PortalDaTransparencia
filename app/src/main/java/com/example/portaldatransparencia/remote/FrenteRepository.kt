package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultFrenteRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultFrenteRequest<T?>()
    data class Error(val exception: Exception) : ResultFrenteRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultFrenteRequest<Nothing>()
}

class FrenteRepository(private val serviceApi: ApiServiceFrente) {

    fun frenteData(id: String) = liveData {
        try {
            val request = serviceApi.getFrente(id)
            if(request.isSuccessful){
                emit(ResultFrenteRequest.Success(dado = request.body()))
            } else {
                emit(ResultFrenteRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultFrenteRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultFrenteRequest.Error(exception = e))
        }
    }
}