package br.edu.ifsp.scl.agsqlitekt.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import br.edu.ifsp.scl.agsqlitekt.model.Classificacao

class ClassificacaoDAO(context: Context) {

    private val dbhelper: SQLiteHelper = SQLiteHelper(context,SQLiteHelper.DATABASE_NAME,null,1)

    object ClassificacaoEntry : BaseColumns {
        const val TABLE_NAME = "classificacao"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NOME = "cla_nome"
    }

    companion object {
        fun deleteTable() = "DROP TABLE IF EXISTS ${ClassificacaoEntry.TABLE_NAME}"
        fun creteTable() =
            "CREATE TABLE ${ClassificacaoEntry.TABLE_NAME} (" +
                    "${ClassificacaoEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${ClassificacaoEntry.COLUMN_NOME} TEXT)"
    }

    fun listaClassificacao(): List<Classificacao> {
        val database = dbhelper.readableDatabase
        val classificacaoList = ArrayList<Classificacao>()

        val cursor: Cursor

        cursor = database.query(
            ClassificacaoEntry.TABLE_NAME,
            null, null, null, null, null,
            ClassificacaoEntry.COLUMN_NOME
        )
        while (cursor.moveToNext()) {
            val classificacao = Classificacao(cursor.getInt(cursor.getColumnIndex(ClassificacaoEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ClassificacaoEntry.COLUMN_NOME)))
            classificacaoList.add(classificacao)
        }

        cursor.close()
        database.close()

        return classificacaoList
    }

    fun getClassificacao(id: Int): Classificacao? {
        val database = dbhelper.readableDatabase
        var classificacao: Classificacao? = null

        val cursor: Cursor

        cursor = database.query(
            ClassificacaoEntry.TABLE_NAME,
            null,
            ClassificacaoEntry.COLUMN_ID+"="+id+"",
            null, null, null,null
        )
        if (cursor.count > 0) {
            classificacao = Classificacao(cursor.getInt(cursor.getColumnIndex(ClassificacaoEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ClassificacaoEntry.COLUMN_NOME)))
        }

        cursor.close()
        database.close()

        return classificacao
    }

    fun incluir(classificacao: Classificacao){
        val database = dbhelper.writableDatabase
        val values = ContentValues()
        values.put(ClassificacaoEntry.COLUMN_NOME, classificacao.nome)
        database.insert(ClassificacaoEntry.TABLE_NAME, null, values)
        database.close()
    }

    fun atualizar(classificacao: Classificacao){
        val database = dbhelper.writableDatabase
        val values = ContentValues()
        values.put(ClassificacaoEntry.COLUMN_NOME, classificacao.nome)
        database.update(ClassificacaoEntry.TABLE_NAME, values,
            ClassificacaoEntry.COLUMN_ID + "=?",
            arrayOf(classificacao.id.toString()))
        database.close()
    }

    fun excluir(classificacao: Classificacao) {
        val database = dbhelper.writableDatabase
        database.delete(ClassificacaoEntry.TABLE_NAME,
            ClassificacaoEntry.COLUMN_ID + "=?",
            arrayOf(classificacao.id.toString()))
        database.close()
    }
}