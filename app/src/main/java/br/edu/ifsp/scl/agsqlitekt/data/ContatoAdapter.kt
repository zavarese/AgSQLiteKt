package br.edu.ifsp.scl.agsqlitekt.data

import android.content.Context
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifsp.scl.agsqlitekt.R
import androidx.annotation.NonNull;
import br.edu.ifsp.scl.agsqlitekt.model.Contato
import kotlinx.android.synthetic.main.contato_celula.view.*


class ContatoAdapter(contatoList: MutableList<Contato>) :
    RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>(), Filterable {

    val contatos: MutableList<Contato> = contatoList
    var contatoListFiltered: List<Contato> = contatoList

    private var clickListener: ItemClickListener? = null


    fun adicionaContatoAdapter(c: Contato) {
        c.nome = c.nome.toUpperCase()
        contatos.add(c)
        contatos.sortedBy { it.nome }

        notifyDataSetChanged()
    }

    fun atualizaContatoAdapter(c: Contato) {
        c.nome = c.nome.toUpperCase()
        contatos.set(contatos.indexOf(c),c)
        notifyItemChanged(contatos.indexOf(c))
    }

    fun apagaContatoAdapter(c: Contato) {
        val pos = contatos.indexOf(c)
        contatos.remove(c)
        notifyItemRemoved(pos)
    }

    fun getContactListFiltered(): List<Contato> {
        return contatoListFiltered
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        clickListener = itemClickListener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contato_celula, parent, false)

        return ContatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        holder.nome.text = contatoListFiltered[position].nome
    }

    override fun getItemCount(): Int {
        return contatoListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    contatoListFiltered = contatos
                } else {
                    val filteredList = ArrayList<Contato>()
                    for (row in contatos) {
                        if (row.nome.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    contatoListFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = contatoListFiltered
                return filterResults

            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                contatoListFiltered = results.values as ArrayList<Contato>
                notifyDataSetChanged()

            }
        }
    }


    inner class ContatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal val nome: TextView

        init {
            nome = itemView.findViewById(R.id.descricao) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (clickListener != null)
                clickListener!!.onItemClick(getAdapterPosition())
        }
    }


    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

}