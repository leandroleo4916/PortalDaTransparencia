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
import com.example.portaldatransparencia.interfaces.IFront
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFrente: Fragment(R.layout.fragment_frente), IFront {

    private var binding: FragmentFrenteBinding? = null
    private val viewModel: FrontViewModel by viewModel()
    private val verifyInternet: ValidationInternet by inject()
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

        val internet = verifyInternet.validationInternet(requireContext().applicationContext)
        if (internet){
            viewModel.getFront(id)
            viewModel.responseLiveData.observe(viewLifecycleOwner){
                val size = it.dados.size
                calculateFront(size.toString())
                adapter.updateData(it.dados)
            }
            viewModel.responseErrorLiveData.observe(viewLifecycleOwner){
                addValue(it)
            }
        }
        else addValue(R.string.verifique_sua_internet)
    }

    private fun addValue(txt: Int){
        binding?.run {
            statusView.disableView(progressFront)
            statusView.enableView(textFrenteParlamentar)
            textFrenteParlamentar.text = getString(txt)
        }
    }

    private fun calculateFront(front: String){
        binding?.run {
            statusView.disableView(progressFront)
            statusView.enableView(textFrenteParlamentar)
            "$front frentes parlamentares".also { textFrenteParlamentar.text = it }
        }
    }

    override fun listenerFront(note: DadoFrente) {
        val intent = Intent(requireContext().applicationContext, FragmentFrenteId::class.java)
        intent.putExtra("id", note.id.toString())
        startActivity(intent)
    }

}