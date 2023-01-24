package com.example.portaldatransparencia.interfaces

import com.example.portaldatransparencia.dataclass.VotoParlamentar

interface IAddVotoInRecycler {
    fun addVoto(list: List<VotoParlamentar>, descricao: String?)
}