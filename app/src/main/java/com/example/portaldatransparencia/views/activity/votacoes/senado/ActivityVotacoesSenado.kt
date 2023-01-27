package com.example.portaldatransparencia.views.activity.votacoes.senado

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.VotacoesSenadoAdapter
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapter
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapterAbs
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapterNao
import com.example.portaldatransparencia.databinding.ActivityVotacoesSenadoBinding
import com.example.portaldatransparencia.dataclass.AddVoto
import com.example.portaldatransparencia.dataclass.VotacaoSenado
import com.example.portaldatransparencia.dataclass.VotacaoSenadoItem
import com.example.portaldatransparencia.dataclass.VotoParlamentar
import com.example.portaldatransparencia.interfaces.IAddVotoInRecycler
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.network.ApiVotacoesSenado
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.util.RetiraAcento
import com.example.portaldatransparencia.util.createDialog
import com.example.portaldatransparencia.views.activity.votacoes.camara.VotacoesViewModelCamara
import com.example.portaldatransparencia.views.senado.senador.SenadorActivity
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.ModifyHttpToHttps
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityVotacoesSenado: AppCompatActivity(), IAddVotoInRecycler, IClickSenador {

    private val binding by lazy { ActivityVotacoesSenadoBinding.inflate(layoutInflater) }
    private val viewModel: VotacoesViewModelCamara by viewModel()
    private val statusView: EnableDisableView by inject()
    private val modifyHttp: ModifyHttpToHttps by inject()
    private lateinit var adapter: VotacoesSenadoAdapter
    private lateinit var adapterSim: VotacoesSenadoVotoAdapter
    private lateinit var adapterNao: VotacoesSenadoVotoAdapterNao
    private lateinit var adapterAbs: VotacoesSenadoVotoAdapterAbs
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
    private val retiraAcento: RetiraAcento by inject()

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

        adapterSim = VotacoesSenadoVotoAdapter(this)
        adapterNao = VotacoesSenadoVotoAdapterNao(this)
        adapterAbs = VotacoesSenadoVotoAdapterAbs(this)

        val recycler = binding.recyclerVotacoesSenado
        adapter = VotacoesSenadoAdapter(this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun listener() {
        binding.layoutTop.run {
            imageViewBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(application, R.anim.click))
                finish()
            }
            imageViewFilter.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(application, R.anim.click))
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

    override fun addVoto(list: List<VotoParlamentar>, descricao: String?) {

        val listSim: ArrayList<AddVoto> = arrayListOf()
        val listNao: ArrayList<AddVoto> = arrayListOf()
        val listAbs: ArrayList<AddVoto> = arrayListOf()

        list.forEach {
            val url = modifyHttp.modifyUrl(it.foto)
            val foto = Glide.with(this).load(url)
            when (it.voto){
                "Sim" -> listSim.add(AddVoto(it.codigoParlamentar, it.nomeParlamentar, foto))
                "Não" -> listNao.add(AddVoto(it.codigoParlamentar, it.nomeParlamentar, foto))
                else ->  listAbs.add(AddVoto(it.codigoParlamentar, it.nomeParlamentar, foto))
            }
        }
        createViewDialog(listSim, listNao, listAbs, descricao)
    }

    private fun createViewDialog (listSim: ArrayList<AddVoto>, listNao: ArrayList<AddVoto>,
                                  listAbs: ArrayList<AddVoto>, descricao: String?){

        val dialog = createDialog()
        val inflate = layoutInflater.inflate(R.layout.dialog_list_voto_senado, null)
        val textVoto = inflate.findViewById<TextView>(R.id.text_voto_title)
        textVoto.text = descricao

        val sim = inflate.findViewById<RecyclerView>(R.id.recyclerView_voto_sim)
        val textSim = inflate.findViewById<TextView>(R.id.text_sim)
        textSim.text = "${searchVoto(listSim.size)}Sim"
        sim.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sim.adapter = adapterSim
        adapterSim.updateData(listSim)

        val nao = inflate.findViewById<RecyclerView>(R.id.recyclerView_voto_nao)
        val textNao = inflate.findViewById<TextView>(R.id.text_nao)
        textNao.text = "${searchVoto(listNao.size)}Não"
        nao.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        nao.adapter = adapterNao
        adapterNao.updateData(listNao)

        val abs = inflate.findViewById<RecyclerView>(R.id.recyclerView_voto_abs)
        val textAbs = inflate.findViewById<TextView>(R.id.text_abstencao)
        textAbs.text = "${searchVoto(listAbs.size)}Abstenção"
        abs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        abs.adapter = adapterAbs
        adapterAbs.updateData(listAbs)

        dialog.setView(inflate)
        create = dialog.create()
        create.show()
    }

    private fun searchVoto(size: Int): String{
        return when (size){
            0 -> "Nenhum voto - "
            1 -> "1 voto - "
            else -> "$size votos - "
        }
    }

    override fun clickSenador(id: String, nome: String) {
        create.dismiss()
        val name = retiraAcento.deleteAccent(nome)
        val intent = Intent(this, SenadorActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("nome", name)
        startActivity(intent)
    }
}