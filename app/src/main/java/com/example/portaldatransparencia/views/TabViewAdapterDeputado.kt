package com.example.portaldatransparencia.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.deputado.frente_deputado.FragmentFrente
import com.example.portaldatransparencia.views.deputado.gastos_deputado.FragmentGastos
import com.example.portaldatransparencia.views.deputado.geral_deputado.FragmentGeralDeputado
import com.example.portaldatransparencia.views.deputado.proposta_deputado.FragmentProposta

class TabViewAdapterDeputado(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(
        FragmentGeralDeputado(), FragmentGastos(), FragmentProposta(), FragmentFrente()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}