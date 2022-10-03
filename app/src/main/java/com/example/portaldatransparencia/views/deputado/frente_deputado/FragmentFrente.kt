package com.example.portaldatransparencia.views.deputado.frente_deputado

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.FrenteAdapter
import com.example.portaldatransparencia.databinding.FragmentFrenteBinding
import com.example.portaldatransparencia.dataclass.DadoFrente
import com.example.portaldatransparencia.interfaces.IFront
import com.example.portaldatransparencia.remote.ResultFrenteRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        adapter = FrenteAdapter(this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer() {

        viewModel.frenteDeputado(id).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultFrenteRequest.Success -> {
                        result.dado?.let { front ->
                            if (front.dados.isNotEmpty()){
                                val size = front.dados.size
                                calculateFront(size.toString())
                                adapter.updateData(front.dados)
                            }else{
                                statusView.disableView(binding!!.progressFront)
                            }
                        }
                    }
                    is ResultFrenteRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultFrenteRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun calculateFront(front: String){
        binding?.run {
            statusView.disableView(progressFront)
            statusView.enableView(textFrenteParlamentar)
            "$front frentes parlamentares".also { textFrenteParlamentar.text = it }
        }
    }

    override fun listenerFront(note: DadoFrente) {}

}