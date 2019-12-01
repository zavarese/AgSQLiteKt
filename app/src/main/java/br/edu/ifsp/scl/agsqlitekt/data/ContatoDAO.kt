package br.edu.ifsp.scl.agsqlitekt.data

import br.edu.ifsp.scl.agsqlitekt.model.Contato
import java.nio.file.Files.delete
import android.provider.SyncStateContract.Helpers.update
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase



class ContatoDAO(context: Context) {
    var dbHelper: SQLiteHelper = SQLiteHelper(context,SQLiteHelper.DATABASE_NAME,null,1)
    //var database = dbHelper.readableDatabase

    fun listaContatos(): ArrayList<Contato> {
        var database = dbHelper.readableDatabase
        val c = Contato(1,"","","")
        val contatos =  ArrayList<Contato>()

        val cursor: Cursor

        cursor = database.query(
            SQLiteHelper.TABLE_NAME,
            null, null, null, null, null,
            SQLiteHelper.KEY_NOME
        )

        while (cursor.moveToNext()) {
            val c = Contato(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3))

            contatos.add(c)
        }

        cursor.close()
        database.close()

        return contatos
    }


    fun incluirContato(c: Contato): Long {

        var database = dbHelper.writableDatabase

        val values = ContentValues()
        values.put(SQLiteHelper.KEY_NOME, c.nome.toUpperCase())
        values.put(SQLiteHelper.KEY_FONE, c.fone)
        values.put(SQLiteHelper.KEY_EMAIL, c.email)

        val id = database.insert(SQLiteHelper.TABLE_NAME, null, values)

        database.close()
        return id

    }

    fun alterarContato(c: Contato) {
        var database = dbHelper.writableDatabase

        val values = ContentValues()
        values.put(SQLiteHelper.KEY_NOME, c.nome)
        values.put(SQLiteHelper.KEY_FONE, c.fone)
        values.put(SQLiteHelper.KEY_EMAIL, c.email)

        database.update(
            SQLiteHelper.TABLE_NAME, values,
            SQLiteHelper.KEY_ID + "=" + c.id, null
        )

        database.close()
    }

    fun excluirContato(c: Contato) {
        var database = dbHelper.writableDatabase

        database.delete(
            SQLiteHelper.TABLE_NAME,
            SQLiteHelper.KEY_ID + "=" + c.id, null
        )

        database.close()

    }
}