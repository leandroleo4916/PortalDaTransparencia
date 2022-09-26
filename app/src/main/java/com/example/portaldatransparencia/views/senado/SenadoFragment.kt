package com.example.portaldatransparencia.views.senado

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.SenadoAdapter
import com.example.portaldatransparencia.databinding.FragmentMainBinding
import com.example.portaldatransparencia.dataclass.IdentificacaoParlamentar
import com.example.portaldatransparencia.dataclass.Parlamentar
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.interfaces.INotificationSenado
import com.example.portaldatransparencia.remote.ResultSenadoRequest
import com.example.portaldatransparencia.views.VisibilityNavViewAndFloating
import com.example.portaldatransparencia.views.deputado.DeputadoActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class SenadoFragment: Fragment(R.layout.fragment_main), IClickSenador, INotificationSenado {

    private var binding: FragmentMainBinding? = null
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val visibilityNavViewAndFloating: VisibilityNavViewAndFloating by inject()
    private lateinit var adapter: SenadoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        recycler()
        observer()
        showTabView()
    }

    private fun recycler() {
        val recycler = binding?.recyclerDeputados
        adapter = SenadoAdapter(this, this)
        recycler?.layoutManager = LinearLayoutManager(context)
        recycler?.adapter = adapter
    }

    private fun observer(){
        senadoViewModel.searchDataSenado().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultSenadoRequest.Success -> {
                        result.dado?.let { senado ->
                            adapter.updateData(
                                senado.listaParlamentarEmExercicio.parlamentares.parlamentar)
                        }
                    }
                    is ResultSenadoRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultSenadoRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    override fun clickSenador(id: String) {
        val intent = Intent(context, DeputadoActivity::class.java)
        intent.putExtra("senador", id)
        startActivity(intent)
    }

    private fun showTabView() {
        binding?.run {
            context?.let {
                visibilityNavViewAndFloating.showTabView(appbar,
                    it, floatingController)
            }
        }
    }

    override fun notification() {
        TODO("Not yet implemented")
    }
}