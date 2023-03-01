package com.example.portaldatransparencia.views.camara.deputado.gastos_deputado

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.portaldatransparencia.adapter.DimensionAdapter
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.example.portaldatransparencia.dataclass.SenadorGastosDataClass
import com.example.portaldatransparencia.dataclass.SublistDataClass
import com.example.portaldatransparencia.repository.IdDespesasRepository
import com.example.portaldatransparencia.repository.ResultCotaRequest
import com.example.portaldatransparencia.util.FormatValueFloat

class DespesasViewModel(private val repository: IdDespesasRepository,
                        private val formatFloat: FormatValueFloat) : ViewModel() {

    fun searchGastosSenador(ano: String, nome: String):
            LiveData<ResultCotaRequest<SenadorGastosDataClass?>> =
        repository.gastosData(ano, nome)

    fun captureDataNotes(dados: List<DadoDespesas>, adapter: DimensionAdapter) {

        val subList: ArrayList<SublistDataClass> = arrayListOf()
        val size = dados.size
        var total = 0.0
        var manutencao = 0.0
        var combustivel = 0.0
        var divulgacao = 0.0
        var passagens = 0.0
        var telefonia = 0.0
        var postais = 0.0
        var alimentacao = 0.0
        var hospedagem = 0.0
        var taxi = 0.0
        var locacao = 0.0
        var consultoria = 0.0
        var seguranca = 0.0
        var curso = 0.0
        var outros = 0.0

        dados.forEach {
            val valor = it.valorDocumento
            if (valor != 0.0) {
                total += valor
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
                    "SERVIÇO DE SEGURANÇA PRESTADO POR EMPRESA ESPECIALIZADA." -> seguranca += valor
                    "PARTICIPAÇÃO EM CURSO, PALESTRA OU EVENTO SIMILAR" -> curso += valor
                    else -> outros += valor
                }
            }

        }

        if (total.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    total.toInt(), "$size notas",
                    "https://cdn-icons-png.flaticon.com/512/116/116638.png",
                    "TOTAL GERAL"
                )
            )
        }
        if (manutencao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    manutencao.toInt(), "Manutenção escritório",
                    "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg",
                    "MANUTENÇÃO DE ESCRITÓRIO DE APOIO"
                )
            )
        }
        if (combustivel.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    combustivel.toInt(), "Combustíveis",
                    "https://cdn-icons-png.flaticon.com/512/2311/2311324.png",
                    "COMBUSTÍVEIS E LUBRIFICANTES."
                )
            )
        }
        if (divulgacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    divulgacao.toInt(), "Divulgação parlamentar",
                    "https://cdn-icons-png.flaticon.com/512/6520/6520327.png",
                    "DIVULGAÇÃO DA ATIVIDADE PARLAMENTAR."
                )
            )
        }
        if (passagens.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    passagens.toInt(), "Passagens aéreas",
                    "https://cdn-icons-png.flaticon.com/512/5014/5014749.png",
                    "PASSAGEM AÉREA"
                )
            )
        }
        if (telefonia.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    telefonia.toInt(), "Telefonia",
                    "https://cdn-icons-png.flaticon.com/512/126/126103.png",
                    "TELEFONIA"
                )
            )
        }
        if (postais.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    postais.toInt(), "Serviços postais",
                    "https://cdn-icons-png.flaticon.com/512/4280/4280211.png",
                    "SERVIÇOS POSTAIS"
                )
            )
        }
        if (alimentacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    alimentacao.toInt(), "Alimentação",
                    "https://cdn-icons-png.flaticon.com/512/6799/6799692.png",
                    "FORNECIMENTO DE ALIMENTAÇÃO"
                )
            )
        }
        if (hospedagem.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    hospedagem.toInt(), "Hospedagens",
                    "https://cdn-icons-png.flaticon.com/512/10/10674.png",
                    "HOSPEDAGEM"
                )
            )
        }
        if (taxi.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    taxi.toInt(), "Taxi, pedágio",
                    "https://media.istockphoto.com/id/1294019348/pt/vetorial/person-catching-taxi-vector-icon.jpg?s=612x612&w=is&k=20&c=Hae45Icbu0rLVukDYxQ1iBets8taivGe-YIUJVnmD2c=",
                    "SERVIÇO DE TÁXI"
                )
            )
        }
        if (locacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    locacao.toInt(), "Locação veículos",
                    "https://cdn-icons-png.flaticon.com/512/84/84925.png?w=740&t=st=1671117756~exp=1671118356~hmac=f0f55b1b53a67ee4715c3eb6527f63761f0d82210c1e76419cb17fc2b4891958",
                    "LOCAÇÃO OU FRETAMENTO"
                )
            )
        }
        if (consultoria.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    consultoria.toInt(), "Consultorias",
                    "https://cdn-icons-png.flaticon.com/512/1522/1522778.png",
                    "CONSULTORIAS"
                )
            )
        }
        if (seguranca.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    seguranca.toInt(), "Serviço de Segurança",
                    "https://cdn-icons-png.flaticon.com/512/4185/4185148.png",
                    "SERVIÇO DE SEGURANÇA"
                )
            )
        }
        if (curso.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    curso.toInt(), "Curso, palestra",
                    "https://cdn-icons-png.flaticon.com/512/1323/1323490.png",
                    "PARTICIPAÇÃO EM CURSO"
                )
            )
        }
        if (outros.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    outros.toInt(), "Outros serviços",
                    "https://cdn-icons-png.flaticon.com/512/4692/4692103.png",
                    "OUTROS SERVIÇOS"
                )
            )
        }
        adapter.updateData(subList)
    }
}


