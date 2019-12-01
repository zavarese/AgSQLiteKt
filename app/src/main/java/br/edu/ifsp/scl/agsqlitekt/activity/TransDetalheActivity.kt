package br.edu.ifsp.scl.agsqlitekt.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import br.edu.ifsp.scl.agsqlitekt.R
import br.edu.ifsp.scl.agsqlitekt.data.TransacaoDAO
import br.edu.ifsp.scl.agsqlitekt.model.Transacao
import kotlinx.android.synthetic.main.common_layout_trans.*
import br.edu.ifsp.scl.agsqlitekt.data.ContaDAO
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import br.edu.ifsp.scl.agsqlitekt.utils.*


class TransDetalheActivity : AppCompatActivity() {
    lateinit var t: Transacao
    lateinit var c: Conta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_detalhe)
        toolbar.title = "Cadastro Transação"
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        if (intent.hasExtra("transacao")) {
            this.t = intent.getSerializableExtra("transacao")  as Transacao
            var auxValor: Double

            val data: EditText = findViewById(R.id.initial_date)
            data.setText(t.dataHora)

            val valor: EditText = findViewById(R.id.editValor)

            if(t.valor<0){
                auxValor = Modulo.modulo(t.valor)
                valor.setText(auxValor.toString())
            }else{
                valor.setText(t.valor.toString())
            }

            //valor.setText(t.valor.toString())

            val descricao: EditText = findViewById(R.id.editDesc)
            descricao.setText(t.descricao)

            val tipo: RadioGroup = findViewById(R.id.tipoRg)
            if(t.tipo.text.equals("Credito")) {
                tipo.check(R.id.credRb)
            }else{
                tipo.check(R.id.debt2Rb)
            }

            c = t.contaRecebedor!!

            //Spinner Operacao
            val operacaoSpn: Spinner = findViewById(R.id.operacaoSpn)
            SpinnerUtil.setSelect(this,t.operacao.text,operacaoSpn,"periodo_class")

            //Spinner Classificacao
            val classificacaoSpn: Spinner = findViewById(R.id.classSpn)
            SpinnerUtil.setSelect(this,t.classificacao,classificacaoSpn,"tipo_class")

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detalhe_trans, menu)
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
            val i = Intent(applicationContext, TransListaActivity::class.java)
            i.putExtra("conta", c)
            startActivityForResult(i, 1)
            return true
        }


        if (id == R.id.action_alterarContato) {

            val dao = TransacaoDAO(this)
            var auxValor: Double

            var data = (findViewById(R.id.initial_date) as EditText).text.toString().trim()
            var valor = (findViewById(R.id.editValor) as EditText).text.toString().trim()
            val descricao = (findViewById(R.id.editDesc) as EditText).text.toString()
            val tipo = if (credRb.isChecked) Tipo.Credito else Tipo.Debito
            val operacaoSpn = (findViewById(R.id.operacaoSpn) as Spinner).selectedItem.toString()
            val classificacaoSpn = (findViewById(R.id.classSpn) as Spinner).selectedItem.toString()

            if(valor.equals("")){ valor = "0" }
            if(data.equals("")){ data = "31/12/2999" }

            if(tipo.text.equals("Debito")){
                auxValor = Modulo.modulo(valor.toDouble())
                auxValor = auxValor * -1
                valor = auxValor.toString()
            }else{
                auxValor = Modulo.modulo(valor.toDouble())
                valor = auxValor.toString()
            }

            t.valor = valor.toDouble()
            t.descricao = descricao
            t.dataHora = data
            t.tipo = tipo
            t.classificacao = classificacaoSpn
            t.operacao = Operacao.valueOf(operacaoSpn)

            dao.atualizaTrans(t)

            Log.d("ID: ", Integer.toString(t.id))
            Log.d("NOME: ", t.descricao)

            TransListaActivity.adapter.atualizaTransAdapter(t)

            Toast.makeText(applicationContext, "Transação alterada", Toast.LENGTH_LONG).show()

            finish()
        }

        if (id == R.id.action_excluirContato) {
            val dao = TransacaoDAO(this)
            dao.excluirTransacao(t)
            TransListaActivity.adapter.apagaTransAdapter(t)

            Toast.makeText(applicationContext, "Transação excluída", Toast.LENGTH_LONG).show()
            finish()

        }


        return super.onOptionsItemSelected(item)
    }
}
