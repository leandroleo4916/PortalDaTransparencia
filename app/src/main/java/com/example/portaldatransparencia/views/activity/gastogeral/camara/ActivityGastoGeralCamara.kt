package com.example.portaldatransparencia.views.activity.gastogeral.camara

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.adapter.GraphGastoAdapter
import com.example.portaldatransparencia.databinding.FragmentMaisBinding
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.interfaces.ISmoothPosition
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara
import com.example.portaldatransparencia.util.FormatValor
import com.example.portaldatransparencia.util.FormaterValueBilhoes
import com.example.portaldatransparencia.util.RetValueInt
import com.example.portaldatransparencia.views.view_generics.AddValueViewGraph
import com.example.portaldatransparencia.views.view_generics.AnimationView
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityGastoGeralCamara: AppCompatActivity(), ISmoothPosition {

    private val binding by lazy { FragmentMaisBinding.inflate(layoutInflater) }
    private val viewModel: GastoGeralViewModelCamara by viewModel()
    private val hideView: EnableDisableView by inject()
    private val formatValor: FormaterValueBilhoes by inject()
    private val crossFade: AnimationView by inject()
    private val retValueInt: RetValueInt by inject()
    private val addValue: AddValueViewGraph by inject()
    private lateinit var adapter: GastoSetorAdapter
    private lateinit var adapterGraph: GraphGastoAdapter
    private lateinit var gastoCamara: GastoGeralCamara
    private lateinit var chipSelected: Chip
    private var infoSetor: ArrayList<AddInfoSetor> = arrayListOf()
    private var ano = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chipGroupItem.chipAll.apply {
            chipSelected = this
            hideView.enableView(this)
        }
        recyclerAdapter()
        modifyElementTop()
        observerGastoCamara()
        listener()
        listenerChip()
    }

    private fun recyclerAdapter(){
        val recycler = binding.recyclerGastoSetor
        adapter = GastoSetorAdapter(FormatValor(), crossFade, this)
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter

        val recyclerGraph = binding.layoutGraph.recyclerGraph
        adapterGraph = GraphGastoAdapter(addValue, retValueInt)
        recyclerGraph.layoutManager =
            LinearLayoutManager(this.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerGraph.adapter = adapterGraph
    }
    
    private fun modifyElementTop(){
        binding.run {
            layoutTop.run {
                textViewDescriptionTop.text = getString(R.string.gastoGeral12Anos)
                hideView.enableView(textViewDescriptionTop)
                hideView.disableView(imageViewFilter)
            }
            chipGroupItem.chip2023.isChecked = false
        }
    }

    private fun listenerChip(){
        binding.run {
            chipGroupItem.run {
                chipAll.setOnClickListener { modify(chipSelected, chipAll) }
                chip2023.setOnClickListener { modify(chipSelected, chip2023) }
                chip2022.setOnClickListener { modify(chipSelected, chip2022) }
                chip2021.setOnClickListener { modify(chipSelected, chip2021) }
                chip2020.setOnClickListener { modify(chipSelected, chip2020) }
                chip2019.setOnClickListener { modify(chipSelected, chip2019) }
                chip2018.setOnClickListener { modify(chipSelected, chip2018) }
                chip2017.setOnClickListener { modify(chipSelected, chip2017) }
                chip2016.setOnClickListener { modify(chipSelected, chip2016) }
                chip2015.setOnClickListener { modify(chipSelected, chip2015) }
                chip2014.setOnClickListener { modify(chipSelected, chip2014) }
                chip2013.setOnClickListener { modify(chipSelected, chip2013) }
                chip2012.setOnClickListener { modify(chipSelected, chip2012) }
                chip2011.setOnClickListener { modify(chipSelected, chip2011) }
            }
        }
    }

    private fun modifyValueViewGraph(view: View){
        val layoutParams = view.layoutParams
        layoutParams.height = 1
        view.layoutParams = layoutParams
    }

    private fun modify(viewSelected: Chip, viewClicked: Chip) {

        if (ano != viewClicked.text){
            hideView.run {
                binding.layoutProgressAndText.run {
                    disableView(progressActive)
                    disableView(textNotValue)
                }
                disableView(binding.linearLayout)
            }
            adapter.updateData(arrayListOf())
            ano = viewClicked.text.toString()
            viewSelected.isChecked = false
            viewClicked.isChecked = true
            chipSelected = viewClicked
            observerGastoCamara()
        }
    }

    private fun observerGastoCamara() {

        viewModel.gastoGeralCamara(ano).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultGastoGeralCamara.Success -> {
                        result.dado?.let { gastos ->
                            gastoCamara = gastos
                            addElementCamara()
                            buildGraphCamara()
                        }
                    }
                    is ResultGastoGeralCamara.Error -> {
                        result.exception.message?.let {
                            binding.layoutProgressAndText.run {
                                hideView.disableView(progressActive)
                                textNotValue.apply {
                                    this.text = "Por enquanto não temos dados do ano $ano"
                                    hideView.enableView(this)
                                }
                            }
                        }
                    }
                    is ResultGastoGeralCamara.ErrorConnection -> {
                        result.exception.message?.let { }
                    }
                }
            }
        }
    }

    private fun addElementCamara() {
        binding.run {
            gastoCamara.total.run {
                val total = formatValor.formatValor(totalGeral.toDouble())
                textViewGastoParlamentar.text = total
                textViewTotalNotas.text = totalNotas
                hideView.run {
                    disableView(layoutProgressAndText.progressActive)
                    enableView(linearLayout)
                    crossFade.crossFade(constraintNumberParlamentar, true)
                    crossFade.crossFade(constraintNumberTotal, true)
                    crossFade.crossFade(constraintNumberNotas, true)
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun buildGraphCamara(){
        infoSetor = arrayListOf()
        gastoCamara.total.run {
            infoSetor.add(AddInfoSetor(getString(R.string.manutencao_escritorio), this.manutencao,
                R.drawable.back_1, getString(R.color.color1),
                "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg"))
            infoSetor.add(AddInfoSetor(getString(R.string.combustivel_lubrificante), this.combustivel,
                R.drawable.back_2, getString(R.color.color2),
                "https://cdn-icons-png.flaticon.com/512/2311/2311324.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.passagens_aereas), this.passagens,
                R.drawable.back_3, getString(R.color.color3),
                "https://cdn-icons-png.flaticon.com/512/5014/5014749.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.assinaturas), this.assinatura,
                R.drawable.back_4, getString(R.color.color4),
            "https://cdn-icons-png.flaticon.com/512/1466/1466339.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.divulgacao_parlamentar), this.divulgacao,
                R.drawable.back_5, getString(R.color.color5),
                "https://cdn-icons-png.flaticon.com/512/6520/6520327.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.servicos_telefonicos), this.telefonia,
                R.drawable.back_6, getString(R.color.color6),
                "https://cdn-icons-png.flaticon.com/512/126/126103.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.servico_postal), this.postais,
                R.drawable.back_7, getString(R.color.color7),
                "https://cdn-icons-png.flaticon.com/512/4280/4280211.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.hospedagem), this.hospedagem,
                R.drawable.back_8, getString(R.color.color8),
                "https://cdn-icons-png.flaticon.com/512/10/10674.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.taxi), this.taxi,
                R.drawable.back_9, getString(R.color.color9),
                "https://media.istockphoto.com/id/1294019348/pt/vetorial/person-catching-taxi-vector-icon.jpg?s=612x612&w=is&k=20&c=Hae45Icbu0rLVukDYxQ1iBets8taivGe-YIUJVnmD2c="))
            infoSetor.add(AddInfoSetor(getString(R.string.locacao), this.locacao,
                R.drawable.back_10, getString(R.color.color10),
                "https://cdn-icons-png.flaticon.com/512/84/84925.png?w=740&t=st=1671117756~exp=1671118356~hmac=f0f55b1b53a67ee4715c3eb6527f63761f0d82210c1e76419cb17fc2b4891958"))
            infoSetor.add(AddInfoSetor(getString(R.string.consultoriaAcessoria), this.consultoria,
                R.drawable.back_11, getString(R.color.color11),
                "https://cdn-icons-png.flaticon.com/512/1522/1522778.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.seguranca), this.seguranca,
                R.drawable.back_12, getString(R.color.color12),
                "https://cdn-icons-png.flaticon.com/512/4185/4185148.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.curso), this.curso,
                R.drawable.back_13, getString(R.color.color13),
                "https://cdn-icons-png.flaticon.com/512/1323/1323490.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.alimentacao), this.alimentacao,
                R.drawable.back_14, getString(R.color.color14),
                "https://cdn-icons-png.flaticon.com/512/6799/6799692.png"))
            infoSetor.add(AddInfoSetor(getString(R.string.outros_servicos), this.outros,
                R.drawable.back_15, getString(R.color.color15),
                "https://cdn-icons-png.flaticon.com/512/4692/4692103.png"))
        }
        adapter.updateData(infoSetor)
        adapterGraph.updateData(infoSetor)
        //addValueInGraph(infoSetor)
    }

    private fun smoothRecycler(position: Int, view: View){
        val recycler = binding.recyclerGastoSetor
        recycler.smoothScrollToPosition(position)
        view.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
        }
    }

    override fun smoothPosition(position: Int) {

    }
}