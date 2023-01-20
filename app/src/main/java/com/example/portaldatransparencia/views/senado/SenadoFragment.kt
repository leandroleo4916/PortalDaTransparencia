package com.example.portaldatransparencia.views.senado

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.SenadoAdapter
import com.example.portaldatransparencia.databinding.FragmentCamaraSenadoBinding
import com.example.portaldatransparencia.interfaces.IClickSenador
import com.example.portaldatransparencia.interfaces.INotificationSenado
import com.example.portaldatransparencia.remote.ResultSenadoRequest
import com.example.portaldatransparencia.util.RetiraAcento
import com.example.portaldatransparencia.views.activity.gastogeral.senado.ActivityGastoGeralSenado
import com.example.portaldatransparencia.views.activity.ranking.senado.ActivityRankingSenado
import com.example.portaldatransparencia.views.activity.votacoes.senado.ActivityVotacoesSenado
import com.example.portaldatransparencia.views.camara.CamaraFragment
import com.example.portaldatransparencia.views.senado.senador.SenadorActivity
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.ModifyChip
import com.example.portaldatransparencia.views.view_generics.VisibilityNavViewAndFloating
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SenadoFragment: Fragment(R.layout.fragment_camara_senado), IClickSenador, INotificationSenado {

    private var binding: FragmentCamaraSenadoBinding? = null
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val modifyChip: ModifyChip by inject()
    private val retiraAcento: RetiraAcento by inject()
    private val hideView: EnableDisableView by inject()
    private val visibilityNavViewAndFloating: VisibilityNavViewAndFloating by inject()
    private lateinit var adapter: SenadoAdapter
    private var chipEnabled: Chip? = null
    private var chipEnabledState: Chip? = null
    private var textPartido = ""
    private var hideFilter = true
    private val permissionCode = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCamaraSenadoBinding.bind(view)

        recycler()
        observer()
        showTabView()
        listener()
        search()
    }

    private fun recycler() {
        val recycler = binding?.recyclerDeputados
        adapter = context?.let { SenadoAdapter(this, this, it) }!!
        recycler?.layoutManager = LinearLayoutManager(context)
        recycler?.adapter = adapter
    }

    private fun observer(){
        senadoViewModel.searchDataSenado().observe(viewLifecycleOwner){
            it?.let { result ->
                when (result) {
                    is ResultSenadoRequest.Success -> {
                        result.dado?.let { senado ->
                            hideView.disableView(binding?.progressMain!!)
                            adapter.updateData(
                                senado.listaParlamentarEmExercicio.parlamentares.parlamentar)
                        }
                    }
                    is ResultSenadoRequest.Error -> {
                        result.exception.message?.let {  }
                    }
                    is ResultSenadoRequest.ErrorConnection -> {
                        result.exception.message?.let {  }
                    }
                }
            }
        }
    }

    private fun search() {
        binding?.textSearch?.editText?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun listener(){
        binding?.run {
            chipGroupItem.run {
                chipAvante.setOnClickListener { modifyChipPartido(chipAvante) }
                chipCidadania.setOnClickListener { modifyChipPartido(chipCidadania) }
                chipDc.setOnClickListener { modifyChipPartido(chipDc) }
                chipDem.setOnClickListener { modifyChipPartido(chipDem) }
                chipMdb.setOnClickListener { modifyChipPartido(chipMdb) }
                chipNovo.setOnClickListener { modifyChipPartido(chipNovo) }
                chipPatri.setOnClickListener { modifyChipPartido(chipPatri) }
                chipPatriota.setOnClickListener { modifyChipPartido(chipPatriota) }
                chipPcb.setOnClickListener { modifyChipPartido(chipPcb) }
                chipPcdob.setOnClickListener { modifyChipPartido(chipPcdob) }
                chipPco.setOnClickListener { modifyChipPartido(chipPco) }
                chipPdt.setOnClickListener { modifyChipPartido(chipPdt) }
                chipPhs.setOnClickListener { modifyChipPartido(chipPhs) }
                chipPl.setOnClickListener { modifyChipPartido(chipPl) }
                chipPros.setOnClickListener { modifyChipPartido(chipPros) }
                chipPsc.setOnClickListener { modifyChipPartido(chipPsc) }
                chipPmb.setOnClickListener { modifyChipPartido(chipPmb) }
                chipPodemos.setOnClickListener { modifyChipPartido(chipPodemos) }
                chipPp.setOnClickListener { modifyChipPartido(chipPp) }
                chipPt.setOnClickListener { modifyChipPartido(chipPt) }
                chipRepublicanos.setOnClickListener { modifyChipPartido(chipRepublicanos) }
                chipUniao.setOnClickListener { modifyChipPartido(chipUniao) }

                chipAc.setOnClickListener { modifyChipState(chipAc) }
                chipAl.setOnClickListener { modifyChipState(chipAl) }
                chipAp.setOnClickListener { modifyChipState(chipAp) }
                chipAm.setOnClickListener { modifyChipState(chipAm) }
                chipBa.setOnClickListener { modifyChipState(chipBa) }
                chipCe.setOnClickListener { modifyChipState(chipCe) }
                chipDf.setOnClickListener { modifyChipState(chipDf) }
                chipEs.setOnClickListener { modifyChipState(chipEs) }
                chipGo.setOnClickListener { modifyChipState(chipGo) }
                chipMa.setOnClickListener { modifyChipState(chipMa) }
                chipMt.setOnClickListener { modifyChipState(chipMt) }
                chipMs.setOnClickListener { modifyChipState(chipMs) }
                chipMg.setOnClickListener { modifyChipState(chipMg) }
                chipPa.setOnClickListener { modifyChipState(chipPa) }
                chipPb.setOnClickListener { modifyChipState(chipPb) }
                chipPr.setOnClickListener { modifyChipState(chipPr) }
                chipPe.setOnClickListener { modifyChipState(chipPe) }
                chipPi.setOnClickListener { modifyChipState(chipPi) }
                chipRj.setOnClickListener { modifyChipState(chipRj) }
                chipRn.setOnClickListener { modifyChipState(chipRn) }
                chipRs.setOnClickListener { modifyChipState(chipRs) }
                chipRo.setOnClickListener { modifyChipState(chipRo) }
                chipRr.setOnClickListener { modifyChipState(chipRr) }
                chipSc.setOnClickListener { modifyChipState(chipSc) }
                chipSp.setOnClickListener { modifyChipState(chipSp) }
                chipSe.setOnClickListener { modifyChipState(chipSe) }
                chipTo.setOnClickListener { modifyChipState(chipTo) }
            }

            icVoz.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                permissionVoice()
            }
            icFilter.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                showFilterIcons()
            }
            floatingController.setOnClickListener {
                recyclerDeputados.smoothScrollToPosition(0)
                context?.let { it1 ->
                    visibilityNavViewAndFloating.visibilityNavViewAndFloating(
                        it1, true, floatingController
                    )}
            }
            layoutItemParlamento.run {
                constraintLayout1.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                    val intent = Intent(context, ActivityGastoGeralSenado::class.java)
                    startActivity(intent)
                }
                constraintLayout2.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                    val intent = Intent(context, ActivityRankingSenado::class.java)
                    startActivity(intent)
                }
                constraintLayout3.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
                    val intent = Intent(context, ActivityVotacoesSenado::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showFilterIcons(){
        binding?.run {
            if (hideFilter){
                icFilter.setImageResource(R.drawable.ic_no_filter)
                hideFilter = false
                frameChip.let { hideView.enableView(it) }
            }
            else {
                icFilter.setImageResource(R.drawable.ic_filter)
                hideFilter = true
                frameChip.let { hideView.disableView(it) }
            }
        }
    }

    private fun modifyChipPartido(viewDisabled: Chip) {
        chipEnabled = modifyChip.modify(chipEnabled, viewDisabled)
        if (chipEnabledState != null) {
            chipEnabledState!!.isChecked = false
        }
        textPartido = viewDisabled.text as String
        if (!viewDisabled.isChecked) {
            adapter.filter.filter("")
            textPartido = ""
        }
        else {
            adapter.filter.filter(viewDisabled.text as String)
        }
    }

    private fun modifyChipState(viewClicked: Chip) {
        chipEnabledState = modifyChip.modify(chipEnabledState, viewClicked)
        if (viewClicked.isChecked) {
            if (textPartido.isEmpty()){
                adapter.filter.filter(viewClicked.text as String)
            }
            else adapter.filterList(textPartido, viewClicked.text as String)
        }
        else adapter.filter.filter(textPartido)
    }

    override fun clickSenador(id: String, nome: String) {
        val name = retiraAcento.deleteAccent(nome)
        val intent = Intent(context, SenadorActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("nome", name)
        startActivity(intent)
    }

    private fun showTabView() {
        binding?.run {
            context?.let {
                visibilityNavViewAndFloating.showTabView(appbar, it, floatingController)
            }
        }
    }

    override fun notification() {
        Toast.makeText(context, getString(R.string.nao_encontrado_dep_partido),
            Toast.LENGTH_SHORT).show()
        binding?.progressMain?.visibility = View.INVISIBLE
    }

    private fun permissionVoice() {

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) }
            == PackageManager.PERMISSION_DENIED || context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) }
            == PackageManager.GET_PERMISSIONS) {

            val permission = arrayOf(Manifest.permission.RECORD_AUDIO)
            requestPermissions(permission, permissionCode)

        } else openVoice()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    openVoice()
                }
                else {
                    Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openVoice() {

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }
            if (intent.`package` == null) {
                startActivityForResult(intent, CamaraFragment.SPEECH_REQUEST_CODE)
            } else {
                Toast.makeText(context, "Erro na captura de voz",
                    Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) { }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CamaraFragment.SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let {
                        results -> results?.get(0)
                }
            binding?.textSearch?.editText?.setText(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}