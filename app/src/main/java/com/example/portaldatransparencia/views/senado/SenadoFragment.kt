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
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.SenadoAdapter
import com.example.portaldatransparencia.databinding.FragmentCamaraSenadoBinding
import com.example.portaldatransparencia.interfaces.IClickPhoto
import com.example.portaldatransparencia.interfaces.IClickParlamentar
import com.example.portaldatransparencia.interfaces.INotificationSenado
import com.example.portaldatransparencia.repository.ResultSenadoRequest
import com.example.portaldatransparencia.util.RetiraAcento
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.activity.gastogeral.senado.ActivityGastoGeralSenado
import com.example.portaldatransparencia.views.activity.ranking.senado.ActivityRankingSenado
import com.example.portaldatransparencia.views.activity.votacoes.senado.ActivityVotacoesSenado
import com.example.portaldatransparencia.views.camara.CamaraFragment
import com.example.portaldatransparencia.views.senado.senador.SenadorActivity
import com.example.portaldatransparencia.views.view_generics.*
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SenadoFragment: Fragment(R.layout.fragment_camara_senado), IClickParlamentar, INotificationSenado, IClickPhoto {

    private var binding: FragmentCamaraSenadoBinding? = null
    private val senadoViewModel: SenadoViewModel by viewModel()
    private val modifyChip: ModifyChip by inject()
    private val retiraAcento: RetiraAcento by inject()
    private val hideView: EnableDisableView by inject()
    private val animeView: AnimationView by inject()
    private val verifyInternet: ValidationInternet by inject()
    private val dialogClass: CreateDialogClass by inject()
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

    private fun validationInternet(text: Int){
        binding?.run {
            layoutValidation.run {
                hideView.enableView(frameValidation)
                hideView.enableView(verifiqueInternet)
                hideView.disableView(progressMain)
                verifiqueInternet.text = getString(text)
                buttonAgain.setOnClickListener {
                    animaView(it)
                    progressMain.smoothToHide()
                    hideView.disableView(frameValidation)
                    observer()
                }
            }
        }
    }

    private fun recycler() {
        val recycler = binding?.recyclerDeputados
        adapter = SenadoAdapter(this, this, this, AnimatorViewParlamentar())
        recycler?.layoutManager = LinearLayoutManager(context)
        recycler?.adapter = adapter
    }

    private fun observer(){

        val internet = verifyInternet.validationInternet(requireContext().applicationContext)
        if (internet){
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
                            validationInternet(R.string.sem_info_api)
                        }
                        is ResultSenadoRequest.ErrorConnection -> {
                            validationInternet(R.string.api_nao_respondeu)
                        }
                    }
                }
            }
        }
        else validationInternet(R.string.verifique_sua_internet)
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
                chipPv.setOnClickListener { modifyChipPartido(chipPv) }
                chipDem.setOnClickListener { modifyChipPartido(chipDem) }
                chipMdb.setOnClickListener { modifyChipPartido(chipMdb) }
                chipNovo.setOnClickListener { modifyChipPartido(chipNovo) }
                chipPatri.setOnClickListener { modifyChipPartido(chipPatri) }
                chipPatriota.setOnClickListener { modifyChipPartido(chipPatriota) }
                chipPsol.setOnClickListener { modifyChipPartido(chipPsol) }
                chipPcdob.setOnClickListener { modifyChipPartido(chipPcdob) }
                chipPsd.setOnClickListener { modifyChipPartido(chipPsd) }
                chipPdt.setOnClickListener { modifyChipPartido(chipPdt) }
                chipPhs.setOnClickListener { modifyChipPartido(chipPhs) }
                chipPl.setOnClickListener { modifyChipPartido(chipPl) }
                chipPros.setOnClickListener { modifyChipPartido(chipPros) }
                chipPsc.setOnClickListener { modifyChipPartido(chipPsc) }
                chipPsb.setOnClickListener { modifyChipPartido(chipPsb) }
                chipPsdb.setOnClickListener { modifyChipPartido(chipPsdb) }
                chipSolidariedade.setOnClickListener { modifyChipPartido(chipSolidariedade) }
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
                animaView(it)
                permissionVoice()
            }
            icFilter.setOnClickListener {
                animaView(it)
                showFilterIcons()
            }
            floatingController.setOnClickListener {
                recyclerDeputados.smoothScrollToPosition(0)
                visibilityNavViewAndFloating
                    .visibilityNavViewAndFloating(
                        requireContext().applicationContext, true, floatingController, animeView)
            }
            layoutItemParlamento.run {
                constraintLayout1.setOnClickListener {
                    val intent = Intent(requireContext().applicationContext, ActivityGastoGeralSenado::class.java)
                    intentStarActivity(it, intent)
                }
                constraintLayout2.setOnClickListener {
                    val intent = Intent(requireContext().applicationContext, ActivityRankingSenado::class.java)
                    intentStarActivity(it, intent)
                }
                constraintLayout3.setOnClickListener {
                    val intent = Intent(requireContext().applicationContext, ActivityVotacoesSenado::class.java)
                    intentStarActivity(it, intent)
                }
            }
        }
    }

    private fun showFilterIcons(){
        binding?.run {
            if (hideFilter) modifyIcon(R.drawable.ic_no_filter, value1 = false)
            else modifyIcon(R.drawable.ic_filter, value1 = true)
        }
    }

    private fun modifyIcon(res: Int, value1: Boolean){
        binding?.run {
            icFilter.setImageResource(res)
            hideFilter = value1
            binding?.frameChip?.apply {
                animeView.crossFade(this)
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

    override fun clickParlamentar(id: String, nome: String) {
        val name = retiraAcento.deleteAccent(nome)
        val intent = Intent(context, SenadorActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("nome", name)
        startActivity(intent)
    }

    private fun showTabView() {
        binding?.run {
            visibilityNavViewAndFloating
                .showTabView(appbar, requireContext(), floatingController, animeView)
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

    private fun animaView(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click))
    }

    private fun intentStarActivity(view: View, intent: Intent){
        animaView(view)
        startActivity(intent)
    }

    override fun clickPhoto(photo: String) {
        val dialog = dialogClass.createDialog(requireContext())
        val inflate = layoutInflater.inflate(R.layout.layout_dialog_photo, null)
        val image = inflate.findViewById<ImageView>(R.id.dialog_photo)
        Glide.with(requireContext())
            .load(photo)
            .into(image)
        dialog.setView(inflate)
        dialog.create()
        dialog.show()
    }
}