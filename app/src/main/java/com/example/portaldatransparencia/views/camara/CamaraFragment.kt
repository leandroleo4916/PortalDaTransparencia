package com.example.portaldatransparencia.views.camara

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.MainAdapter
import com.example.portaldatransparencia.databinding.FragmentCamaraSenadoBinding
import com.example.portaldatransparencia.interfaces.IClickDeputado
import com.example.portaldatransparencia.interfaces.INotification
import com.example.portaldatransparencia.remote.ResultRequest
import com.example.portaldatransparencia.util.ValidationInternet
import com.example.portaldatransparencia.views.view_generics.EnableDisableView
import com.example.portaldatransparencia.views.view_generics.ModifyChip
import com.example.portaldatransparencia.views.view_generics.VisibilityNavViewAndFloating
import com.example.portaldatransparencia.views.deputado.DeputadoActivity
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CamaraFragment: Fragment(R.layout.fragment_camara_senado), IClickDeputado, INotification {

    private var binding: FragmentCamaraSenadoBinding? = null
    private val mainViewModel: CamaraViewModel by viewModel()
    private val hideView: EnableDisableView by inject()
    private val modifyChip: ModifyChip by inject()
    private val validationInternet: ValidationInternet by inject()
    private val visibilityNavViewAndFloating: VisibilityNavViewAndFloating by inject()
    private lateinit var adapter: MainAdapter
    private var chipEnabled: Chip? = null
    private val permissionCode = 1000
    companion object { const val SPEECH_REQUEST_CODE = 0 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCamaraSenadoBinding.bind(view)

        recycler()
        observer()
        search()
        listener()
        showTabView()
    }

    private fun recycler() {
        val recycler = binding?.recyclerDeputados
        adapter = MainAdapter(this, this)
        recycler?.layoutManager = LinearLayoutManager(context)
        recycler?.adapter = adapter
    }

    private fun observer() {

        val internet = context?.let { validationInternet.validationInternet(it) }
        if (internet == true){
            mainViewModel.searchData(ordenarPor = "nome").observe(viewLifecycleOwner) {
                it?.let { result ->
                    when (result) {
                        is ResultRequest.Success -> {
                            result.dado?.let { deputados ->
                                binding?.run {
                                    hideView.enableView(recyclerDeputados)
                                    hideView.disableView(progressMain)
                                    hideView.disableView(frameValidation)
                                }
                                adapter.updateData(deputados.dados)
                            }
                        }
                        is ResultRequest.Error -> {
                            result.exception.message?.let {
                                showValidationInternet(R.string.nao_recebeu_dados)
                            }
                        }
                        is ResultRequest.ErrorConnection -> {
                            result.exception.message?.let {
                                showValidationInternet(R.string.falha_no_servidor)
                            }
                        }
                    }
                }
            }
        }
        else {
            showValidationInternet(R.string.verifique_sua_internet)
        }
    }

    private fun showValidationInternet(value: Int){
        binding?.run {
            hideView.disableView(progressMain)
            hideView.disableView(recyclerDeputados)
            hideView.enableView(frameValidation)
            layoutValidation.verifiqueInternet.text = getString(value)
        }
    }

    private fun search() {
        binding?.textSearch?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun clickDeputado(id: String) {
        val intent = Intent(context, DeputadoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun listener(){
        binding?.run {
            chipGroupItem.run {
                chipAvante.setOnClickListener { modify(chipEnabled, chipAvante) }
                chipCidadania.setOnClickListener { modify(chipEnabled, chipCidadania) }
                chipDc.setOnClickListener { modify(chipEnabled, chipDc) }
                chipDem.setOnClickListener { modify(chipEnabled, chipDem) }
                chipMdb.setOnClickListener { modify(chipEnabled, chipMdb) }
                chipNovo.setOnClickListener { modify(chipEnabled, chipNovo) }
                chipPatri.setOnClickListener { modify(chipEnabled, chipPatri) }
                chipPatriota.setOnClickListener { modify(chipEnabled, chipPatriota) }
                chipPcb.setOnClickListener { modify(chipEnabled, chipPcb) }
                chipPcdob.setOnClickListener { modify(chipEnabled, chipPcdob) }
                chipPco.setOnClickListener { modify(chipEnabled, chipPco) }
                chipPdt.setOnClickListener { modify(chipEnabled, chipPdt) }
                chipPhs.setOnClickListener { modify(chipEnabled, chipPhs) }
                chipPl.setOnClickListener { modify(chipEnabled, chipPl) }
                chipPros.setOnClickListener { modify(chipEnabled, chipPros) }
                chipPsc.setOnClickListener { modify(chipEnabled, chipPsc) }
                chipPmb.setOnClickListener { modify(chipEnabled, chipPmb) }
                chipPodemos.setOnClickListener { modify(chipEnabled, chipPodemos) }
                chipPp.setOnClickListener { modify(chipEnabled, chipPp) }
                chipPt.setOnClickListener { modify(chipEnabled, chipPt) }
                chipRepublicanos.setOnClickListener { modify(chipEnabled, chipRepublicanos) }
                chipUniao.setOnClickListener { modify(chipEnabled, chipUniao) }
            }

            icVoz.setOnClickListener { permissionVoice() }
            floatingController.setOnClickListener {
                recyclerDeputados.smoothScrollToPosition(0)
                context?.let { it1 ->
                    visibilityNavViewAndFloating.visibilityNavViewAndFloating(it1, true,
                        floatingController
                    )}
            }
            layoutValidation.buttonAgain.setOnClickListener {
                hideView.enableView(binding!!.progressMain)
                observer()
            }
        }
    }

    private fun modify(viewEnabled: Chip?, viewDisabled: Chip) {
        chipEnabled = modifyChip.modify(viewEnabled, viewDisabled)
        if (!viewDisabled.isChecked) adapter.filter.filter("")
        else adapter.filter.filter(viewDisabled.text as String)
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
                } else {
                    Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openVoice() {

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }
            if (intent.`package` == null) {
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            } else {
                Toast.makeText(context, "Erro na captura de voz",
                    Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) { }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            binding?.textSearch?.setText(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
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
        Toast.makeText(context, "Não encontrado deputado com esse partido",
            Toast.LENGTH_SHORT).show()
    }
}