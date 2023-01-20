package com.example.portaldatransparencia.views.activity.votacoes.senado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.VotacoesSenadoAdapter
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapter
import com.example.portaldatransparencia.databinding.ActivityVotacoesSenadoBinding
import com.example.portaldatransparencia.databinding.RecyclerVotacoesListSenadoBinding
import com.example.portaldatransparencia.dataclass.EventoDataClass
import com.example.portaldatransparencia.dataclass.VotacaoId
import com.example.portaldatransparencia.dataclass.VotacaoSenado
import com.example.portaldatransparencia.dataclass.VotacaoSenadoItem
import com.example.portaldatransparencia.interfaces.IClickSeeDetails
import com.example.portaldatransparencia.interfaces.IClickSeeVideo
import com.example.portaldatransparencia.interfaces.IClickSeeVote
import com.example.portaldatransparencia.remote.ApiServiceEvento
import com.example.portaldatransparencia.remote.ApiVotacoesSenado
import com.example.portaldatransparencia.remote.Retrofit
import com.example.portaldatransparencia.util.createDialog
import com.example.portaldatransparencia.views.activity.votacoes.camara.VotacoesViewModelCamara
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityVotacoesSenado: AppCompatActivity(), IClickSeeVideo, IClickSeeVote, IClickSeeDetails {

    private val binding by lazy { ActivityVotacoesSenadoBinding.inflate(layoutInflater) }
    private val bindingRecycler by lazy { RecyclerVotacoesListSenadoBinding.inflate(layoutInflater) }
    private val viewModel: VotacoesViewModelCamara by viewModel()
    private val statusView: EnableDisableView by inject()
    private lateinit var adapter: VotacoesSenadoAdapter
    private lateinit var adapterVoto: VotacoesSenadoVotoAdapter
    private lateinit var chipYear: Chip
    private lateinit var chipMonth: Chip
    private var votacoes: List<VotacaoSenadoItem> = arrayListOf()
    private var votacoesFilter: ArrayList<VotacaoSenadoItem> = arrayListOf()
    private var sizeVotacoes = 0
    private var value = 0
    private var year = "2023"
    private var month = "Todos"
    private var monthName = ""
    private lateinit var create: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chipYear = binding.layoutYear.chip2023
        chipMonth = binding.layoutMonth.chipAll

        recycler()
        observerVotacoesCamara()
        modifyTitle()
        listener()
        listenerChip()
    }

    private fun recycler() {
        adapterVoto = VotacoesSenadoVotoAdapter(application)
        val recyclerSim = bindingRecycler.recyclerVotoSim
        val recyclerNao = bindingRecycler.recyclerVotoNao
        val recyclerAbs = bindingRecycler.recyclerVotoAbstencao

        val recycler = binding.recyclerVotacoesSenado
        adapter = VotacoesSenadoAdapter(application, adapterVoto, recyclerSim, recyclerNao, recyclerAbs)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                finish()
            }
            imageViewFilter.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.click))
                modifyFilter()
            }
        }
    }

    private fun observerVotacoesCamara() {

        val retrofit = Retrofit.createService(ApiVotacoesSenado::class.java)
        val call: Call<VotacaoSenado> = retrofit.getVotacoes(year)
        call.enqueue(object: Callback<VotacaoSenado> {
            override fun onResponse(call: Call<VotacaoSenado>, response: Response<VotacaoSenado>) {
                when (response.code()){
                    200 ->
                        if (response.body() != null){
                            response.body()!!.listaVotacoes.votacoes.votacao.run {
                                votacoes = this
                                adapter.updateData(this as ArrayList<VotacaoSenadoItem>)
                                sizeVotacoes = this.size
                            }
                            addElement()
                        }
                        else disableProgressAndEnableText()

                    429 -> observerVotacoesCamara()
                    else -> disableProgressAndEnableText()
                }
            }
            override fun onFailure(call: Call<VotacaoSenado>, t: Throwable) {
                disableProgressAndEnableText()
            }
        })
    }

    private fun searchVideoVotacao(id: String) {

        val retrofit = Retrofit.createService(ApiServiceEvento::class.java)
        val call: Call<EventoDataClass> = retrofit.getEvento(id)
        call.enqueue(object: Callback<EventoDataClass> {
            override fun onResponse(call: Call<EventoDataClass>, response: Response<EventoDataClass>) {
                when (response.code()){
                    200 ->
                        if (response.body() != null){
                            response.body()!!.dados.run {
                                create.dismiss()
                                openVideoVotacao(this.urlRegistro)
                            }
                        }
                        else showToast("Não foi adicionado URL do vídeo")

                    429 -> searchVideoVotacao(id)
                    else -> showToast("API não respondeu")
                }
            }
            override fun onFailure(call: Call<EventoDataClass>, t: Throwable) {
                showToast("API não respondeu")
            }
        })
    }

    private fun openVideoVotacao(url: String){
        if (url.isNotEmpty()){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } else {
            Toast.makeText(application,
                "Não foi adicionado link do vídeo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun modifyTitle() {
        binding.layoutTop.run {
            textViewTitleTop.text = "Votacões - Senadores"
        }
    }

    private fun modifyFilter() {
        if (value == 1){
            binding.run {
                layoutTop.imageViewFilter.setImageResource(R.drawable.ic_no_filter_dark)
                statusView.enableView(frameYear)
                statusView.enableView(frameMonth)
            }
            value = 0
        }
        else {
            binding.run {
                layoutTop.imageViewFilter.setImageResource(R.drawable.ic_filter_dark)
                statusView.disableView(frameYear)
                statusView.disableView(frameMonth)
            }
            value = 1
        }
    }

    private fun listenerChip(){
        binding.run {
            layoutYear.run {
                chip2023.setOnClickListener { modify(chip2023) }
                chip2022.setOnClickListener { modify(chip2022) }
                chip2021.setOnClickListener { modify(chip2021) }
                chip2020.setOnClickListener { modify(chip2020) }
                chip2019.setOnClickListener { modify(chip2019) }
                chip2018.setOnClickListener { modify(chip2018) }
                chip2017.setOnClickListener { modify(chip2017) }
                chip2016.setOnClickListener { modify(chip2016) }
                chip2015.setOnClickListener { modify(chip2015) }
            }
            layoutMonth.run {
                chipAll.setOnClickListener { modifyChipMonth(chipAll) }
                chipJaneiro.setOnClickListener { modifyChipMonth(chipJaneiro) }
                chipFevereiro.setOnClickListener { modifyChipMonth(chipFevereiro) }
                chipMarco.setOnClickListener { modifyChipMonth(chipMarco) }
                chipAbril.setOnClickListener { modifyChipMonth(chipAbril) }
                chipMaio.setOnClickListener { modifyChipMonth(chipMaio) }
                chipJunho.setOnClickListener { modifyChipMonth(chipJunho) }
                chipJulho.setOnClickListener { modifyChipMonth(chipJulho) }
                chipAgosto.setOnClickListener { modifyChipMonth(chipAgosto) }
                chipSetembro.setOnClickListener { modifyChipMonth(chipSetembro) }
                chipOutubro.setOnClickListener { modifyChipMonth(chipOutubro) }
                chipNovembro.setOnClickListener { modifyChipMonth(chipNovembro) }
                chipDezembro.setOnClickListener { modifyChipMonth(chipDezembro) }
            }
        }
    }

    private fun modify(chip: Chip) {
        enableProgressAndDisable()
        binding.run {
            layoutMonth.run {
                chipAll.isChecked = true
                if (chipMonth.text != "Todos") {
                    chipMonth.isChecked = false
                    chipMonth = chipAll
                    month = chipAll.text.toString()
                }
            }
        }
        adapter.updateData(arrayListOf())
        year = chip.text.toString()
        chipYear.isChecked = false
        chip.isChecked = true
        chipYear = chip
        observerVotacoesCamara()
    }

    private fun modifyChipMonth(viewDisabled: Chip) {
        enableProgressAndDisable()
        month = viewDisabled.hint.toString()
        monthName = viewDisabled.text.toString()
        chipMonth.isChecked = false
        viewDisabled.isChecked = true
        chipMonth = viewDisabled
        adapter.updateData(arrayListOf())

        if (month != "Todos") {
            adapter.updateData(arrayListOf())
            votacoesFilter = arrayListOf()
            votacoes.forEach {
                if (it.dataSessao != ""){
                    if (it.dataSessao.split("-")[1].contains(month))
                        votacoesFilter.add(it)
                }
            }
            sizeVotacoes = votacoesFilter.size
            disableProgress()
            if (sizeVotacoes != 0){
                adapter.updateData(votacoesFilter)
                addElement()
            }
            else {
                binding.run {
                    layoutTop.textViewDescriptionTop.text =
                        (if (sizeVotacoes == 0) "Nenhuma votação em $monthName de $year"
                         else "$sizeVotacoes votações em $monthName de $year").toString()

                    statusView.enableView(textNotValue)
                }
            }
        }
        else {
            adapter.updateData(votacoes as ArrayList<VotacaoSenadoItem>)
            sizeVotacoes = votacoes.size
            addElement()
        }
    }

    private fun addElement(){
        disableProgress()
        binding.run {
            layoutTop.textViewDescriptionTop.run {
                text = (if (month == "Todos") "$sizeVotacoes votações em $year"
                else "$sizeVotacoes votações em $monthName de $year").toString()
                statusView.enableView(this)
                this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_votacao))
            }
        }
    }

    private fun disableProgress(){
        binding.run {
            statusView.run {
                disableView(progressVotacoes)
                disableView(constraintNumberVotacoes)
                disableView(textNotValue)
            }
        }
    }

    private fun disableProgressAndEnableText(){
        binding.run {
            statusView.run {
                disableView(progressVotacoes)
                enableView(textNotValue)
            }
            layoutTop.textViewDescriptionTop.text = getString(R.string.nenhuma_votacao_este_ano)
        }
        votacoes = arrayListOf()
        votacoesFilter = arrayListOf()
    }

    private fun enableProgressAndDisable(){
        binding.run {
            statusView.run {
                disableView(textNotValue)
                enableView(constraintNumberVotacoes)
                enableView(progressVotacoes)
            }
        }
    }

    override fun clickSeeVote(votacao: VotacaoId) {

    }

    override fun clickSeeVideo(votacao: VotacaoId) {
        val dialog = createDialog()
        dialog.setView(R.layout.layout_dialog)
        create = dialog.create()
        create.show()
        searchVideoVotacao(votacao.idEvento.toString())
    }

    override fun clickSeeDetails(votacao: VotacaoId) {

    }

    private fun showToast(message: String){
        create.dismiss()
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }
}