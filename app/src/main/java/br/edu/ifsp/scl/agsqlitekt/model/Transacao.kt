package br.edu.ifsp.scl.agsqlitekt.model


import br.edu.ifsp.scl.agsqlitekt.utils.Operacao
import br.edu.ifsp.scl.agsqlitekt.utils.Status
import br.edu.ifsp.scl.agsqlitekt.utils.Tipo
import java.io.Serializable
import java.sql.Timestamp

class Transacao(
    val id: Int,
    val contaSacado: Conta,
    val contaRecebedor: Conta? = null,
    var tipo: Tipo,
    var operacao: Operacao,
    var classificacao: String,
    var descricao: String,
    var dataHora: String,
    var valor: Double,
    var status: Status,
    var erro: String,
    var data: String = dataHora.substring(6,9)+dataHora.substring(3,4)+dataHora.substring(0,1)) : Serializable