package com.example.portaldatransparencia.views.activity.gastogeral.senado

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.adapter.GraphGastoAdapter
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.dataclass.GastoGeralDataClass
import com.example.portaldatransparencia.dataclass.GastoGeralSenadoData
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralSenado

class GastoGeralViewModelSenado(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeralSenado(ano: String):
            LiveData<ResultGastoGeralSenado<GastoGeralSenadoData?>> = repository.gastoGeralSenado(ano)

    @SuppressLint("ResourceType")
    fun buildGraphCamara (gastoSenado: GastoGeralSenadoData,
                          adapter: GastoSetorAdapter,
                          adapterGraph: GraphGastoAdapter,
                          ano: String, context: Context) {

        val infoSetor: ArrayList<AddInfoSetor> = arrayListOf()
        gastoSenado.gastoGeral.run {
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.alugueDeImoveis), this.aluguel,
                    R.drawable.back_1, context.getString(R.color.color1),
                    "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.divulgacao_parlamentar), this.divulgacao,
                    R.drawable.back_2, context.getString(R.color.color2),
                    "https://cdn-icons-png.flaticon.com/512/6520/6520327.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.passagens_aereas), this.passagens,
                    R.drawable.back_3, context.getString(R.color.color3),
                    "https://cdn-icons-png.flaticon.com/512/5014/5014749.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.consultoriaAcessoria), this.contratacao,
                    R.drawable.back_4, context.getString(R.color.color4),
                    "https://cdn-icons-png.flaticon.com/512/1522/1522778.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.locomocaoHospedagemAlimentacao), this.locomocao,
                    R.drawable.back_5, context.getString(R.color.color5),
                    "https://cdn-icons-png.flaticon.com/512/6799/6799692.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.aquisicaoDeMateriais), this.aquisicao,
                    R.drawable.back_6, context.getString(R.color.color6),
                    "https://cdn-icons-png.flaticon.com/512/6169/6169675.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.outros_servicos), this.outros,
                    R.drawable.back_15, context.getString(R.color.color15),
                    "https://cdn-icons-png.flaticon.com/512/4692/4692103.png")
            )
        }
        adapter.updateData(infoSetor)
        adapterGraph.updateData(infoSetor, ano)
    }

}

