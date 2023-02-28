package com.example.portaldatransparencia.repository

import androidx.lifecycle.liveData
import com.example.portaldatransparencia.network.ApiServiceGastoGeralDeputado
import com.example.portaldatransparencia.network.ApiServiceGastoGeralSenado
import com.example.portaldatransparencia.network.ApiServiceRankingSenador
import com.example.portaldatransparencia.network.ApiServiceRankingDeputado
import java.net.ConnectException

sealed class ResultGastoGeralSenado<out R> {
    data class Success<out T>(val dado: T?) : ResultGastoGeralSenado<T?>()
    data class Error(val exception: Exception) : ResultGastoGeralSenado<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultGastoGeralSenado<Nothing>()
}

sealed class ResultRankingSenado<out R> {
    data class Success<out T>(val dado: T?) : ResultRankingSenado<T?>()
    data class Error(val exception: Exception) : ResultRankingSenado<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultRankingSenado<Nothing>()
}

sealed class ResultGastoGeralCamara<out R> {
    data class Success<out T>(val dado: T?) : ResultGastoGeralCamara<T?>()
    data class Error(val exception: Exception) : ResultGastoGeralCamara<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultGastoGeralCamara<Nothing>()
}

sealed class ResultRankingGeralCamara<out R> {
    data class Success<out T>(val dado: T?) : ResultRankingGeralCamara<T?>()
    data class Error(val exception: Exception) : ResultRankingGeralCamara<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultRankingGeralCamara<Nothing>()
}

class GastoGeralRepository(private val serviceApi: ApiServiceRankingSenador,
                           private val serviceApiCamara: ApiServiceGastoGeralDeputado,
                           private val serviceApiRanking: ApiServiceRankingDeputado,
                           private val serviceApiGastoSenado: ApiServiceGastoGeralSenado ) {

    fun gastoGeralSenado(ano: String) = liveData {
        try {
            val request = serviceApiGastoSenado.gastoGeral(ano)
            if(request.isSuccessful){
                emit(ResultGastoGeralSenado.Success(dado = request.body()))
            } else {
                emit(ResultGastoGeralSenado.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultGastoGeralSenado.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultGastoGeralSenado.Error(exception = e))
        }
    }

    fun rankingGeralSenado(ano: String) = liveData {
        try {
            val request = serviceApi.rankingGeral(ano)
            if(request.isSuccessful){
                emit(ResultRankingSenado.Success(dado = request.body()))
            } else {
                emit(ResultRankingSenado.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultRankingSenado.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultRankingSenado.Error(exception = e))
        }
    }

    fun gastoGeralCamara(ano: String) = liveData {
        try {
            val request = serviceApiCamara.getGastoGeral(ano)
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

    fun rankingGeralCamara(ano: String) = liveData {
        try {
            val request = serviceApiRanking.rankingGeral(ano)
            if(request.isSuccessful){
                emit(ResultRankingGeralCamara.Success(dado = request.body()))
            } else {
                emit(ResultRankingGeralCamara.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultRankingGeralCamara.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultRankingGeralCamara.Error(exception = e))
        }
    }
}