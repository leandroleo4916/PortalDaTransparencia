package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultGastoGeralRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultGastoGeralRequest<T?>()
    data class Error(val exception: Exception) : ResultGastoGeralRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultGastoGeralRequest<Nothing>()
}

class GastoGeralRepository(private val serviceApi: ApiServiceGastoGeral) {

    fun gastoGeralData() = liveData {
        try {
            val request = serviceApi.getGastoGeral()
            if(request.isSuccessful){
                emit(ResultGastoGeralRequest.Success(dado = request.body()))
            } else {
                emit(ResultGastoGeralRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultGastoGeralRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultGastoGeralRequest.Error(exception = e))
        }
    }
}