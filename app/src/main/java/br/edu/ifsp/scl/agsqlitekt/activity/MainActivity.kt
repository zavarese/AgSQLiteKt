package br.edu.ifsp.scl.agsqlitekt.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.agsqlitekt.R

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.edu.ifsp.scl.agsqlitekt.data.ContaAdapter
import br.edu.ifsp.scl.agsqlitekt.data.ContaDAO
import br.edu.ifsp.scl.agsqlitekt.data.ContatoAdapter
import br.edu.ifsp.scl.agsqlitekt.data.ContatoDAO
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import br.edu.ifsp.scl.agsqlitekt.model.Contato
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView


    companion object {
        //lateinit var adapter: ContatoAdapter
        lateinit var adapter: ContaAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        //val dao = ContatoDAO(this)
        val dao = ContaDAO(this)

        recyclerView = findViewById(R.id.recyclerview)
        val layout = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layout)

        val contas: MutableList<Conta> = dao.listaContas()

        adapter = ContaAdapter(contas)

        recyclerView.setAdapter(adapter)

        adapter.setClickListener(object : ContaAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                val c = adapter.contasListFiltered.get(position)

                val i = Intent(applicationContext, DetalheActivity::class.java)
                i.putExtra("conta", c)
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
                /*
                val c = adapter.getContaListFiltered().get(viewHolder.adapterPosition)

                dao.excluirConta(this,c)

                adapter.apagaContaAdapter(c)

                Toast.makeText(applicationContext, "Conta apagada", Toast.LENGTH_LONG).show()

                 */

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
                val i = Intent(applicationContext, CadastroActivity::class.java)
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


        return super.onOptionsItemSelected(item)
    }


}
