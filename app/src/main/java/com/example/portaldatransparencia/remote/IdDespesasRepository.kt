package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultDespesasRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultDespesasRequest<T?>()
    data class Error(val exception: Exception) : ResultDespesasRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultDespesasRequest<Nothing>()
}

sealed class ResultCotaRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultCotaRequest<T?>()
    data class Error(val exception: Exception) : ResultCotaRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultCotaRequest<Nothing>()
}

class IdDespesasRepository(private val serviceApi: ApiServiceIdDespesas,
                           private val service: ApiServiceGastos) {

    fun searchDespesasData(id: String, ano: String, pagina: Int) = liveData {
        try {
            val request = serviceApi.getIdDespesas(id, ano, pagina = pagina)
            if(request.isSuccessful){
                emit(ResultDespesasRequest.Success(dado = request.body()))
            } else {
                emit(ResultDespesasRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultDespesasRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultDespesasRequest.Error(exception = e))
        }
    }

    fun gastosData(year: String, nome: String) = liveData {
        try {
            val request = service.getGastos(year, nome)
            if(request.isSuccessful){
                emit(ResultCotaRequest.Success(dado = request.body()))
            } else {
                emit(ResultCotaRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultCotaRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultCotaRequest.Error(exception = e))
        }
    }
}