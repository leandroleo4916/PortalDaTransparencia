package com.example.portaldatransparencia.views.camara.deputado.frente_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.FrenteId
import com.example.portaldatransparencia.network.ApiServiceFrenteId
import com.example.portaldatransparencia.network.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FrontIdViewModel(private val service: ApiServiceFrenteId): ViewModel() {

    private val response = MutableLiveData<FrenteId>()
    val responseLiveData: LiveData<FrenteId> = response

    private val responseError = MutableLiveData<Int>()
    val responseErrorLiveData: LiveData<Int> = responseError

    fun getFront(id: String) {

        service.getFrenteId(id).enqueue(object: Callback<FrenteId> {
            override fun onResponse(call: Call<FrenteId>, res: Response<FrenteId>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null) response.value = res.body()!!
                        else responseError.value = R.string.nenhuma_frente_parlamentar
                    }
                    429 -> getFront(id)
                    else -> responseError.value = R.string.api_nao_respondeu
                }
            }
            override fun onFailure(call: Call<FrenteId>, t: Throwable) {
                responseError.value = R.string.api_nao_respondeu
            }
        })
    }
}

