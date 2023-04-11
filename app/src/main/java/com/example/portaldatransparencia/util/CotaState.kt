package com.example.portaldatransparencia.util

class CotaState {
    fun cotaStateCamara(state: String): Int {
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
    fun cotaStateSenado(state: String): Int {
        return when (state) {
            "AC" -> 38854
            "AL" -> 35056
            "AM" -> 44276
            "AP" -> 42855
            "BA" -> 35416
            "CE" -> 38186
            "DF" -> 21045
            "ES" -> 33176
            "GO" -> 21045
            "MA" -> 37396
            "MG" -> 28496
            "MS" -> 32905
            "MT" -> 34934
            "PA" -> 40426
            "PB" -> 35555
            "PE" -> 36266
            "PI" -> 38834
            "PR" -> 32586
            "RJ" -> 31816
            "RN" -> 35976
            "RO" -> 34615
            "RR" -> 40724
            "RS" -> 35886
            "SC" -> 32871
            "SE" -> 41844
            "SP" -> 30226
            "TO" -> 25215
            else -> 0
        }
    }
}