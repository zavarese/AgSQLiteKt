package br.edu.ifsp.scl.agsqlitekt.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.agsqlitekt.R
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar
import br.edu.ifsp.scl.agsqlitekt.data.ContaDAO

import br.edu.ifsp.scl.agsqlitekt.model.Contato
import br.edu.ifsp.scl.agsqlitekt.data.ContatoDAO
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import kotlinx.android.synthetic.main.activity_main.*


class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_cadastro, menu)
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

        if (id == R.id.action_salvarContato) {
            //val dao = ContatoDAO(this)
            val dao = ContaDAO(this)
            /*
            val nome = (findViewById(R.id.editTextNome) as EditText).text.toString()
            val fone = (findViewById(R.id.editTextTel) as EditText).text.toString()
            val email = (findViewById(R.id.editTextEmail) as EditText).text.toString()
            */
            var descricao = (findViewById(R.id.editDescricao) as EditText).text.toString().trim()
            var saldoInicial = (findViewById(R.id.editSaldoInicial) as EditText).text.toString().trim()
            var saldoFinal = (findViewById(R.id.editSaldoFinal) as EditText).text.toString().trim()

            if(descricao.equals("")){ descricao = "<vazio>" }
            if(saldoInicial.equals("")){ saldoInicial = "0" }
            if(saldoFinal.equals("")){ saldoFinal = saldoInicial }

            val c = Conta(0,descricao, saldoInicial.toDouble(), saldoFinal.toDouble())

            val idConta = dao.incluirConta(c).toInt()
            c.id = idConta

            MainActivity.adapter.adicionaContaAdapter(c)

            Toast.makeText(applicationContext, "Contato inserido", Toast.LENGTH_LONG).show()

            finish()


        }

        return super.onOptionsItemSelected(item)
    }


}
