package br.edu.ifsp.scl.agsqlitekt.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.agsqlitekt.R
import br.edu.ifsp.scl.agsqlitekt.data.ContaAdapter
import br.edu.ifsp.scl.agsqlitekt.data.TransacaoAdapter
import br.edu.ifsp.scl.agsqlitekt.data.TransacaoDAO
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import br.edu.ifsp.scl.agsqlitekt.model.Transacao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class TransListaActivity : AppCompatActivity() {

    lateinit var c: Conta
    lateinit var recyclerView: RecyclerView

    companion object {
        lateinit var adapter: TransacaoAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_lista)
        toolbar.title = "Transações"
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        if (intent.hasExtra("conta")) {
            this.c = intent.getSerializableExtra("conta") as Conta
        }

        val dao = TransacaoDAO(this)

        recyclerView = findViewById(R.id.recyclerview)
        val layout = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layout)

        val transacoes: MutableList<Transacao> = dao.listaTransacoes(c.id)
        transacoes.sortBy { it.data }

        adapter = TransacaoAdapter(transacoes)

        recyclerView.setAdapter(adapter)

        adapter.setClickListener(object : TransacaoAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                val t = adapter.transacoesListFiltered.get(position)

                val i = Intent(applicationContext, TransDetalheActivity::class.java)
                i.putExtra("transacao", t)
                startActivityForResult(i, 2)

            }
        })

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                val t = adapter.getTransListFiltered().get(viewHolder.adapterPosition)

                dao.excluirTransacao(t)
                adapter.apagaTransAdapter(t)
                Toast.makeText(applicationContext, "Transação apagada", Toast.LENGTH_LONG).show()

            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                val p = Paint()

                val itemView = viewHolder.itemView
                val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                val width = height / 3

                p.setColor(ContextCompat.getColor(baseContext, android.R.color.holo_orange_light))

                val background = RectF(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    dX,
                    itemView.bottom.toFloat()
                )

                c.drawRect(background, p)

                icon = BitmapFactory.decodeResource(resources, android.R.drawable.ic_delete)

                val icon_dest = RectF(
                    itemView.left.toFloat() + width, itemView.top.toFloat() + width,
                    itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width
                )

                c.drawBitmap(icon, null, icon_dest, null)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }


        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val i = Intent(applicationContext, TransCadastroActivity::class.java)
                i.putExtra("conta", c)
                startActivityForResult(i, 1)

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView: SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setMaxWidth(Integer.MAX_VALUE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                adapter.getFilter().filter(query)

                return true
            }
        })

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

        return super.onOptionsItemSelected(item)
    }

}
