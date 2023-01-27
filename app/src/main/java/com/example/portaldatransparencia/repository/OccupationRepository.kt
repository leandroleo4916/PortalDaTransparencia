package com.example.portaldatransparencia.repository

import com.example.portaldatransparencia.network.ApiServiceOccupation

sealed class ResultOccupationRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultOccupationRequest<T?>()
    data class Error(val exception: Exception) : ResultOccupationRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultOccupationRequest<Nothing>()
}

class OccupationRepository(private val serviceApi: ApiServiceOccupation) {

    /*fun occupationData(id: String) = liveData {
        try {
            val request = serviceApi.getOccupation(id)
            if(request.isSuccessful){
                emit(ResultOccupationRequest.Success(dado = request.body()))
            } else {
                emit(ResultOccupationRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultOccupationRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultOccupationRequest.Error(exception = e))
        }
    }*/
}