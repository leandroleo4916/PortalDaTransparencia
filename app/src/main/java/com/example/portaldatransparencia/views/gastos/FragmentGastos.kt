package com.example.portaldatransparencia.views.gastos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DespesasAdapter
import com.example.portaldatransparencia.databinding.FragmentGastosBinding
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.interfaces.INoteDespesas
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import com.example.portaldatransparencia.security.SecurityPreferences
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class FragmentGastos: Fragment(R.layout.fragment_gastos), INoteDespesas {

    private var binding: FragmentGastosBinding? = null
    private val viewModel: DespesasViewModel by viewModel()
    private lateinit var adapter: DespesasAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var chipEnabled: Chip
    private lateinit var id: String
    private var total = 0.0
    private var numberNote = 0
    private var page = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGastosBinding.bind(view)
        chipEnabled = binding!!.chip2022
        id = securityPreferences.getStoredString("id")
        recyclerView()
        observer(id, "2022", page)
        listenerChip()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerDespesas
        adapter = DespesasAdapter(this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer(id: String, year: String, pagina: Int) {

        viewModel.searchDespesasDeputado(id, year, pagina).observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultDespesasRequest.Success -> {
                        result.dado?.let { despesas ->
                            if (despesas.dados.isNotEmpty()){
                                val size = despesas.dados.size
                                numberNote += size
                                calculateNumberNote()
                                calculateTotal(despesas.dados, page)
                                adapter.updateData(despesas.dados, page)
                                page += 1
                                if (size >= 100) observer(id, year, page)

                            }else{
                                binding?.progressDespesas?.visibility = View.GONE
                                binding?.textNotesSend?.visibility = View.INVISIBLE
                                binding?.textTotal?.visibility = View.VISIBLE
                                binding?.textTotal?.text = "Não há dados no ano ${year}"
                                adapter.updateData(despesas.dados, page)
                            }
                        }
                    }
                    is ResultDespesasRequest.Error -> {
                        result.exception.message?.let { it -> }
                    }
                    is ResultDespesasRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> }
                    }
                }
            }
        }
    }

    private fun calculateNumberNote(){
        binding!!.textNotesSend.text = "$numberNote notas fiscais"
    }

    private fun calculateTotal(dados: List<DadoDespesas>, page: Int) {
        if (page == 1) total = 0.0
        dados.forEach { total = (total+it.valorDocumento) }

        val format = DecimalFormat("#.00")
        val formatTotal = format.format(total)
        ("R$ $formatTotal").also { binding!!.textTotal.text = it }

        binding?.progressDespesas?.visibility = View.GONE
        binding?.textNotesSend?.visibility = View.VISIBLE
        binding?.textTotal?.visibility = View.VISIBLE
    }

    private fun listenerChip(){
        binding?.run {
            chip2022.setOnClickListener { modify(chipEnabled, chip2022) }
            chip2021.setOnClickListener { modify(chipEnabled, chip2021) }
            chip2020.setOnClickListener { modify(chipEnabled, chip2020) }
            chip2019.setOnClickListener { modify(chipEnabled, chip2019) }
            chip2018.setOnClickListener { modify(chipEnabled, chip2018) }
            chip2017.setOnClickListener { modify(chipEnabled, chip2017) }
            chip2016.setOnClickListener { modify(chipEnabled, chip2016) }
            chip2015.setOnClickListener { modify(chipEnabled, chip2015) }
        }
    }

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {
        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        numberNote = 0
        page = 1
        chipEnabled = viewDisabled
        observer(id, viewDisabled.text as String, page)
    }

    override fun listenerDespesas(note: DadoDespesas) {
        if (note.urlDocumento != null){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(note.urlDocumento))
            startActivity(browserIntent)
        } else {
            Toast.makeText(context, "Comprovante não enviado", Toast.LENGTH_SHORT).show()
        }
    }
}