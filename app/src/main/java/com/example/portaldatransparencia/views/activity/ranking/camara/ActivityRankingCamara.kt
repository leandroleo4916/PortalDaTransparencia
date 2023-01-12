package com.example.portaldatransparencia.views.activity.ranking.camara

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoGeralAdapter
import com.example.portaldatransparencia.databinding.LayoutRankingBinding
import com.example.portaldatransparencia.dataclass.Dado
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.ListParlamentar
import com.example.portaldatransparencia.dataclass.MainDataClass
import com.example.portaldatransparencia.interfaces.IClickOpenDeputadoRanking
import com.example.portaldatransparencia.remote.ApiServiceMain
import com.example.portaldatransparencia.remote.ResultGastoGeralCamara
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.camara.CamaraViewModel
import com.example.portaldatransparencia.views.camara.deputado.DeputadoActivity
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRankingCamara: AppCompatActivity(), IClickOpenDeputadoRanking {

    private val binding by lazy { LayoutRankingBinding.inflate(layoutInflater) }
    private val viewModel: RankingViewModelCamara by viewModel()
    private val camaraViewModel: CamaraViewModel by viewModel()
    private val validationInternet: ValidationInternet by inject()
    private val hideView: EnableDisableView by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private lateinit var adapter: GastoGeralAdapter
    private lateinit var listDeputados: ArrayList<Dado>
    private lateinit var listGastoGeralDeputado: ArrayList<ListParlamentar>
    private var listAdpterDeputado: ArrayList<ListParlamentar> = arrayListOf()
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var sizeDeputado: String
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = securityPreferences.getString("id")
        recycler()
        observerCamara()
        listenerBack()
    }

    private fun listenerBack() {
        binding.layoutTop.run {
            hideView.disableView(imageViewFilter)
            hideView.enableView(textViewDescriptionTop)
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    private fun recycler(){
        val recycler = binding.recyclerRancking
        adapter = GastoGeralAdapter(formatValor, baseContext, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerCamara(){

        val internet = this.let { validationInternet.validationInternet(it) }
        val retrofit = Retrofit.createService(ApiServiceMain::class.java)
        val call: Call<MainDataClass> = retrofit.getDeputados(ordem = "ASC", "nome")
        if (internet){
            call.enqueue(object: Callback<MainDataClass> {
                override fun onResponse(call: Call<MainDataClass>, res: Response<MainDataClass>) {
                    when (res.code()) {
                        200 -> {
                            if (res.body()!!.dados.isNotEmpty()){
                                val list = res.body()!!.dados
                                sizeDeputado = list.size.toString()
                                listDeputados = list as ArrayList
                                observerGastoCamara()
                            }
                        }
                        429 -> observerCamara()
                    }
                }
                override fun onFailure(call: Call<MainDataClass>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        else {
            //showValidationInternet(R.string.verifique_sua_internet)
        }
    }

    private fun observerGastoCamara() {

        viewModel.rankingCamara().observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            listGastoGeralDeputado = gastos.gastoGeral.listDeputado as ArrayList
                            gastoCamara = gastos
                            processListCamara()
                        }
                    }
                    is ResultGastoGeralCamara.Error -> {
                        result.exception.message?.let { }
                    }
                    is ResultGastoGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun processListCamara(){

        listDeputados.forEach { deputado ->
            val id = deputado.id
            val partido = deputado.siglaPartido
            val uf = deputado.siglaUf
            val nome = deputado.nome
            val urlFoto = deputado.urlFoto
            listGastoGeralDeputado.forEach{
                val n = it.nome
                if (n == nome){
                    it.id = id.toString()
                    it.partido = partido
                    it.uf = uf
                    it.urlFoto = urlFoto
                }
            }
        }
        listGastoGeralDeputado.forEach {
            val item = it
            if (item.urlFoto != null) listAdpterDeputado.add(item)
        }
        binding.run {
            progressRancking.let { hideView.disableView(it) }
            textResultRacking.let { hideView.disableView(it) }
        }
        adapter.updateData(listAdpterDeputado)
    }

    override fun clickRanking(id: String) {
        val intent = Intent(this, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}