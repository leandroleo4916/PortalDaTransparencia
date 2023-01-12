package com.example.portaldatransparencia.views.camara.deputado

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityDeputadoBinding
import com.example.portaldatransparencia.dataclass.IdDeputadoDataClass
import com.example.portaldatransparencia.remote.ApiServiceIdDeputado
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.TabViewAdapterDeputado
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeputadoActivity: AppCompatActivity() {

    private val binding by lazy { ActivityDeputadoBinding.inflate(layoutInflater) }
    private val mainViewModel: DeputadoViewModel by viewModel()
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPagerDeputado.isUserInputEnabled = false
        id = intent.extras?.getString("id").toString()
        securityPreferences.putString("id", id)
        setupViewGeral()
        observer()
        listener()
    }

    override fun onBackPressed() = finish()

    private fun setupViewGeral(){
        val tabs = arrayOf(R.string.geral, R.string.gastos, R.string.acao, R.string.frente)
        val icons = arrayOf(
            R.drawable.ic_avatar,
            R.drawable.ic_money,
            R.drawable.ic_project,
            R.drawable.ic_front
        )
        val tabLayout = binding.tabDeputado
        val pagerGeral = binding.viewPagerDeputado
        val adapter = TabViewAdapterDeputado(this)
        pagerGeral.adapter = adapter

        TabLayoutMediator(tabLayout, pagerGeral){ tab, position ->
            tab.text = getString(tabs[position])
            tab.icon = getDrawable(icons[position])
        }.attach()
    }

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServiceIdDeputado::class.java)
        val call: Call<IdDeputadoDataClass> = retrofit.getIdDeputado(id)
        call.enqueue(object: Callback<IdDeputadoDataClass> {
            override fun onResponse(call: Call<IdDeputadoDataClass>, res: Response<IdDeputadoDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            addElementView(res.body()!!)
                        }
                        else {

                        }
                    }
                    429 -> observer()
                    else -> {

                    }
                }
            }

            override fun onFailure(call: Call<IdDeputadoDataClass>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addElementView(item: IdDeputadoDataClass) {
        binding.run {
            Glide.with(application)
                .load(item.dados.ultimoStatus.urlFoto)
                .circleCrop()
                .into(imageDeputado)
            textDescription.text = item.dados.ultimoStatus.nome
            statusView.run {
                disableView(progressDeputado)
                enableView(textDescription)
                enableView(imageDeputado)
            }
        }
    }

    private fun listener(){
        binding.imageBack.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click))
            finish()
        }
    }
}