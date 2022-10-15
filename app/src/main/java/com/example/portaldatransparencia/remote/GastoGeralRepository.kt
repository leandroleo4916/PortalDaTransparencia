package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultGastoGeralRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultGastoGeralRequest<T?>()
    data class Error(val exception: Exception) : ResultGastoGeralRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultGastoGeralRequest<Nothing>()
}

sealed class ResultGastoGeralCamara<out R> {
    data class Success<out T>(val dado: T?) : ResultGastoGeralCamara<T?>()
    data class Error(val exception: Exception) : ResultGastoGeralCamara<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultGastoGeralCamara<Nothing>()
}

class GastoGeralRepository(private val serviceApi: ApiServiceGastoGeralSenador,
                           private val serviceApiCamara: ApiServiceGastoGeralDeputado) {

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

    fun gastoGeralCamara() = liveData {
        try {
            val request = serviceApiCamara.getGastoGeral()
            if(request.isSuccessful){
                emit(ResultGastoGeralCamara.Success(dado = request.body()))
            } else {
                emit(ResultGastoGeralCamara.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultGastoGeralCamara.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultGastoGeralCamara.Error(exception = e))
        }
    }
}