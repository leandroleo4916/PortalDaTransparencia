package com.example.portaldatransparencia.util

class CotaState {
    fun cotaState(state: String): Int {
        return when (state) {
            "AC" -> 44632
            "AL" -> 40944
            "AM" -> 43570
            "AP" -> 43374
            "BA" -> 39010
            "CE" -> 42451
            "DF" -> 30788
            "ES" -> 37423
            "GO" -> 35507
            "MA" -> 42151
            "MG" -> 36092
            "MS" -> 40542
            "MT" -> 39428
            "PA" -> 42227
            "PB" -> 42032
            "PE" -> 41676
            "PI" -> 40971
            "PR" -> 38871
            "RJ" -> 35760
            "RN" -> 42732
            "RO" -> 43672
            "RR" -> 45612
            "RS" -> 40875
            "SC" -> 39878
            "SE" -> 40139
            "SP" -> 37042
            "TO" -> 39503
            else -> 0
        }
    }
}