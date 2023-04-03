package com.example.portaldatransparencia.views.activity.votacoes.camara

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.VotacoesCamaraAdapter
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapter
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapterAbs
import com.example.portaldatransparencia.adapter.VotacoesSenadoVotoAdapterNao
import com.example.portaldatransparencia.databinding.ActivityVotacoesBinding
import com.example.portaldatransparencia.dataclass.*
import com.example.portaldatransparencia.interfaces.*
import com.example.portaldatransparencia.network.ApiServiceEvento
import com.example.portaldatransparencia.network.ApiServiceVotos
import com.example.portaldatransparencia.network.ApiVotacoes
import com.example.portaldatransparencia.network.Retrofit
import com.example.portaldatransparencia.repository.PropostaIdRepository
import com.example.portaldatransparencia.repository.ResultIdPropostaRequest
import com.example.portaldatransparencia.views.camara.deputado.DeputadoActivity
import com.example.portaldatransparencia.views.camara.deputado.proposta_deputado.FragmentPropostaItem
import com.example.portaldatransparencia.views.camara.deputado.proposta_deputado.PropostaViewModel
import com.example.portaldatransparencia.views.view_generics.createDialog
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityVotacoesCamara: AppCompatActivity(), IClickSeeVideo, IClickSeeVote,
    IClickSeeDetails, IClickParlamentar, IClickSeeDoc {

    private val binding by lazy { ActivityVotacoesBinding.inflate(layoutInflater) }
    private val viewModel: VotacoesViewModelCamara by viewModel()
    private val viewModelProposicao: PropostaViewModel by viewModel()
    private val statusView: EnableDisableView by inject()
    private val propostaId: PropostaIdRepository by inject()
    private lateinit var adapter: VotacoesCamaraAdapter
    private lateinit var chipYear: Chip
    private lateinit var chipMonth: Chip
    private lateinit var adapterSim: VotacoesSenadoVotoAdapter
    private lateinit var adapterNao: VotacoesSenadoVotoAdapterNao
    private lateinit var adapterAbs: VotacoesSenadoVotoAdapterAbs
    private var votacoes: List<VotacaoId> = arrayListOf()
    private var votacoesFilter: ArrayList<VotacaoId> = arrayListOf()
    private var sizeVotacoes = 0
    private var value = 0
    private var year = "2023"
    private var month = "Todos"
    private var monthName = ""
    private lateinit var create: AlertDialog
    private var shortAnimationDuration = 300

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

        val recycler = binding.recyclerVotacoes
        adapter = VotacoesCamaraAdapter(this, this, this, this)
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

        val retrofit = Retrofit.createService(ApiVotacoes::class.java)
        val call: Call<VotacoesList> = retrofit.getVotacoes(year)
        call.enqueue(object: Callback<VotacoesList> {
            override fun onResponse(call: Call<VotacoesList>, response: Response<VotacoesList>) {
                when (response.code()){
                    200 ->
                        if (response.body() != null && response.body()!!.dados.isNotEmpty()){
                            response.body()!!.dados.run {
                                votacoes = this
                                adapter.updateData(this as ArrayList<VotacaoId>)
                                sizeVotacoes = this.size
                            }
                            addElement()
                        }
                        else disableProgressAndEnableText(R.string.nenhuma_votacao_este_ano)

                    429 -> observerVotacoesCamara()
                    else -> disableProgressAndEnableText(R.string.nenhuma_votacao_este_ano)
                }
            }
            override fun onFailure(call: Call<VotacoesList>, t: Throwable) {
                disableProgressAndEnableText(R.string.erro_api_camara)
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
                    else -> showToast("Não foi adicionado URL do vídeo")
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
            textViewTitleTop.text = "Votacões - Deputados"
        }
    }

    private fun modifyFilter() {
        value = if (value == 1){
            binding.layoutTop.imageViewFilter.setImageResource(R.drawable.ic_no_filter_dark)
            crossFade(true)
            0
        } else {
            binding.layoutTop.imageViewFilter.setImageResource(R.drawable.ic_filter_dark)
            crossFade(false)
            1
        }
    }

    private fun crossFade(visible: Boolean) {
        binding.run {
            frameYear.apply {
                alpha = 0F
                visibility =
                    if (visible) View.VISIBLE
                    else View.GONE

                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setListener(null)
            }
            frameMonth.apply {
                alpha = 0F
                visibility =
                    if (visible) View.VISIBLE
                    else View.GONE

                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setListener(null)
            }
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
                chip2014.setOnClickListener { modify(chip2014) }
                chip2013.setOnClickListener { modify(chip2013) }
                chip2012.setOnClickListener { modify(chip2012) }
                chip2011.setOnClickListener { modify(chip2011) }
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
                if (it.data != ""){
                    if (it.data.split("-")[1].contains(month))
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
                    val value = if (sizeVotacoes == 0) "0 votação em $monthName de $year"
                    else "$sizeVotacoes votações em $monthName de $year"
                    layoutTop.textViewDescriptionTop.text = value

                    layoutProgressAndText.textNotValue.apply {
                        this.text = value
                        statusView.enableView(this)
                    }
                }
            }
        }
        else {
            adapter.updateData(votacoes as ArrayList<VotacaoId>)
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
        binding.layoutProgressAndText.run {
            statusView.run {
                disableView(progressActive)
                disableView(textNotValue)
            }
        }
    }

    private fun disableProgressAndEnableText(text: Int){
        binding.layoutProgressAndText.run {
            statusView.run {
                disableView(progressActive)
                enableView(textNotValue)
                textNotValue.text = getString(text)
            }
        }
        binding.layoutTop.textViewDescriptionTop.text = getString(R.string.nenhuma_votacao_este_ano)

        votacoes = arrayListOf()
        votacoesFilter = arrayListOf()
    }

    private fun enableProgressAndDisable(){
        binding.layoutProgressAndText.run {
            statusView.run {
                disableView(textNotValue)
                enableView(progressActive)
            }
        }
    }

    override fun clickSeeVote(votacao: VotacaoId) {

        createDialogProgress("Processando votos...")

        val retrofit = Retrofit.createService(ApiServiceVotos::class.java)
        val call: Call<VotoDeputadosDataC> = retrofit.getVoto(votacao.id)
        call.enqueue(object: Callback<VotoDeputadosDataC> {
            override fun onResponse(call: Call<VotoDeputadosDataC>, response: Response<VotoDeputadosDataC>) {
                when (response.code()){
                    200 -> response.body()?.run {
                        when {
                            this.dados.isNotEmpty() -> processVotos(this, votacao.siglaOrgao)
                            else -> showToast("Não foi registrados votos")
                        }
                    }
                    else -> showToast("Não foi registrados votos")
                }
            }
            override fun onFailure(call: Call<VotoDeputadosDataC>, t: Throwable) {
                showToast("API não respondeu")
            }
        })
    }

    private fun processVotos(data: VotoDeputadosDataC, votacao: String) {
        val listSim: ArrayList<AddVoto> = arrayListOf()
        val listNao: ArrayList<AddVoto> = arrayListOf()
        val listAbs: ArrayList<AddVoto> = arrayListOf()

        data.dados.forEach {
            val id = it.deputado.id.toString()
            val nome = it.deputado.nome
            val foto = Glide.with(this).load(it.deputado.urlFoto)
            when (it.tipoVoto){
                "Sim" -> listSim.add(AddVoto(id, nome, foto))
                "Não" -> listNao.add(AddVoto(id, nome, foto))
                else ->  listAbs.add(AddVoto(id, nome, foto))
            }
        }
        createViewDialog(listSim, listNao, listAbs, votacao)
    }

    override fun clickSeeVideo(votacao: VotacaoId) {
        createDialogProgress("Processando vídeo...")
        searchVideoVotacao(votacao.idEvento.toString())
    }

    override fun clickSeeDetails(votacao: VotacaoId) {
        val intent = Intent(applicationContext, FragmentPropostaItem::class.java)
        intent.putExtra("id", votacao.ultimaApresentacaoProposicao.idProposicao)
        startActivity(intent)
    }

    override fun clickParlamentar(id: String, nome: String) {
        create.dismiss()
        val intent = Intent(applicationContext, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun clickSeeDoc(votacao: VotacaoId) {
        createDialogProgress("Buscando documento...")
        val idProposicao = votacao.ultimaApresentacaoProposicao.idProposicao

        viewModel.getProposta(idProposicao.toString()).observe(this){
            it?.let { result ->
                when(result){
                    is ResultIdPropostaRequest.Success -> {
                        if (result.dado?.dados?.urlInteiroTeor != null && result.dado.dados.urlInteiroTeor != "" ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.dado.dados.urlInteiroTeor))
                            startActivity(intent)
                        }
                    }
                    is ResultIdPropostaRequest.Error -> {
                        showToast("Documento não foi anexado")
                    }
                    is ResultIdPropostaRequest.ErrorConnection -> {
                        showToast("Documento não foi anexado")
                    }
                }
            }
            create.dismiss()
        }
    }

    private fun createViewDialog (listSim: ArrayList<AddVoto>, listNao: ArrayList<AddVoto>,
                                  listAbs: ArrayList<AddVoto>, descricao: String?){

        create.dismiss()
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

    private fun createDialogProgress(text: String){
        val dialog = createDialog()
        val viewDialog = layoutInflater.inflate(R.layout.layout_dialog, null)
        val textView = viewDialog.findViewById<TextView>(R.id.text_not_value)
        textView.text = text
        dialog.setView(viewDialog)
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

    private fun showToast(message: String){
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }
}