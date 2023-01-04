package com.example.portaldatransparencia.views.activity.votacoes

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.VotacoesCamaraAdapter
import com.example.portaldatransparencia.databinding.ActivityVotacoesBinding
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.dataclass.VotacoesList
import com.example.portaldatransparencia.remote.ApiServiceMain
import com.example.portaldatransparencia.remote.ApiVotacoes
import com.example.portaldatransparencia.remote.ResultVotacoesCamara
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityVotacoesCamara: AppCompatActivity() {

    private val binding by lazy { ActivityVotacoesBinding.inflate(layoutInflater) }
    private val viewModel: VotacoesViewModelCamara by viewModel()
    private lateinit var adapter: VotacoesCamaraAdapter
    private val statusView: EnableDisableView by inject()
    private var votacoesSize = 0
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        observerVotacoesCamara()
        modifyTitle()
        listener()
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    private fun modifyTitle() {
        binding.layoutTop.run {
            textViewTitleTop.text = "Votacões - Deputados"
            textViewDescriptionTop.text = "Todas as votações registradas"
        }
    }

    private fun recycler() {
        val recycler = binding.recyclerVotacoes
        adapter = VotacoesCamaraAdapter(baseContext)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerVotacoesCamara() {

        val retrofit = Retrofit.createService(ApiVotacoes::class.java)
        val call: Call<VotacoesList> = retrofit
            .getVotacoes("DESC", "dataHoraRegistro", page.toString(), "200")
        call.enqueue(object: Callback<VotacoesList> {
            override fun onResponse(call: Call<VotacoesList>, response: Response<VotacoesList>) {
                when (response.code()){
                    200 ->
                        if (response.body() != null && response.body()!!.dados.isNotEmpty()){
                            adapter.updateData(response.body()!!.dados)
                            votacoesSize += response.body()!!.dados.size
                            page += 1
                            if (response.body()!!.dados.size == 200) observerVotacoesCamara()

                            binding.run {
                                statusView.run {
                                    disableView(progressVotacoes)
                                    enableView(iconVotacoes)
                                    enableView(textNumberVotacoes)
                                }
                                textNumberVotacoes.text = votacoesSize.toString()+" Votações"
                            }
                        }
                    429 -> observerVotacoesCamara()
                    else -> {}
                }
            }

            override fun onFailure(call: Call<VotacoesList>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}