package br.edu.ifsp.scl.agsqlitekt.utils

enum class Operacao(val text: String) {
    Unica("Unica"), Diaria("Diaria"), Quinzenal ("Quinzenal"), Mensal ("Mensal"), Anual ("Anual")
}