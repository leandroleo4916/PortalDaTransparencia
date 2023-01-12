package com.example.portaldatransparencia.views.view_generics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.senado.senador.gastos_senador.FragmentGastosSenador
import com.example.portaldatransparencia.views.senado.senador.geral_senador.FragmentGeralSenador
import com.example.portaldatransparencia.views.senado.senador.votacoes_senador.FragmentVotacoes

class TabViewAdapterSenador(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(
        FragmentGeralSenador(), FragmentGastosSenador(), FragmentVotacoes()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}