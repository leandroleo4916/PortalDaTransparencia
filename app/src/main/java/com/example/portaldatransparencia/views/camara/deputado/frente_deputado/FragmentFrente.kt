package com.example.portaldatransparencia.views.camara.deputado.frente_deputado

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.FrenteAdapter
import com.example.portaldatransparencia.databinding.FragmentFrenteBinding
import com.example.portaldatransparencia.dataclass.DadoFrente
import com.example.portaldatransparencia.dataclass.Frente
import com.example.portaldatransparencia.interfaces.IFront
import com.example.portaldatransparencia.remote.ApiServiceFrente
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFrente: Fragment(R.layout.fragment_frente), IFront {

    private var binding: FragmentFrenteBinding? = null
    private val viewModel: FrenteViewModel by viewModel()
    private lateinit var adapter: FrenteAdapter
    private val statusView: EnableDisableView by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFrenteBinding.bind(view)
        id = securityPreferences.getString("id")
        recyclerView()
        observer()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerFrente
        adapter = context?.let { FrenteAdapter(this, it) }!!
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServiceFrente::class.java)
        val call: Call<Frente> = retrofit.getFrente(id)
        call.enqueue(object: Callback<Frente> {
            override fun onResponse(call: Call<Frente>, res: Response<Frente>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null){
                            val size = res.body()!!.dados.size
                            calculateFront(size.toString())
                            adapter.updateData(res.body()!!.dados)
                        }
                        else {
                            binding?.run {
                                statusView.disableView(progressFront)
                                statusView.enableView(textFrenteParlamentar)
                                textFrenteParlamentar.text = getString(R.string.nenhuma_frente_parlamentar)
                            }
                        }
                    }
                    429 -> observer()
                    else -> {
                        binding?.run {
                            statusView.disableView(progressFront)
                            statusView.enableView(textFrenteParlamentar)
                            textFrenteParlamentar.text = getString(R.string.nenhuma_frente_parlamentar)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Frente>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun calculateFront(front: String){
        binding?.run {
            statusView.disableView(progressFront)
            statusView.enableView(textFrenteParlamentar)
            "$front frentes parlamentares".also { textFrenteParlamentar.text = it }
        }
    }

    override fun listenerFront(note: DadoFrente) {
        val intent = Intent(context, FragmentFrenteId::class.java)
        intent.putExtra("id", note.id.toString())
        startActivity(intent)
    }

}