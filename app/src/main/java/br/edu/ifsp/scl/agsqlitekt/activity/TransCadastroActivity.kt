package br.edu.ifsp.scl.agsqlitekt.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.edu.ifsp.scl.agsqlitekt.R
import br.edu.ifsp.scl.agsqlitekt.data.ContaDAO
import br.edu.ifsp.scl.agsqlitekt.data.TransacaoDAO
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import br.edu.ifsp.scl.agsqlitekt.model.Transacao
import br.edu.ifsp.scl.agsqlitekt.utils.Operacao
import br.edu.ifsp.scl.agsqlitekt.utils.Status
import br.edu.ifsp.scl.agsqlitekt.utils.Tipo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_layout_trans.*
import kotlinx.android.synthetic.main.common_layout_trans.toolbar
import java.sql.Timestamp

class TransCadastroActivity : AppCompatActivity() {

    lateinit var c: Conta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_cadastro)
        toolbar.title = "Cadastro Transação"
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        if (intent.hasExtra("conta")) {
            this.c = intent.getSerializableExtra("conta") as Conta

        }
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
            if (item.getItemId() == android.R.id.home) {
                //finish() // close this activity and return to preview activity (if there is any)
                val i = Intent(applicationContext, TransListaActivity::class.java)
                startActivityForResult(i, 1)
                return true
            }
        }

        if (id == R.id.action_salvarContato) {
            lateinit var operacao: Operacao

            val dao = TransacaoDAO(this)
            var auxValor: Double

            var descricao = (findViewById(R.id.editDesc) as EditText).text.toString().trim()
            var valor = (findViewById(R.id.editValor) as EditText).text.toString().trim()
            var data = ((findViewById(R.id.initial_date) as EditText).text.toString()).trim()
            val tipo = if (credRb.isChecked) Tipo.Credito else Tipo.Debito
            val periodicidade = operacaoSpn.selectedItemPosition
            val classificacao = classSpn.selectedItem.toString()

            if(valor.equals("")){ valor = "0" }
            if(data.equals("")){ data = "31/12/2999" }

            if(tipo.text.equals("Debito")){
                auxValor = valor.toDouble() * -1
                valor = auxValor.toString()
            }

            when(periodicidade){
                0 -> operacao = Operacao.Unica
                1 -> operacao = Operacao.Diaria
                2 -> operacao = Operacao.Quinzenal
                3 -> operacao = Operacao.Mensal
                4 -> operacao = Operacao.Anual
            }


            val t = Transacao(0,c,null,tipo,operacao,classificacao,descricao,data,valor.toDouble(), Status.Efetivado, "")

            val idTransacao = dao.incluir(t).toInt()

            TransListaActivity.adapter.adicionaTransAdapter(t)

            Toast.makeText(applicationContext, "Transação inserida", Toast.LENGTH_LONG).show()

            /*
            val i = Intent(applicationContext, TransListaActivity::class.java)
                i.putExtra("conta", c)
                startActivityForResult(i, 2)
             */

            finish()


        }

        return super.onOptionsItemSelected(item)
    }

}
