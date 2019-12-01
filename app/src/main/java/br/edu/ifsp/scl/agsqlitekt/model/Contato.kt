package br.edu.ifsp.scl.agsqlitekt.model

import java.io.Serializable

class Contato(
    var id: Int,
    var nome: String,
    var fone: String,
    var email: String
): Serializable {

    override fun equals(obj: Any?): Boolean {
        val c2 = obj as Contato?
        return if (this.id == c2!!.id)
            true
        else
            false

    }


}