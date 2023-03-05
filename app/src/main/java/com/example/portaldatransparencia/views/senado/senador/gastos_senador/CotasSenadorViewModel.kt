package com.example.portaldatransparencia.views.senado.senador.gastos_senador

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.dataclass.GastosSenador
import com.example.portaldatransparencia.dataclass.SenadorGastosDataClass
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.repository.IdDespesasRepository
import com.example.portaldatransparencia.repository.ResultCotaRequest
import com.example.portaldatransparencia.util.FormatValueFloat

class CotasSenadorViewModel(private val repository: IdDespesasRepository,
                            private val formatFloat: FormatValueFloat) : ViewModel() {

    fun searchGastosSenador(ano: String, nome: String):
            LiveData<ResultCotaRequest<SenadorGastosDataClass?>> = repository.gastosData(ano, nome)

    fun addCotasAdapterDimension(dados: List<GastosSenador>, adapterDimension: DimensionAdapter) {

        val subList: ArrayList<SublistDataClass> = arrayListOf()
        val size = dados.size
        var total = 0.0F
        var aluguel = 0.0F
        var divulgacao = 0.0F
        var passagens = 0.0F
        var contratacao = 0.0F
        var locomocao = 0.0F
        var aquisicao = 0.0F
        var servico = 0.0F
        var outros = 0.0F

        dados.forEach {
            val value = formatFloat.formatFloat(it.valorReembolsado)
            if (value != 0.0F){
                total += value
                when (it.tipoDespesa.substring(0,5)){
                    "Alugu" -> aluguel += value
                    "Divul" -> divulgacao += value
                    "Passa" -> passagens += value
                    "Contr" -> contratacao += value
                    "Locom" -> locomocao += value
                    "Aquis" -> aquisicao += value
                    "Servi" -> servico += value
                    else -> outros += value
                }
            }
        }
        if (total.toInt() != 0){
            subList.add(SublistDataClass(total.toInt(), "$size notas",
                "https://cdn-icons-png.flaticon.com/512/116/116638.png",
                "Total geral"))
        }
        if (divulgacao.toInt() != 0){
            subList.add(SublistDataClass(divulgacao.toInt(), "Divulgação parlamentar",
                "https://cdn-icons-png.flaticon.com/512/6520/6520327.png",
                "Divulgação"))
        }
        if (passagens.toInt() != 0){
            subList.add(SublistDataClass(passagens.toInt(), "Passagens aéreas",
                "https://cdn-icons-png.flaticon.com/512/5014/5014749.png",
                "Passagens"))
        }
        if (contratacao.toInt() != 0){
            subList.add(SublistDataClass(contratacao.toInt(), "Consultoria, assessoria",
                "https://cdn-icons-png.flaticon.com/512/1522/1522778.png",
                "Contratação"))
        }
        if (locomocao.toInt() != 0){
            subList.add(SublistDataClass(locomocao.toInt(), "Hospedagem, alimentação",
                "https://cdn-icons-png.flaticon.com/512/6799/6799692.png",
                "Locomoção"))
        }
        if (aquisicao.toInt() != 0){
            subList.add(SublistDataClass(aquisicao.toInt(), "Aquisição de materiais",
                "https://cdn-icons-png.flaticon.com/512/6169/6169675.png",
                "Aquisição"))
        }
        if (servico.toInt() != 0){
            subList.add(SublistDataClass(servico.toInt(), "Serviços postais",
                "https://cdn-icons-png.flaticon.com/512/4280/4280211.png",
                "Serviços"))
        }
        if (outros.toInt() != 0){
            subList.add(SublistDataClass(outros.toInt(), "Outros serviços",
                "https://cdn-icons-png.flaticon.com/512/4692/4692103.png",
                "Outros"))
        }
        adapterDimension.updateData(subList)
    }
}


