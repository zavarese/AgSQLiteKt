package br.edu.ifsp.scl.agsqlitekt.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.agsqlitekt.R
import br.edu.ifsp.scl.agsqlitekt.model.Transacao
import br.edu.ifsp.scl.agsqlitekt.utils.ListaTrans
import java.util.ArrayList

class TransacaoAdapter(transacoesList: MutableList<Transacao>) :
    RecyclerView.Adapter<TransacaoAdapter.TransacaoViewHolder>(), Filterable {

    val transacoes: MutableList<Transacao> = transacoesList
    var transacoesListFiltered: List<Transacao> = transacoesList

    private var clickListener: ItemClickListener? = null

    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        holder.data.text = transacoesListFiltered[position].dataHora
        holder.classificacao.text = transacoesListFiltered[position].classificacao
        holder.valor.text = transacoesListFiltered[position].valor.toString()

    }


    fun adicionaTransAdapter(t: Transacao) {
        t.descricao = t.descricao
        t.valor = t.valor
        transacoes.add(t)
        var i = transacoes.indexOf(t)
        transacoes.sortedBy { it.descricao }

        notifyDataSetChanged()
    }

    fun atualizaTransAdapter(t: Transacao) {
        t.descricao = t.descricao
        t.valor = t.valor
        val l = ListaTrans()
        var i = l.getPositionlist(transacoes,t.id)
        transacoes.set(i,t)
        transacoes.sortBy { it.descricao }

        notifyItemChanged(transacoes.indexOf(t))
    }

    fun apagaTransAdapter(t: Transacao) {
        val l = ListaTrans()
        var pos = l.getPositionlist(transacoes,t.id)

        transacoes.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun getTransListFiltered(): List<Transacao> {
        return transacoesListFiltered
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        clickListener = itemClickListener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trans_celula, parent, false)

        return TransacaoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transacoesListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    transacoesListFiltered = transacoes
                } else {
                    val filteredList = ArrayList<Transacao>()
                    for (row in transacoes) {
                        if (row.dataHora.toLowerCase().contains(charString.toLowerCase()) || row.classificacao.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    transacoesListFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = transacoesListFiltered
                return filterResults

            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                transacoesListFiltered = results.values as ArrayList<Transacao>
                notifyDataSetChanged()

            }
        }
    }


    inner class TransacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal val data: TextView
        internal val classificacao: TextView
        internal val valor: TextView

        init {
            classificacao = itemView.findViewById(R.id.classificacao) as TextView
            valor = itemView.findViewById(R.id.valor) as TextView
            data = itemView.findViewById(R.id.data) as TextView
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