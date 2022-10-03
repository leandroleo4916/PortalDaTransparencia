package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultVotacoesRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultVotacoesRequest<T?>()
    data class Error(val exception: Exception) : ResultVotacoesRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultVotacoesRequest<Nothing>()
}

class VotacoesRepository(private val serviceApi: ApiServiceVotacoes) {

    fun votacoesData(id: String, year: String) = liveData {
        try {
            val request = serviceApi.getVotacoes(id, year)
            if(request.isSuccessful){
                emit(ResultVotacoesRequest.Success(dado = request.body()))
            } else {
                emit(ResultVotacoesRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultVotacoesRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultVotacoesRequest.Error(exception = e))
        }
    }
}