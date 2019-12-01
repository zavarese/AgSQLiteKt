package br.edu.ifsp.scl.agsqlitekt.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.agsqlitekt.R
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar
import br.edu.ifsp.scl.agsqlitekt.data.ContaAdapter
import br.edu.ifsp.scl.agsqlitekt.data.ContaDAO

import br.edu.ifsp.scl.agsqlitekt.data.ContatoDAO;
import br.edu.ifsp.scl.agsqlitekt.data.TransacaoAdapter
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import br.edu.ifsp.scl.agsqlitekt.model.Contato;



class DetalheActivity : AppCompatActivity() {
    lateinit var c: Conta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        if (intent.hasExtra("conta")) {
            this.c = intent.getSerializableExtra("conta") as Conta

            val descricao: EditText = findViewById(R.id.editDescricao)
            descricao.setText(c.descricao)

            val saldoInicial: EditText = findViewById(R.id.editSaldoInicial)
            saldoInicial.setText(c.saldoInicial.toString())

            val saldoFinal: EditText = findViewById(R.id.editSaldoFinal)
            saldoFinal.setText(c.saldoFinal.toString())

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detalhe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //finish() // close this activity and return to preview activity (if there is any)
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivityForResult(i, 1)
            return true
        }

        if (id == R.id.action_transContato) {

            val i = Intent(applicationContext, TransListaActivity::class.java)
            i.putExtra("conta", c)
            startActivityForResult(i, 1)

            return true
        }

        if (id == R.id.action_alterarContato) {

            val dao = ContaDAO(this)

            var descricao = (findViewById(R.id.editDescricao) as EditText).text.toString().trim()
            var saldoInicial = (findViewById(R.id.editSaldoInicial) as EditText).text.toString().trim()
            var saldoFinal = (findViewById(R.id.editSaldoFinal) as EditText).text.toString().trim()

            if(descricao.equals("")){ descricao = "<vazio>" }
            if(saldoInicial.equals("")){ saldoInicial = "0" }
            if(saldoFinal.equals("")){ saldoFinal = "0" }

            c.descricao = descricao
            c.saldoInicial = saldoInicial.toDouble()
            c.saldoFinal = saldoFinal.toDouble()

            dao.alterarConta(c)
            Log.d("ID: ", Integer.toString(c.id))
            Log.d("NOME: ", c.descricao)

            MainActivity.adapter.atualizaContaAdapter(c)

            Toast.makeText(applicationContext, "Contato alterado", Toast.LENGTH_LONG).show()

            finish()
        }

        if (id == R.id.action_excluirContato) {
            val dao = ContaDAO(this)
            dao.excluirConta(this,c)
            MainActivity.adapter.apagaContaAdapter(c)

            Toast.makeText(applicationContext, "Conta excluída", Toast.LENGTH_LONG).show()
            finish()

        }


        return super.onOptionsItemSelected(item)
    }

}
