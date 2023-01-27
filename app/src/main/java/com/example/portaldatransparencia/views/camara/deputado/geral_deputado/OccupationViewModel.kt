package com.example.portaldatransparencia.views.camara.deputado.geral_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Occupation
import com.example.portaldatransparencia.dataclass.OccupationDataClass
import com.example.portaldatransparencia.network.ApiServiceOccupation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OccupationViewModel(private val service: ApiServiceOccupation): ViewModel() {

    private val response = MutableLiveData<List<Occupation>>()
    val responseLiveData: LiveData<List<Occupation>> = response

    private val responseError = MutableLiveData<Int>()
    val responseErrorLiveData: LiveData<Int> = responseError

    fun getOccupation(id: String){
        service.getOccupation(id).enqueue(object: Callback<OccupationDataClass> {
            override fun onResponse(call: Call<OccupationDataClass>, res: Response<OccupationDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            response.value = res.body()!!.dados
                        }
                    }
                    429 -> getOccupation(id)
                    else -> responseError.value = R.string.api_nao_respondeu
                }
            }
            override fun onFailure(call: Call<OccupationDataClass>, t: Throwable) {
                responseError.value = R.string.api_nao_respondeu
            }
        })
    }
}

