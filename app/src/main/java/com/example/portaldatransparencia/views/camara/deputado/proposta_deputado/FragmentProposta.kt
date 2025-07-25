package com.example.portaldatransparencia.views.camara.deputado.proposta_deputado

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.PropostaAdapter
import com.example.portaldatransparencia.databinding.FragmentPropostaBinding
import com.example.portaldatransparencia.dataclass.PropostaDataClass
import com.example.portaldatransparencia.interfaces.IClickItemProposta
import com.example.portaldatransparencia.network.ApiServiceProposta
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.security.SecurityPreferences
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentProposta: Fragment(R.layout.fragment_proposta), IClickItemProposta {

    private var binding: FragmentPropostaBinding? = null
    private val viewModel: PropostaViewModel by viewModel()
    private lateinit var adapter: PropostaAdapter
    private val securityPreferences: SecurityPreferences by inject()
    private val statusView: EnableDisableView by inject()
    private lateinit var id: String
    private lateinit var chipEnabled: Chip
    private var numberProposta = 0
    private var page = 1
    private var year = "2025"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPropostaBinding.bind(view)

        binding!!.chipGroupItem.chip2025.apply {
            chipEnabled = this
            this.isChecked = true
        }
        id = securityPreferences.getString("id")
        recyclerView()
        listenerChip()
        observer()
    }

    private fun recyclerView() {
        val recycler = binding!!.recyclerProposta
        adapter = PropostaAdapter(this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    private fun observer() {

        val retrofit = Retrofit.createService(ApiServiceProposta::class.java)
        val call: Call<PropostaDataClass> = retrofit.getProposta(year, id, 100, page)
        call.enqueue(object: Callback<PropostaDataClass> {
            override fun onResponse(call: Call<PropostaDataClass>, res: Response<PropostaDataClass>) {
                when (res.code()){
                    200 -> {
                        if (res.body() != null && res.body()!!.dados.isNotEmpty()){
                            val size = res.body()!!.dados.size
                            calculatePropostas(size, page)
                            adapter.updateData(res.body()!!.dados, page)
                            numberProposta += size
                            page += 1
                            if (size >= 100) observer()
                        }
                        else {
                            if (numberProposta == 0) {
                                setTextNoProposta()
                                adapter.updateData(res.body()!!.dados, page)
                            }
                        }
                    }
                    429 -> observer()
                    else -> setTextNoProposta()
                }
            }

            override fun onFailure(call: Call<PropostaDataClass>, t: Throwable) {
                setTextNoProposta()
            }
        })
    }

    private fun listenerChip(){
        binding?.run {
            chipGroupItem.run {
                chip2025.setOnClickListener { modify(chipEnabled, chip2025) }
                chip2024.setOnClickListener { modify(chipEnabled, chip2024) }
                chip2023.setOnClickListener { modify(chipEnabled, chip2023) }
                chip2022.setOnClickListener { modify(chipEnabled, chip2022) }
                chip2021.setOnClickListener { modify(chipEnabled, chip2021) }
                chip2020.setOnClickListener { modify(chipEnabled, chip2020) }
                chip2019.setOnClickListener { modify(chipEnabled, chip2019) }
                chip2018.setOnClickListener { modify(chipEnabled, chip2018) }
                chip2017.setOnClickListener { modify(chipEnabled, chip2017) }
                chip2016.setOnClickListener { modify(chipEnabled, chip2016) }
                chip2015.setOnClickListener { modify(chipEnabled, chip2015) }
                chip2014.setOnClickListener { modify(chipEnabled, chip2014) }
                chip2013.setOnClickListener { modify(chipEnabled, chip2013) }
                chip2012.setOnClickListener { modify(chipEnabled, chip2012) }
                chip2011.setOnClickListener { modify(chipEnabled, chip2011) }
            }
        }
    }

    private fun modify(viewEnabled: Chip, viewDisabled: Chip) {
        viewEnabled.isChecked = false
        viewDisabled.isChecked = true
        numberProposta = 0
        year = viewDisabled.text.toString()
        page = 1
        chipEnabled = viewDisabled
        binding?.run {
            statusView.run {
                enableView(layoutProgressAndText.progressActive)
                disableView(textPropostaParlamentar)
                disableView(iconProposta)
                disableView(layoutProgressAndText.textNotValue)
            }
        }
        adapter.updateData(arrayListOf(), 1)
        observer()
    }

    private fun calculatePropostas(size: Int, pagina: Int){
        if (pagina == 1) numberProposta = 0
        numberProposta += size
        binding?.run {
            statusView.run {
                disableView(layoutProgressAndText.progressActive)
                enableView(textPropostaParlamentar)
                enableView(iconProposta)
                disableView(layoutProgressAndText.textNotValue)
            }
            textPropostaParlamentar.text =
                if (numberProposta == 1) "$numberProposta projeto de lei"
                else "$numberProposta projetos de lei"
        }
    }

    private fun setTextNoProposta(){
        binding?.layoutProgressAndText?.run {
            statusView.disableView(progressActive)
            statusView.enableView(textNotValue)
            textNotValue.text = "Nenhum projeto de lei para $year"
        }
    }

    override fun clickProposta(id: String) {
        val intent = Intent(context, FragmentPropostaItem::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}