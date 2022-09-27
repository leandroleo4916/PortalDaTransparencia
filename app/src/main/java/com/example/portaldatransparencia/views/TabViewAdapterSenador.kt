package com.example.portaldatransparencia.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.senador.gastos_senador.FragmentGastosSenador
import com.example.portaldatransparencia.views.senador.geral_senador.FragmentGeralSenador
import com.example.portaldatransparencia.views.deputado.proposta_deputado.FragmentProposta

class TabViewAdapterSenador(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(
        FragmentGeralSenador(), FragmentGastosSenador(), FragmentProposta()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}