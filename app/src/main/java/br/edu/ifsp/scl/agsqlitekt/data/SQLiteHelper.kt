package br.edu.ifsp.scl.agsqlitekt.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

class SQLiteHelper(context: Context, dbName: String, factory: SQLiteDatabase.CursorFactory?, version: Int):
    SQLiteOpenHelper(context, dbName, null, version){

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "GerenciadorFinanceiro.db"

        //Contato
        val KEY_ID = "id"
        val TABLE_NAME = "contatos"
        val KEY_NOME = "nome"
        val KEY_FONE = "fone"
        val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ContaDAO.creteTable())
        db.execSQL(ClassificacaoDAO.creteTable())
        db.execSQL(TransacaoDAO.creteTable())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


/*
    companion object {
        val DATABASE_NAME = "agenda.db"
        val KEY_ID = "id"
        val TABLE_NAME = "contatos"
        val KEY_NOME = "nome"
        val KEY_FONE = "fone"
        val KEY_EMAIL = "email"
    }

    private val DATABASE_VERSION = version

    private val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NOME + " TEXT, "
            + KEY_FONE + " TEXT, "
            + KEY_EMAIL + " TEXT)")

    val teste = " "

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
 */
}