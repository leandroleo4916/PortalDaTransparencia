package com.example.portaldatransparencia.views.deputado.gastos_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.Despesas
import com.example.portaldatransparencia.dataclass.SenadorGastosDataClass
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.remote.IdDespesasRepository
import com.example.portaldatransparencia.remote.ResultCotaRequest
import com.example.portaldatransparencia.remote.ResultDespesasRequest
import com.example.portaldatransparencia.util.FormatValueFloat

class DespesasViewModel(private val repository: IdDespesasRepository,
                        private val formatFloat: FormatValueFloat) : ViewModel() {

    fun searchDespesasDeputado(id: String, ano: String, pagina: Int):
            LiveData<ResultDespesasRequest<Despesas?>> =
        repository.searchDespesasData(id, ano, pagina)

    fun searchGastosSenador(ano: String, nome: String):
            LiveData<ResultCotaRequest<SenadorGastosDataClass?>> = repository.gastosData(ano, nome)

    fun captureDataNotes(dados: List<DadoDespesas>, adapter: DimensionAdapter) {

        val subList: ArrayList<SublistDataClass> = arrayListOf()
        var aluguel = 0.0F
        var divulgacao = 0.0F
        var passagens = 0.0F
        var contratacao = 0.0F
        var alimentacao = 0.0F
        var aquisicao = 0.0F
        var servico = 0.0F
        var outros = 0.0F

        dados.forEach {
            val valor = formatFloat.formatFloat(it.valorDocumento.toString())
            when (it.tipoDespesa.substring(0, 5)) {

                "MANUT" -> aluguel += valor
                "COMBU" -> aquisicao += valor
                "PASSA" -> passagens += valor
                "DIVUL" -> divulgacao += valor
                "TELEF" -> contratacao += valor
                "SERVI" -> servico += valor
                "FORNE" -> alimentacao += valor
                else -> outros += valor
            }
        }
        if (aluguel.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    aluguel.toInt(), "Manutenção de escritório", R.drawable.back_7,
                    "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg"
                )
            )
        }
        if (divulgacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    divulgacao.toInt(), "Divulgação parlamentar", R.drawable.back_6,
                    "https://cdn-icons-png.flaticon.com/512/6520/6520327.png"
                )
            )
        }
        if (passagens.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    passagens.toInt(), "Passagens aéreas", R.drawable.back_5,
                    "https://cdn-icons-png.flaticon.com/512/5014/5014749.png"
                )
            )
        }
        if (contratacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    contratacao.toInt(), "Serviços telefônicos", R.drawable.back_4,
                    "https://cdn-icons-png.flaticon.com/512/1522/1522778.png"
                )
            )
        }
        if (alimentacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    alimentacao.toInt(), "Hospedagem, alimentação", R.drawable.back_3,
                    "https://cdn-icons-png.flaticon.com/512/6799/6799692.png"
                )
            )
        }
        if (aquisicao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    aquisicao.toInt(), "Combustíveis e lubrificantes", R.drawable.back_2,
                    "https://cdn-icons-png.flaticon.com/512/2311/2311324.png"
                )
            )
        }
        if (servico.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    servico.toInt(), "Serviços postais, correios", R.drawable.back_1,
                    "https://cdn-icons-png.flaticon.com/512/4280/4280211.png"
                )
            )
        }
        if (outros.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    outros.toInt(), "Outros serviços e produtos", R.drawable.back_7,
                    "https://cdn-icons-png.flaticon.com/512/4692/4692103.png"
                )
            )
        }
        adapter.updateData(subList)
    }
}


