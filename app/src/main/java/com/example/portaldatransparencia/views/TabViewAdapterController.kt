package com.example.portaldatransparencia.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.gastos.FragmentGastos
import com.example.portaldatransparencia.views.camara.CamaraFragment
import com.example.portaldatransparencia.views.proposta.FragmentProposta
import com.example.portaldatransparencia.views.senado.SenadoFragment

class TabViewAdapterController(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(
        CamaraFragment(), SenadoFragment(), FragmentProposta()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}