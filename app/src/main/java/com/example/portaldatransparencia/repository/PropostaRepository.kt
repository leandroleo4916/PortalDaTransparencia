package com.example.portaldatransparencia.repository

import com.example.portaldatransparencia.network.ApiServiceProposta

sealed class ResultPropostaRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultPropostaRequest<T?>()
    data class Error(val exception: Exception) : ResultPropostaRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultPropostaRequest<Nothing>()
}

class PropostaRepository(private val serviceApi: ApiServiceProposta) {

    /*fun propostaData(year: String, id: String, page: Int) = liveData {
        try {
            val request = serviceApi.getProposta(year, id, pagina = page)
            if(request.isSuccessful){
                emit(ResultPropostaRequest.Success(dado = request.body()))
            } else {
                emit(ResultPropostaRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultPropostaRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultPropostaRequest.Error(exception = e))
        }
    }*/
}