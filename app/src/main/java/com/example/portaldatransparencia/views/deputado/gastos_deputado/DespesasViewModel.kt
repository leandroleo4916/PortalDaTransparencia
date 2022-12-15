package com.example.portaldatransparencia.views.deputado.gastos_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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
        var manutencao = 0.0F
        var combustivel = 0.0F
        var divulgacao = 0.0F
        var passagens = 0.0F
        var telefonia = 0.0F
        var postais = 0.0F
        var alimentacao = 0.0F
        var hospedagem = 0.0F
        var taxi = 0.0F
        var locacao = 0.0F
        var consultoria = 0.0F
        var outros = 0.0F
        val listType = mutableListOf<String>()

        dados.forEach {
            val valor = formatFloat.formatFloat(it.valorDocumento.toString())
            when (it.tipoDespesa) {

                "MANUTENÇÃO DE ESCRITÓRIO DE APOIO À ATIVIDADE PARLAMENTAR" -> manutencao += valor
                "COMBUSTÍVEIS E LUBRIFICANTES." -> combustivel += valor
                "DIVULGAÇÃO DA ATIVIDADE PARLAMENTAR." -> divulgacao += valor
                "ASSINATURA DE PUBLICAÇÕES" -> divulgacao += valor
                "PASSAGEM AÉREA - REEMBOLSO" -> passagens += valor
                "PASSAGEM AÉREA - SIGEPA" -> passagens += valor
                "PASSAGEM AÉREA - RPA" -> passagens += valor
                "PASSAGENS TERRESTRES, MARÍTIMAS OU FLUVIAIS" -> passagens += valor
                "TELEFONIA" -> telefonia += valor
                "SERVIÇOS POSTAIS" -> postais += valor
                "FORNECIMENTO DE ALIMENTAÇÃO DO PARLAMENTAR" -> alimentacao += valor
                "HOSPEDAGEM ,EXCETO DO PARLAMENTAR NO DISTRITO FEDERAL." -> hospedagem += valor
                "SERVIÇO DE TÁXI, PEDÁGIO E ESTACIONAMENTO" -> taxi += valor
                "LOCAÇÃO OU FRETAMENTO DE VEÍCULOS AUTOMOTORES" -> locacao += valor
                "CONSULTORIAS, PESQUISAS E TRABALHOS TÉCNICOS." -> consultoria += valor
                else -> outros += valor
            }
            if (!listType.contains(it.tipoDespesa)){
                listType.add(it.tipoDespesa)
            }
        }

        if (manutencao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    manutencao.toInt(), "Manutenção escritório",
                    "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg"
                )
            )
        }
        if (combustivel.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    combustivel.toInt(), "Combustíveis",
                    "https://cdn-icons-png.flaticon.com/512/2311/2311324.png"
                )
            )
        }
        if (divulgacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    divulgacao.toInt(), "Divulgação parlamentar",
                    "https://cdn-icons-png.flaticon.com/512/6520/6520327.png"
                )
            )
        }
        if (passagens.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    passagens.toInt(), "Passagens aéreas",
                    "https://cdn-icons-png.flaticon.com/512/5014/5014749.png"
                )
            )
        }
        if (telefonia.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    telefonia.toInt(), "Telefonia",
                    "https://cdn-icons-png.flaticon.com/512/126/126103.png"
                )
            )
        }
        if (postais.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    postais.toInt(), "Serviços postais",
                    "https://cdn-icons-png.flaticon.com/512/4280/4280211.png"
                )
            )
        }
        if (alimentacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    alimentacao.toInt(), "Alimentação",
                    "https://cdn-icons-png.flaticon.com/512/6799/6799692.png"
                )
            )
        }
        if (hospedagem.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    hospedagem.toInt(), "Hospedagens",
                    "https://cdn-icons-png.flaticon.com/512/10/10674.png"
                )
            )
        }
        if (taxi.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    taxi.toInt(), "Taxi, pedágio, estacionamento",
                    "https://media.istockphoto.com/id/1294019348/pt/vetorial/person-catching-taxi-vector-icon.jpg?s=612x612&w=is&k=20&c=Hae45Icbu0rLVukDYxQ1iBets8taivGe-YIUJVnmD2c="
                )
            )
        }
        if (locacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    locacao.toInt(), "Locação veículos",
                    "https://cdn-icons-png.flaticon.com/512/84/84925.png?w=740&t=st=1671117756~exp=1671118356~hmac=f0f55b1b53a67ee4715c3eb6527f63761f0d82210c1e76419cb17fc2b4891958"
                )
            )
        }
        if (consultoria.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    consultoria.toInt(), "Consultorias",
                    "https://cdn-icons-png.flaticon.com/512/1522/1522778.png"
                )
            )
        }
        if (outros.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    outros.toInt(), "Outros serviços",
                    "https://cdn-icons-png.flaticon.com/512/4692/4692103.png"
                )
            )
        }
        val v = listType.size
        adapter.updateData(subList)
    }
}


