package com.example.portaldatransparencia.views

import com.google.android.material.chip.Chip

class ModifyChip {

    fun modify(viewEnabled: Chip?, viewDisabled: Chip): Chip? {
        return if (viewEnabled != null){
            viewEnabled.isChecked = false
            viewDisabled.isChecked = true

            if (viewEnabled != viewDisabled) viewDisabled
            else { viewDisabled.isChecked = false
                null
            }
        } else {
            viewDisabled.isChecked = true
            viewDisabled
        }
    }
}