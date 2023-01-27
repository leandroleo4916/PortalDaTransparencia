package com.example.portaldatransparencia.views.camara.deputado.frente_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Frente
import com.example.portaldatransparencia.network.ApiServiceFrente
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.repository.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FrontViewModel(private val service: ApiServiceFrente): ViewModel() {

    private val response = MutableLiveData<Frente>()
    val responseLiveData: LiveData<Frente> = response

    private val responseError = MutableLiveData<Int>()
    val responseErrorLiveData: LiveData<Int> = responseError

    fun getFront(id: String) {

        service.getFrente(id).enqueue(object: Callback<Frente> {
            override fun onResponse(call: Call<Frente>, res: Response<Frente>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null) response.value = res.body()!!
                        else responseError.value = R.string.nenhuma_frente_parlamentar
                    }
                    429 -> getFront(id)
                    else -> responseError.value = R.string.api_nao_respondeu
                }
            }
            override fun onFailure(call: Call<Frente>, t: Throwable) {
                responseError.value = R.string.api_nao_respondeu
            }
        })
    }
}

