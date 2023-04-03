package com.example.portaldatransparencia.repository

import androidx.lifecycle.liveData
import com.example.portaldatransparencia.network.ApiServicePropostaItemSuspend
import java.net.ConnectException

sealed class ResultIdPropostaRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultIdPropostaRequest<T?>()
    data class Error(val exception: Exception) : ResultIdPropostaRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultIdPropostaRequest<Nothing>()
}

class PropostaIdRepository(private val serviceApi: ApiServicePropostaItemSuspend) {

    fun propostaData(id: String) = liveData {
        try {
            val request = serviceApi.getPropostaItem(id)
            if(request.isSuccessful){
                emit(ResultIdPropostaRequest.Success(dado = request.body()))
            } else {
                emit(ResultIdPropostaRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultIdPropostaRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultIdPropostaRequest.Error(exception = e))
        }
    }
}