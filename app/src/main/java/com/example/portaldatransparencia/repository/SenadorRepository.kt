package com.example.portaldatransparencia.repository

import androidx.lifecycle.liveData
import com.example.portaldatransparencia.network.ApiServiceSenador
import java.net.ConnectException

sealed class ResultSenadorRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultSenadorRequest<T?>()
    data class Error(val exception: Exception) : ResultSenadorRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultSenadorRequest<Nothing>()
}

class SenadorRepository(private val serviceApi: ApiServiceSenador) {

    fun senadorData(id: String) = liveData {
        try {
            val request = serviceApi.getSenador(id)
            if(request.isSuccessful){
                emit(ResultSenadorRequest.Success(dado = request.body()))
            } else {
                emit(ResultSenadorRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultSenadorRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultSenadorRequest.Error(exception = e))
        }
    }
}