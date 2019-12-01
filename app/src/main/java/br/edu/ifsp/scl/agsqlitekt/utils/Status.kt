package br.edu.ifsp.scl.agsqlitekt.utils

enum class Status(val text: String) {
    Efetivado("Efetivado"), Agendado("Agendado"), NaoEfetivado("Não efetivado")
}