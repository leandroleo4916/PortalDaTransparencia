package com.example.portaldatransparencia.views.view_generics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.portaldatransparencia.views.camara.CamaraFragment
import com.example.portaldatransparencia.views.senado.SenadoFragment

class TabViewAdapterController(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(CamaraFragment(), SenadoFragment())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position] as Fragment
    }
}