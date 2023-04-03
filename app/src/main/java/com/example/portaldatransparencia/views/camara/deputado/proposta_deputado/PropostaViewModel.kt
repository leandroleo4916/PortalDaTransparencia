package com.example.portaldatransparencia.views.camara.deputado.proposta_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.network.ApiServiceIdDeputado
import com.example.portaldatransparencia.network.ApiServicePropostaItem
import com.example.portaldatransparencia.repository.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PropostaViewModel(private val service: ApiServicePropostaItem,
                        private val serviceDeputado: ApiServiceIdDeputado): ViewModel() {

    private val response = MutableLiveData<DadosProposicao>()
    val responseLive: LiveData<DadosProposicao> = response

    private val responseError = MutableLiveData<Int>()
    val responseErrorLiveData: LiveData<Int> = responseError

    private val responseParlamentar = MutableLiveData<IdDeputadoDataClass>()
    val responseLiveParlamentar: LiveData<IdDeputadoDataClass> = responseParlamentar

    private val responseErrorParlamentar = MutableLiveData<Int>()
    val responseErrorLiveDataParlamentar: LiveData<Int> = responseErrorParlamentar

    fun propostaIdDeputado(id: String){

        service.getPropostaItem(id).enqueue(object: Callback<ProposicaoDataClass> {
            override fun onResponse(call: Call<ProposicaoDataClass>, res: Response<ProposicaoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null) {
                            response.value = res.body()!!.dados
                        }
                        else responseError.value = R.string.api_nao_respondeu
                    }
                    429 -> propostaIdDeputado(id)
                    else -> responseError.value = R.string.api_nao_respondeu
                }
            }
            override fun onFailure(call: Call<ProposicaoDataClass>, t: Throwable) {
                responseError.value = R.string.api_nao_respondeu
            }
        })
    }

    fun getParlamentar(idParlamentar: String){
        serviceDeputado.getIdDeputado(idParlamentar).enqueue(object: Callback<IdDeputadoDataClass> {
            override fun onResponse(call: Call<IdDeputadoDataClass>, res: Response<IdDeputadoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            responseParlamentar.value = res.body()
                        }
                    }
                    429 -> getParlamentar(idParlamentar)
                    else -> {

                    }
                }
            }
            override fun onFailure(call: Call<IdDeputadoDataClass>, t: Throwable) {}
        })
    }
}

