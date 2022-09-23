package com.example.portaldatransparencia.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.gastos.FragmentGastos
import com.example.portaldatransparencia.views.main.MainFragment
import com.example.portaldatransparencia.views.proposta.FragmentProposta

class TabViewAdapterController(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(
        MainFragment(), FragmentGastos(), FragmentProposta()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}