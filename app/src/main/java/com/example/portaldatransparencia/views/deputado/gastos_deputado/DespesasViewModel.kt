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

    /*fun searchDespesasDeputado(id: String, ano: String, pagina: Int):
            LiveData<ResultDespesasRequest<Despesas?>> =
        repository.searchDespesasData(id, ano, pagina)*/

    fun searchGastosSenador(ano: String, nome: String):
            LiveData<ResultCotaRequest<SenadorGastosDataClass?>> =
        repository.gastosData(ano, nome)

    fun captureDataNotes(dados: List<DadoDespesas>, adapter: DimensionAdapter) {

        val subList: ArrayList<SublistDataClass> = arrayListOf()
        val size = dados.size
        var total = 0.0F
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
        var seguranca = 0.0F
        var curso = 0.0F
        var outros = 0.0F

        dados.forEach {
            val valor = formatFloat.formatFloat(it.valorDocumento.toString())
            if (valor != 0.0F) {
                total += valor
                when (it.tipoDespesa) {
                    "MANUTEN????O DE ESCRIT??RIO DE APOIO ?? ATIVIDADE PARLAMENTAR" -> manutencao += valor
                    "COMBUST??VEIS E LUBRIFICANTES." -> combustivel += valor
                    "DIVULGA????O DA ATIVIDADE PARLAMENTAR." -> divulgacao += valor
                    "ASSINATURA DE PUBLICA????ES" -> divulgacao += valor
                    "PASSAGEM A??REA - REEMBOLSO" -> passagens += valor
                    "PASSAGEM A??REA - SIGEPA" -> passagens += valor
                    "PASSAGEM A??REA - RPA" -> passagens += valor
                    "PASSAGENS TERRESTRES, MAR??TIMAS OU FLUVIAIS" -> passagens += valor
                    "TELEFONIA" -> telefonia += valor
                    "SERVI??OS POSTAIS" -> postais += valor
                    "FORNECIMENTO DE ALIMENTA????O DO PARLAMENTAR" -> alimentacao += valor
                    "HOSPEDAGEM ,EXCETO DO PARLAMENTAR NO DISTRITO FEDERAL." -> hospedagem += valor
                    "SERVI??O DE T??XI, PED??GIO E ESTACIONAMENTO" -> taxi += valor
                    "LOCA????O OU FRETAMENTO DE VE??CULOS AUTOMOTORES" -> locacao += valor
                    "CONSULTORIAS, PESQUISAS E TRABALHOS T??CNICOS." -> consultoria += valor
                    "SERVI??O DE SEGURAN??A PRESTADO POR EMPRESA ESPECIALIZADA." -> seguranca += valor
                    "PARTICIPA????O EM CURSO, PALESTRA OU EVENTO SIMILAR" -> curso += valor
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
                    manutencao.toInt(), "Manuten????o escrit??rio",
                    "https://as2.ftcdn.net/v2/jpg/01/38/80/37/1000_F_138803784_E08XLKKxkMrknHpurwaADXtRcfcpihdm.jpg",
                    "MANUTEN????O DE ESCRIT??RIO DE APOIO"
                )
            )
        }
        if (combustivel.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    combustivel.toInt(), "Combust??veis",
                    "https://cdn-icons-png.flaticon.com/512/2311/2311324.png",
                    "COMBUST??VEIS E LUBRIFICANTES."
                )
            )
        }
        if (divulgacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    divulgacao.toInt(), "Divulga????o parlamentar",
                    "https://cdn-icons-png.flaticon.com/512/6520/6520327.png",
                    "DIVULGA????O DA ATIVIDADE PARLAMENTAR."
                )
            )
        }
        if (passagens.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    passagens.toInt(), "Passagens a??reas",
                    "https://cdn-icons-png.flaticon.com/512/5014/5014749.png",
                    "PASSAGEM A??REA"
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
                    postais.toInt(), "Servi??os postais",
                    "https://cdn-icons-png.flaticon.com/512/4280/4280211.png",
                    "SERVI??OS POSTAIS"
                )
            )
        }
        if (alimentacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    alimentacao.toInt(), "Alimenta????o",
                    "https://cdn-icons-png.flaticon.com/512/6799/6799692.png",
                    "FORNECIMENTO DE ALIMENTA????O"
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
                    taxi.toInt(), "Taxi, ped??gio",
                    "https://media.istockphoto.com/id/1294019348/pt/vetorial/person-catching-taxi-vector-icon.jpg?s=612x612&w=is&k=20&c=Hae45Icbu0rLVukDYxQ1iBets8taivGe-YIUJVnmD2c=",
                    "SERVI??O DE T??XI"
                )
            )
        }
        if (locacao.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    locacao.toInt(), "Loca????o ve??culos",
                    "https://cdn-icons-png.flaticon.com/512/84/84925.png?w=740&t=st=1671117756~exp=1671118356~hmac=f0f55b1b53a67ee4715c3eb6527f63761f0d82210c1e76419cb17fc2b4891958",
                    "LOCA????O OU FRETAMENTO"
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
                    seguranca.toInt(), "Servi??o de Seguran??a",
                    "https://cdn-icons-png.flaticon.com/512/4185/4185148.png",
                    "SERVI??O DE SEGURAN??A"
                )
            )
        }
        if (curso.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    curso.toInt(), "Curso, palestra",
                    "https://cdn-icons-png.flaticon.com/512/1323/1323490.png",
                    "PARTICIPA????O EM CURSO"
                )
            )
        }
        if (outros.toInt() != 0) {
            subList.add(
                SublistDataClass(
                    outros.toInt(), "Outros servi??os",
                    "https://cdn-icons-png.flaticon.com/512/4692/4692103.png",
                    "OUTROS SERVI??OS"
                )
            )
        }
        adapter.updateData(subList)
    }
}


