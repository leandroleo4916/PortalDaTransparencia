package com.example.portaldatransparencia.views.camara

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.network.ApiServiceMain
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.repository.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CamaraViewModel(private val repository: SearchRepository) : ViewModel() {

    private val response = MutableLiveData<List<Dado>>()
    val responseLiveData: MutableLiveData<List<Dado>> = response

    private val responseError = MutableLiveData<Int>()
    val responseLiveError: LiveData<Int> = responseError

    fun searchData() {
        val retrofit = Retrofit.createService(ApiServiceMain::class.java)
        val call: Call<MainDataClass> = retrofit.getDeputados(ordem = "ASC", "nome")
        call.enqueue(object : Callback<MainDataClass> {
            override fun onResponse(call: Call<MainDataClass>, res: Response<MainDataClass>) {
                when (res.code()) {
                    200 -> response.value = res.body()?.dados
                    429 -> searchData()
                    else -> responseError.value = R.string.nao_recebeu_dados
                }
            }
            override fun onFailure(call: Call<MainDataClass>, t: Throwable) {
                responseError.value = R.string.falha_no_servidor
            }
        })
    }
}

