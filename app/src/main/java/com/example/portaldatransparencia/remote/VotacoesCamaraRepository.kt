package com.example.portaldatransparencia.remote

import androidx.lifecycle.liveData
import java.net.ConnectException

sealed class ResultVotacoesCamara<out R> {
    data class Success<out T>(val dado: T?) : ResultVotacoesCamara<T?>()
    data class Error(val exception: Exception) : ResultVotacoesCamara<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultVotacoesCamara<Nothing>()
}

class VotacoesCamaraRepository(private val serviceApi: ApiVotacoes) {

    fun votacoesData(page: Int) = liveData {
        try {
            val request = serviceApi.getVotacoes("DESC", "dataHoraRegistro", page.toString(), "200")
            if(request.isSuccessful){
                emit(ResultVotacoesCamara.Success(dado = request.body()))
            } else {
                emit(ResultVotacoesCamara.Error(exception = Exception("Não foi possível conectar!")))
            }
        }
        catch (e: ConnectException) {
            emit(ResultVotacoesCamara.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultVotacoesCamara.Error(exception = e))
        }
    }
}