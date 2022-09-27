package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultCargosRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultCargosRequest<T?>()
    data class Error(val exception: Exception) : ResultCargosRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultCargosRequest<Nothing>()
}

class GeralSenadorRepository(private val serviceCargos: ApiServiceSenadorCargos) {

    fun cargosDataSenador() = liveData {
        try {
            val request = serviceCargos.getCargos()
            if(request.isSuccessful){
                emit(ResultCargosRequest.Success(dado = request.body()))
            } else {
                emit(ResultCargosRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultCargosRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultCargosRequest.Error(exception = e))
        }
    }
}