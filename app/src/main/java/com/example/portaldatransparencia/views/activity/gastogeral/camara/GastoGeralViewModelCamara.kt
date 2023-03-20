package com.example.portaldatransparencia.views.activity.gastogeral.camara

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.GastoSetorAdapter
import com.example.portaldatransparencia.dataclass.AddInfoSetor
import com.example.portaldatransparencia.dataclass.GastoGeralCamara
import com.example.portaldatransparencia.repository.GastoGeralRepository
import com.example.portaldatransparencia.repository.ResultGastoGeralCamara

class GastoGeralViewModelCamara(private val repository: GastoGeralRepository): ViewModel() {

    fun gastoGeralCamara(ano: String):
            LiveData<ResultGastoGeralCamara<GastoGeralCamara?>> = repository.gastoGeralCamara(ano)

    @SuppressLint("ResourceType")
    fun buildGraphCamara (gastoCamara: GastoGeralCamara,
                          adapter: GastoSetorAdapter,
                          context: Context) {
        
        val infoSetor: ArrayList<AddInfoSetor> = arrayListOf()
        gastoCamara.total.run {
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.manutencao_escritorio), this.manutencao,
                R.drawable.back_1, context.getString(R.color.color1),
                "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.combustivel_lubrificante), this.combustivel,
                R.drawable.back_2, context.getString(R.color.color2),
                "https://cdn-icons-png.flaticon.com/512/2311/2311324.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.passagens_aereas), this.passagens,
                R.drawable.back_3, context.getString(R.color.color3),
                "https://cdn-icons-png.flaticon.com/512/5014/5014749.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.assinaturas), this.assinatura,
                R.drawable.back_4, context.getString(R.color.color4),
                "https://cdn-icons-png.flaticon.com/512/1466/1466339.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.divulgacao_parlamentar), this.divulgacao,
                R.drawable.back_5, context.getString(R.color.color5),
                "https://cdn-icons-png.flaticon.com/512/6520/6520327.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.servicos_telefonicos), this.telefonia,
                R.drawable.back_6, context.getString(R.color.color6),
                "https://cdn-icons-png.flaticon.com/512/126/126103.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.servico_postal), this.postais,
                R.drawable.back_7, context.getString(R.color.color7),
                "https://cdn-icons-png.flaticon.com/512/4280/4280211.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.hospedagem), this.hospedagem,
                R.drawable.back_8, context.getString(R.color.color8),
                "https://cdn-icons-png.flaticon.com/512/10/10674.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.taxi), this.taxi,
                R.drawable.back_9, context.getString(R.color.color9),
                "https://media.istockphoto.com/id/1294019348/pt/vetorial/person-catching-taxi-vector-icon.jpg?s=612x612&w=is&k=20&c=Hae45Icbu0rLVukDYxQ1iBets8taivGe-YIUJVnmD2c=")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.locacao), this.locacao,
                R.drawable.back_10, context.getString(R.color.color10),
                "https://cdn-icons-png.flaticon.com/512/84/84925.png?w=740&t=st=1671117756~exp=1671118356~hmac=f0f55b1b53a67ee4715c3eb6527f63761f0d82210c1e76419cb17fc2b4891958")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.consultoriaAcessoria), this.consultoria,
                R.drawable.back_11, context.getString(R.color.color11),
                "https://cdn-icons-png.flaticon.com/512/1522/1522778.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.seguranca), this.seguranca,
                R.drawable.back_12, context.getString(R.color.color12),
                "https://cdn-icons-png.flaticon.com/512/4185/4185148.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.curso), this.curso,
                R.drawable.back_13, context.getString(R.color.color13),
                "https://cdn-icons-png.flaticon.com/512/1323/1323490.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.alimentacao), this.alimentacao,
                R.drawable.back_14, context.getString(R.color.color14),
                "https://cdn-icons-png.flaticon.com/512/6799/6799692.png")
            )
            infoSetor.add(
                AddInfoSetor(context.getString(R.string.outros_servicos), this.outros,
                R.drawable.back_15, context.getString(R.color.color15),
                "https://cdn-icons-png.flaticon.com/512/4692/4692103.png")
            )
        }
        adapter.updateData(infoSetor)
    }
}

