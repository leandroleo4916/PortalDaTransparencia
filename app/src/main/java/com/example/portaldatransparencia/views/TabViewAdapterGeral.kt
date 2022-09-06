package com.example.portaldatransparencia.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.gastos.FragmentGastos

class TabViewAdapterGeral(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(
        FragmentGastos(), FragmentGastos(),
        FragmentGastos(), FragmentGastos()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}