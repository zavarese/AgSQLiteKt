package br.edu.ifsp.scl.agsqlitekt.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import br.edu.ifsp.scl.agsqlitekt.model.Transacao
import java.util.ArrayList

class ContaDAO(context: Context){

    private val dbhelper: SQLiteHelper = SQLiteHelper(context,SQLiteHelper.DATABASE_NAME,null,1)

    object ContaEntry : BaseColumns {
        const val TABLE_NAME = "conta"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_DESCRICAO = "cta_descricao"
        const val COLUMN_SALDO_INICIAL = "cta_saldo_inicial"
        const val COLUMN_SALDO_FINAL = "cta_saldo_final"
    }

    companion object {
        fun deleteTable() = "DROP TABLE IF EXISTS ${ContaEntry.TABLE_NAME}"
        fun creteTable() =
            "CREATE TABLE ${ContaEntry.TABLE_NAME} (" +
                    "${ContaEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${ContaEntry.COLUMN_DESCRICAO} TEXT," +
                    "${ContaEntry.COLUMN_SALDO_INICIAL} DOUBLE, " +
                    "${ContaEntry.COLUMN_SALDO_FINAL} DOUBLE)"
    }


    fun listaContas(): ArrayList<Conta> {
        val database = dbhelper.readableDatabase
        val contas = ArrayList<Conta>()

        val cursor: Cursor

        cursor = database.query(
            ContaEntry.TABLE_NAME,
            null, null, null, null, null,
            ContaEntry.COLUMN_DESCRICAO
        )
        while (cursor.moveToNext()) {
            val conta = Conta(cursor.getInt(cursor.getColumnIndex(ContaEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ContaEntry.COLUMN_DESCRICAO)),
                cursor.getDouble(cursor.getColumnIndex(ContaEntry.COLUMN_SALDO_INICIAL)),
                cursor.getDouble(cursor.getColumnIndex(ContaEntry.COLUMN_SALDO_FINAL)))

            conta?.saldoFinal = calculaSaldo(conta)

            contas.add(conta)
        }

        cursor.close()
        database.close()

        return contas
    }

    fun getConta(id: Int): Conta? {
        val database = dbhelper.readableDatabase
        var conta: Conta? = null
        val whereClause = ContaEntry.COLUMN_ID+"=?"
        val whereArgs = arrayOf(id.toString())

        val cursor: Cursor

        cursor = database.query(
            ContaEntry.TABLE_NAME,
            null,
            whereClause,
            whereArgs, null, null,null
        )
/*
        Log.d("COLUMN=",cursor.getInt(cursor.getColumnIndex(ContaEntry.COLUMN_ID)+1).toString())
        Log.d("COLUMN=",cursor.getString(cursor.getColumnIndex(ContaEntry.COLUMN_DESCRICAO)).toString())
        Log.d("COLUMN=",cursor.getDouble(cursor.getColumnIndex(ContaEntry.COLUMN_SALDO_INICIAL)).toString())
        Log.d("COLUMN=",cursor.getDouble(cursor.getColumnIndex(ContaEntry.COLUMN_SALDO_FINAL)).toString())
*/
        if (cursor.moveToNext()) {
            conta = Conta(cursor.getInt(cursor.getColumnIndex(ContaEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ContaEntry.COLUMN_DESCRICAO)),
                cursor.getDouble(cursor.getColumnIndex(ContaEntry.COLUMN_SALDO_INICIAL)),
                cursor.getDouble(cursor.getColumnIndex(ContaEntry.COLUMN_SALDO_FINAL)))

            conta?.saldoFinal = calculaSaldo(conta)
        }

        cursor.close()
        database.close()

        return conta
    }

    fun incluirConta(conta: Conta):Int{
        val database = dbhelper.writableDatabase
        val values = ContentValues()
        values.put(ContaEntry.COLUMN_DESCRICAO, conta.descricao)
        values.put(ContaEntry.COLUMN_SALDO_INICIAL, conta.saldoInicial)
        values.put(ContaEntry.COLUMN_SALDO_FINAL, conta.saldoFinal)
        val id = database.insert(ContaEntry.TABLE_NAME, null, values)
        database.close()
        return id.toInt()
    }

    fun excluirConta(context: Context,conta: Conta):Int{
        TransacaoDAO.excluirTransacao(context,conta.id)
        val database = dbhelper.writableDatabase
        val id = database.delete(ContaEntry.TABLE_NAME,
            ContaEntry.COLUMN_ID + "=" + conta.id, null)
        database.close()

        return id.toInt()
    }

    fun alterarConta(conta: Conta){
        val database = dbhelper.writableDatabase
        val values = ContentValues()
        values.put(ContaEntry.COLUMN_DESCRICAO, conta.descricao)
        values.put(ContaEntry.COLUMN_SALDO_INICIAL, conta.saldoInicial)
        database.update(ContaEntry.TABLE_NAME, values,
            ContaEntry.COLUMN_ID + "=?",
            arrayOf(conta.id.toString()))
        database.close()
    }

    fun alterarSaldoInicial(conta: Conta){
        val database = dbhelper.writableDatabase
        val values = ContentValues()
        values.put(ContaEntry.COLUMN_SALDO_INICIAL, conta.saldoInicial)
        database.update(ContaEntry.TABLE_NAME, values,
            "${ContaEntry.COLUMN_ID}=?",
            arrayOf(conta.id.toString()))
        database.close()
    }

    fun alterarSaldo(conta: Conta){
        val database = dbhelper.writableDatabase
        val values = ContentValues()
        values.put(ContaEntry.COLUMN_SALDO_FINAL, conta.saldoFinal)
        database.update(ContaEntry.TABLE_NAME, values,
            "${ContaEntry.COLUMN_ID}=?",
            arrayOf(conta.id.toString()))
        database.close()
    }

    fun calculaSaldo(conta: Conta): Double{
        var saldo: Double = conta.saldoInicial
        var query = "SELECT SUM("+TransacaoDAO.TransacaoEntry.COLUMN_VALOR+") as SALDO FROM "+TransacaoDAO.TransacaoEntry.TABLE_NAME+
                " WHERE "+TransacaoDAO.TransacaoEntry.COLUMN_CTA_REALIZA+"="+conta.id+
                " GROUP BY "+TransacaoDAO.TransacaoEntry.COLUMN_CTA_REALIZA
        val database = dbhelper.readableDatabase
        val cursor = database.rawQuery(query,null)

        if (cursor.moveToNext()) {
            saldo = saldo + cursor.getDouble(cursor.getColumnIndex("SALDO"))
        }

        cursor.close()
        database.close()

        return saldo
    }
}
