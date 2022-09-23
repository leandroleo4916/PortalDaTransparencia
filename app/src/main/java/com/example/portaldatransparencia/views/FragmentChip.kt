package com.example.portaldatransparencia.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ChipGroupPartidosBinding

class FragmentChip: Fragment(R.layout.chip_group_partidos) {

    private var binding: ChipGroupPartidosBinding? = null

    companion object { fun getInstance() = FragmentChip()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChipGroupPartidosBinding.bind(view)
    }
}