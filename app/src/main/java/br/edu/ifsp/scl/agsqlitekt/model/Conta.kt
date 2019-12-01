package br.edu.ifsp.scl.agsqlitekt.model

import java.io.Serializable

class Conta(
    var id: Int,
    var descricao: String,
    var saldoInicial: Double,
    var saldoFinal: Double = saldoInicial) : Serializable {

    fun somarAoSaldo(valor: Double) {
        this.saldoFinal += valor
    }
    fun subtrairDoSaldo(valor: Double){
        this.saldoFinal -= valor
    }

    override fun equals(obj: Any?): Boolean {
        val c2 = obj as Conta?
        return if (this.id == c2!!.id)
            true
        else
            false

    }
}