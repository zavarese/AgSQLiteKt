package br.edu.ifsp.scl.agsqlitekt.utils

class Modulo {
    companion object {
        fun modulo(num: Double): Double {
            var c = 0
            var numMod: Double

            if (num < 0) {
                numMod = num * -1
            } else {
                numMod = num
            }

            return numMod
        }
    }
}