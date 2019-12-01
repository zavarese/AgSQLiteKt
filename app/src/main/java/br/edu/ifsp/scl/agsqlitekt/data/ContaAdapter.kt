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
import br.edu.ifsp.scl.agsqlitekt.model.Conta
import kotlinx.android.synthetic.main.contato_celula.view.*

class ContaAdapter(contasList: MutableList<Conta>) :
    RecyclerView.Adapter<ContaAdapter.ContaViewHolder>(), Filterable {

        val contas: MutableList<Conta> = contasList
        var contasListFiltered: List<Conta> = contasList

        private var clickListener: ItemClickListener? = null


        fun adicionaContaAdapter(c: Conta) {
            c.descricao = c.descricao
            c.saldoFinal = c.saldoFinal
            contas.add(c)
            var i = contas.indexOf(c)
            contas.sortedBy { it.descricao }

            notifyDataSetChanged()
        }

        fun atualizaContaAdapter(c: Conta) {
            c.descricao = c.descricao
            c.saldoFinal = c.saldoFinal
            var i = contas.indexOf(c)
            contas.set(contas.indexOf(c),c)
            notifyItemChanged(contas.indexOf(c))
        }

        fun apagaContaAdapter(c: Conta) {
            val pos = contas.indexOf(c)
            contas.remove(c)
            notifyItemRemoved(pos)
        }

        fun getContaListFiltered(): List<Conta> {
            return contasListFiltered
        }

        fun setClickListener(itemClickListener: ItemClickListener) {
            clickListener = itemClickListener

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.contato_celula, parent, false)

            return ContaViewHolder(view)
        }

        override fun onBindViewHolder(holder: ContaViewHolder, position: Int) {
            holder.descricao.text = contasListFiltered[position].descricao
            holder.saldoFinal.text = contasListFiltered[position].saldoFinal.toString()
        }

        override fun getItemCount(): Int {
            return contasListFiltered.size
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
                    val charString = constraint.toString()
                    if (charString.isEmpty()) {
                        contasListFiltered = contas
                    } else {
                        val filteredList = ArrayList<Conta>()
                        for (row in contas) {
                            if (row.descricao.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row)
                            }
                        }
                        contasListFiltered = filteredList
                    }

                    val filterResults = Filter.FilterResults()
                    filterResults.values = contasListFiltered
                    return filterResults

                }

                override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                    contasListFiltered = results.values as ArrayList<Conta>
                    notifyDataSetChanged()

                }
            }
        }


        inner class ContaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            internal val descricao: TextView
            internal val saldoFinal: TextView

            init {
                descricao = itemView.findViewById(R.id.descricao) as TextView
                saldoFinal = itemView.findViewById(R.id.saldo) as TextView
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