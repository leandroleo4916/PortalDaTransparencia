package com.example.portaldatransparencia.views.camara.deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Dados
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.dataclass.Occupation
import com.example.portaldatransparencia.network.ApiServiceIdDeputado
import com.example.portaldatransparencia.repository.IdDeputadoRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeputadoViewModel(private val service: ApiServiceIdDeputado): ViewModel() {

    private val response = MutableLiveData<Dados>()
    val responseLiveData: LiveData<Dados> = response

    private val responseError = MutableLiveData<Int>()
    val responseErrorLiveData: LiveData<Int> = responseError

    fun searchDataDeputado(id: String){
        service.getIdDeputado(id).enqueue(object: Callback<IdDeputadoDataClass> {
            override fun onResponse(call: Call<IdDeputadoDataClass>, res: Response<IdDeputadoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            response.value = res.body()!!.dados
                        }
                        else responseError.value = R.string.sem_info_api
                    }
                    429 -> searchDataDeputado(id)
                    else -> responseError.value = R.string.erro_api_senado
                }
            }
            override fun onFailure(call: Call<IdDeputadoDataClass>, t: Throwable) {
                responseError.value = R.string.erro_api_senado
            }
        })
    }
}

